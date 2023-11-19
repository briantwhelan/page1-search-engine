package com.example;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.example.CustomisedAnalyzer.CustomAnalyzer_Syn_stp;
import com.example.indexing.indexingConstrution;
import com.example.queryCreation.ProcessingFromTopicParser;

/**
 * SearchEngine indexes and searches documents.
 */
// ADD QUERY EXPANSO

// SIMIALTY ANALYZER

public class SearchEngine {
	public static final String FT_PATH = "./data/ft";
	public static final String LATIMES_PATH = "./data/latimes";
	public static final String FR_PATH = "./data/fr94";
	public static final String DIRTORY_ADDRESS = "./dir";
	private final static Path DATA_PATH_TOPIC = Paths.get("./data/queryCreationdataset/topics.txt");
	private static final int NUMBER_DOCUMENT_QUERY = 1000;
	private static final String TITLE_MARCO = "title";
	private static final String CONTENT_MARCO = "content";
	private static final String MYOUTPUT_FILE_DIRECTORY = "./output";
	private final String DOC_NUMBE_MARCO = "docno";
	public SearchEngine() {
	}

	/*
	 * Code from this link:
	 * https://github.com/taaanmay/CS7IS3-Assignment-2/blob/main/src/main/java/app/
	 * QueryResolverWithExp.java
	 */
	private static Query expandQuery(IndexSearcher searcher, Analyzer analyzer, Query queryContents, ScoreDoc[] hits,
			IndexReader reader) throws Exception {
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(queryContents, BooleanClause.Occur.SHOULD);
		TopDocs topDocs = searcher.search(queryContents, 4);

		for (ScoreDoc score : topDocs.scoreDocs) {
			Document hitDoc = reader.document(score.doc);
			String fieldText = hitDoc.getField("content").stringValue();
			String[] moreLikeThisField = {"content"};
			MoreLikeThisQuery expandedQueryMoreLikeThis = new MoreLikeThisQuery(fieldText, moreLikeThisField, analyzer,
					"content");
			Query expandedQuery = expandedQueryMoreLikeThis.rewrite(reader);
			queryBuilder.add(expandedQuery, BooleanClause.Occur.SHOULD);
		}
		return queryBuilder.build();
	}

