package com.example;

import com.example.fbisparser.Fbis95Structure;
import com.example.fbisparser.Fbis96Parser;
import com.example.fr.FederalRegisterDocument;
import com.example.fr.FederalRegisterReader;
import com.example.ft.FinancialTimesDocument;
import com.example.ft.FinancialTimesReader;
import com.example.latimes.LosAngelesTimesDocument;
import com.example.latimes.LosAngelesTimesReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;


public class Indexer {
  public static final String FT_PATH = "./page1-search-engine/data/ft";
  public static final String LATIMES_PATH = "./page1-search-engine/data/latimes";
  public static final String FR_PATH = "./page1-search-engine/data/fr94";

  private final String DOC_NO = "docno";
  private final String TITLE = "title";
  private final String CONTENT = "content";

  private ArrayList<Document> allLuceneDocuments;

  public Indexer() throws IOException {
    this.allLuceneDocuments = new ArrayList<>();
    // FBIS.
    Fbis96Parser myFbisParser = new Fbis96Parser();
    myFbisParser.loadFiles();
    ArrayList<Fbis95Structure> fbisDocuments = myFbisParser.getMyFbisContainer();
    for (Fbis95Structure d : fbisDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getDocno(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // Financial Times.
    ArrayList<FinancialTimesDocument> ftDocuments = FinancialTimesReader.readDocuments(FT_PATH);
    for (FinancialTimesDocument d : ftDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // Federal Register.
    ArrayList<FederalRegisterDocument> frDocuments = FederalRegisterReader.readDocuments(FR_PATH);
    for (FederalRegisterDocument d : frDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // LA Times.
    ArrayList<LosAngelesTimesDocument> laDocuments = LosAngelesTimesReader.readDocuments(LATIMES_PATH);
    for (LosAngelesTimesDocument d : laDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
  }
  public void indexAllDocuments(String indexDirectory, Analyzer analyzer, Similarity scorer) throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(4);


    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }); // FBIS
    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }); // FT
    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }); // FR
    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }); // LATIMES

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  }
  public void indexDocuments(String indexDirectory, Analyzer analyzer, Similarity scorer) throws IOException {
    String analyzerName = analyzer.getClass().getName()
            .substring(analyzer.getClass().getName().lastIndexOf('.') + 1);
    String scorerName = scorer.getClass().getName()
            .substring(scorer.getClass().getName().lastIndexOf('.') + 1);
    System.out.println("Indexing documents with " + analyzerName + " and " + scorerName + "...");
    IndexWriter indexWriter = createWriter(indexDirectory, analyzer, scorer);
    indexWriter.forceMerge(100000);
    indexWriter.addDocuments(this.allLuceneDocuments);
    indexWriter.close();
    System.out.println("Indexed documents with " + analyzerName + " and " + scorerName
            + " and stored to directory " + indexDirectory + ".");
  }
//  public static void createIndex(AnalyzerType analyzerEnum, SimilarityType similarityEnum) throws Exception{
//    long startTime = System.currentTimeMillis()
//
//  }
  /**
   * Creates an IndexWriter with the given analyzer and scorer.
   *
   * @param analyzer Analyzer to use for indexing.
   * @param similarity Similarity to use for indexing.
   * @return IndexWriter object.
   */
  private static IndexWriter createWriter(String indexDirectory, Analyzer analyzer,
                                          Similarity similarity) throws IOException {
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    config.setMaxBufferedDocs(100000);
    config.setSimilarity(similarity);
    FSDirectory dir = FSDirectory.open(Paths.get(indexDirectory));
    IndexWriter writer = new IndexWriter(dir, config);
    return writer;
  }
}