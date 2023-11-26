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
  private ArrayList<Document> fbisDocuments;
  private ArrayList<Document> ftDocuments;
  private ArrayList<Document> frDocuments;
  private ArrayList<Document> laDocuments;
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";

  private final String DOC_NO = "docno";
  private final String TITLE = "title";
  private final String CONTENT = "content";

  //private ArrayList<Document> allLuceneDocuments;

  public Indexer() throws IOException {
    fbisDocuments = new ArrayList<>();
    ftDocuments = new ArrayList<>();
    frDocuments = new ArrayList<>();
    laDocuments = new ArrayList<>();

    // FBIS
    Fbis96Parser myFbisParser = new Fbis96Parser();
    myFbisParser.loadFiles();
    for (Fbis95Structure d : myFbisParser.getMyFbisContainer()) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getDocno(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      fbisDocuments.add(doc);
    }
    // Financial Times.
    for (FinancialTimesDocument d : FinancialTimesReader.readDocuments(FT_PATH)) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      ftDocuments.add(doc);
    }
    // Federal Register.
    for (FederalRegisterDocument d : FederalRegisterReader.readDocuments(FR_PATH)) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      frDocuments.add(doc);
    }
    // LA Times.
    for (LosAngelesTimesDocument d : LosAngelesTimesReader.readDocuments(LATIMES_PATH)) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      laDocuments.add(doc);
    }
  }
  public void indexAllDocuments(String indexDirectory, Analyzer analyzer, Similarity scorer) throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer, fbisDocuments);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer, ftDocuments);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer, frDocuments);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    executorService.submit(() -> {
      try {
        indexDocuments(indexDirectory, analyzer, scorer, laDocuments);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  }

  public void indexDocuments(String indexDirectory, Analyzer analyzer, Similarity scorer, List<Document> documents) throws IOException {
    String analyzerName = analyzer.getClass().getSimpleName();
    String scorerName = scorer.getClass().getSimpleName();
    System.out.println("Indexing documents with " + analyzerName + " and " + scorerName + "with");
    IndexWriter indexWriter = createWriter(indexDirectory, analyzer, scorer);
    indexWriter.addDocuments(documents);
    indexWriter.close();
    System.out.println("Indexed " + documents.size() + " documents with " + analyzerName + " and " + scorerName
            + " and stored to directory " + indexDirectory + ".");
  }
  /**
   * Creates an IndexWriter with the given analyzer and scorer.
   *
   * @param analyzer Analyzer to use for indexing.
   * @param scorer Similarity to use for indexing.
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