	/**
	 * Main method for SearchEngine.
	 */
	public static <Document> void main(String[] args) throws Exception {

		System.out.println(
				"Final model :Analyzer: Customised analyzer" + "\n" + "Scoring simualrity LMJelinekMercerSimilarity"
						+ "\n" + "Query creation : TD-IDF selection" + "\n" + "Query expansion : Proximity search");

		// indexing StandaradAnalyzer and BM25SIMIALRITY
		System.out.println("Starting Loading Documents into Memory  ");
		indexingConstrution myIndexCreation = new indexingConstrution();
		// float lambda = settings.getAsFloat("lambda", 0.1f);
		ArrayList<org.apache.lucene.document.Document> LuenceAllDocuemnt = myIndexCreation.getLuenceAllDocuemnt();

		System.out.println("Starting indexing ");
		Analyzer currentMyAnalyzer = new CustomAnalyzer_Syn_stp();
		IndexWriterConfig myWriterconfig = new IndexWriterConfig(currentMyAnalyzer);

		myWriterconfig.setMaxBufferedDocs(100000);
		// SET HERE
		Similarity mySimilairty = new LMJelinekMercerSimilarity(0.7f);

		myWriterconfig.setSimilarity(mySimilairty);

		Path myindexPath = Paths.get(DIRTORY_ADDRESS);
		Directory indexDir = FSDirectory.open(myindexPath);
		// Overwrite seveearl times
		myWriterconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(indexDir, myWriterconfig);
		// add document to the index
		indexWriter.forceMerge(100000);
		indexWriter.addDocuments(LuenceAllDocuemnt);

		indexWriter.close();
		System.out.println("Finishing indexing  hhh");

		// FinancialTimesReader.readDocuments(FT_PATH);
		// LosAngelesTimesReader.readDocuments(LATIMES_PATH);
		// FederalRegisterReader.readDocuments(FR_PATH);
		// NEXT step create query
		System.out.println("Deleting the write lock file for writing when indexing  ");

		// DELETE write lock file
		String directoryPath = "./dir";
		String fileName = "write.lock";

		File lockFile = new File(directoryPath, fileName);

		if (lockFile.exists()) {
			boolean isDeleted = lockFile.delete();
			if (isDeleted) {
				System.out.println(fileName + " has been successfully deleted.");
			} else {
				System.out.println("Could not delete " + fileName + ".");
			}
		} else {
			System.out.println(fileName + " does not exist in the directory " + directoryPath);
		}

		// Bug at searching
		System.out.println("Start searching");
		Directory dirr = FSDirectory.open(Paths.get(DIRTORY_ADDRESS));
		DirectoryReader dirReader = DirectoryReader.open(dirr);
		IndexSearcher searcher = new IndexSearcher(dirReader);
		searcher.setSimilarity(mySimilairty);

		// FINISHING SEARCH CONFIG

		//
		System.out.println("Query setting ");

		QueryParser parser_single_mutiple = null;
		float weightForTittle = 0.2f;

		float weightForDescr = 1.1f;
		Map<String, Float> bootssetting3 = new HashMap<>();
		bootssetting3.put(TITLE_MARCO, weightForTittle);
		bootssetting3.put(CONTENT_MARCO, weightForDescr);
		String[] Feature3_CONSTANT_TITLE_DESC_FEATURE = new String[]{TITLE_MARCO, CONTENT_MARCO};
		parser_single_mutiple = new MultiFieldQueryParser(Feature3_CONSTANT_TITLE_DESC_FEATURE, currentMyAnalyzer,
				bootssetting3);

		parser_single_mutiple.setAllowLeadingWildcard(true);
		// String []Feature6_CONSTANT_BIBI_DESCI_FEATURE =new String
		// []{constant_bibi,constant_descir};
		// String []Feature7_CONSTANT_ALL_THREE_FEATURE =new String
		// []{constant_title,constant_bibi,constant_descir};

		// Setting for writting file
		int initlisedTopicNumber = 401;
		String saveFileAddress = "result" + ".txt";
		FileWriter fileWriter = new FileWriter(saveFileAddress);

		// List<Query> local = my.parseQueries();
		// ArrayList<String> OutputList = new ArrayList<>();

		// Read query creation using TF-IDF
		ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
		myqueryGeneration.run();
		ArrayList<String> myQueryList = myqueryGeneration.getQueryList();

		for (int i = 0; i < myQueryList.size(); i++) {
			String querytempDescription = myQueryList.get(i).trim();
			Query myqry = parser_single_mutiple.parse(querytempDescription);
			ScoreDoc[] mydocHit = {};
			Query expandedQuery = expandQuery(searcher, currentMyAnalyzer, myqry, mydocHit, dirReader);

			mydocHit = searcher.search(expandedQuery, NUMBER_DOCUMENT_QUERY).scoreDocs;

			final String DOC_NUMBE_MARCO = "docno";

			int RankLable = 1;

			if (RankLable % 100 == 0) {
				RankLable = 1;
			}
			for (int j = 0; j < mydocHit.length; j++) {
				ScoreDoc hit = mydocHit[j];

				String searchDOCNO = searcher.doc(hit.doc).get(DOC_NUMBE_MARCO);
				// query-id Q0 document-id rank score STANDARD

				
				String formatOutput = initlisedTopicNumber + " Q0 " + searchDOCNO + " " + RankLable + " " + hit.score
						+ " STANDARD";
				RankLable++;
				System.out.println(formatOutput);
				fileWriter.write(formatOutput + System.lineSeparator());
			}

			initlisedTopicNumber++;
		}

		fileWriter.close();

	}

}
