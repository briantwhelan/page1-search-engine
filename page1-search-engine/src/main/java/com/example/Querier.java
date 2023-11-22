package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.example.queryCreation.QueryCreator;

public class Querier {
  public static final int TOP_DOCS_LIMIT = 1000;

  private static final String DOC_NO = "docno";
  private static final String TITLE  = "title";
  private static final String CONTENT  = "content";

  private final ArrayList<String> queries;

  /**
   * Constructor for Querier.
   * @param pathToQueries Path to the queries to search.
   */
  public Querier(String pathToQueries) {
    // Read queries from topics.
    QueryCreator qc = new QueryCreator();
    this.queries = qc.createQueries();
  }

  /**
   * Queries the documents with the given analyzer and scorer.
   * @param indexDirectory Path to the index directory.
   * @param analyzerName(analyzer) Analyzer to use for querying.
   * @param scorerName(scorer) Similarity to use for querying.
   * @throws Exception exception.
   */
  public void queryIndex(String indexDirectory, String analyzerName, String scorerName) throws Exception {
    System.out.println("Querying index at '" + indexDirectory + "'...");
//    String analyzerName = analyzer.getClass().getName()
//            .substring(analyzer.getClass().getName().lastIndexOf('.') + 1);
//    String scorerName = scorer.getClass().getName()
//            .substring(scorer.getClass().getName().lastIndexOf('.') + 1);
    Analyzer analyzer = get_Analyzer(analyzerName);
    Similarity scorer = get_Similarity(scorerName);
    File fout = new File("./results/" + analyzerName + "-" + scorerName+ ".txt");
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    IndexSearcher searcher = createIndexSearcher(indexDirectory, scorer);

    // Search index with all queries and write results to file.
    int queryID = 401;
    for (String q : queries) {
      q = q.trim();
      TopDocs results = search(q, searcher, analyzer);
      String queryResults = "";
      int rank = 1;
      for (ScoreDoc sd : results.scoreDocs) {
        // Output in trec_eval format - see http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system.
        // Format: query-id Q0 document-id rank score STANDARD
        queryResults += queryID + " Q0 " + (searcher.doc(sd.doc).get(DOC_NO)) + " "
                + rank + " " + sd.score + " " + analyzerName + "-" + scorerName + "\n";
        rank++;
      }
      bw.write(queryResults);
      queryID++;
    }
    bw.close();
    System.out.println("Queried index with " + analyzerName + " and " + scorerName + " results saved to '"  + fout.getPath() + "'.\n");
  }

  /**
   * Creates an IndexSearcher with the given scorer.
   * @param scorer Similarity to use for querying.
   * @throws IOException input-otput exception.
   */
  private static IndexSearcher createIndexSearcher(String indexDirectory, Similarity scorer) throws IOException {
    // Use file system directory to retrieve index.
    Directory dir = FSDirectory.open(Paths.get(indexDirectory));
    IndexReader reader = DirectoryReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(reader);
    // Configure index searcher with scorer.
    searcher.setSimilarity(scorer);
    return searcher;
  }

  private static QueryParser createQueryParser(Analyzer analyzer) {
    // Create query parser.
    float weightForTitle = 0.2f;
    float weightForDesc =1.1f;
    Map<String, Float> boosts = new HashMap<>();
    boosts.put(TITLE, weightForTitle);
    boosts.put(CONTENT, weightForDesc);
    String []fields = new String []{TITLE,CONTENT};
    QueryParser parser_single_mutiple = new MultiFieldQueryParser(fields, analyzer, boosts);
    parser_single_mutiple.setAllowLeadingWildcard(true);
    return parser_single_mutiple;
  }

  /**
   * Searches the documents for the given term.
   * @param term Term to search for.
   * @param searcher IndexSearcher to use for searching.
   * @param analyzer Analyzer to use for searching.
   * @throws Exception exception.
   */
  private static TopDocs search(String term, IndexSearcher searcher,
                                Analyzer analyzer) throws Exception {
    // Use specified analyzer to parse query.
    QueryParser qp = createQueryParser(analyzer);
    qp.setAllowLeadingWildcard(true);
    // Search both title and content fields for query term.
    // https://lucene.apache.org/core/2_9_4/queryparsersyntax.html.
    String queryTerm = "title:" + term + " OR content:" + term;
    // Search index for top results for query.
    Query query = qp.parse(queryTerm);
    TopDocs topDocs = searcher.search(query, TOP_DOCS_LIMIT);
    return topDocs;
  }

  private static Analyzer get_Analyzer(String analyzerType) {
    return switch (analyzerType) {
      case "EnglishAnalyzer" -> new EnglishAnalyzer();
      case "StandardAnalyzer" -> new StandardAnalyzer();
      case "WhitespaceAnalyzer" -> new WhitespaceAnalyzer();
      case "SimpleAnalyzer" -> new SimpleAnalyzer();
      default -> throw new IllegalArgumentException("Unknown analyzer type: " + analyzerType);
    };
  }
  private static Similarity get_Similarity(String scorer) {
    return switch (scorer) {
      case "BM25Similarity" -> new BM25Similarity();
      case "LMDirichletSimilarity" -> new LMDirichletSimilarity();
      case "ClassicSimilarity" -> new ClassicSimilarity();
      case "BooleanSimilarity" -> new BooleanSimilarity();
      case "Classic_LMDirichletSimilarity" -> new MultiSimilarity(new Similarity[]{new ClassicSimilarity(), new LMDirichletSimilarity()});
      case "BM25_ClassicSimilarity" -> new MultiSimilarity(new Similarity[]{new BM25Similarity(), new ClassicSimilarity()});
      case "BM25_LMDirichletSimilarity" -> new MultiSimilarity(new Similarity[]{new BM25Similarity(), new LMDirichletSimilarity()});
      default -> throw new IllegalArgumentException("Unknown analyzer type: " + scorer);
      //
    };
  }

}