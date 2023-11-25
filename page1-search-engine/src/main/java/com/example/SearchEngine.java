package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import com.example.CustomisedAnalyzer.CustomAnalyzer_Syn_stp;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  private static final String INDEX_DIRECTORY = "./index";

  // Analyzers to use.
  private static final Analyzer[] analyzers = {
          new StandardAnalyzer(), // splits tokens at punctuation, whitespace and lowercases.
          new WhitespaceAnalyzer(), // splits tokens at whitespace.
          new EnglishAnalyzer(), // splits tokens punctuation, whitespace, lowercases
          // and removes English stop words.
          new SimpleAnalyzer(), // splits tokens at non-alphanumeric characters and lowercases.
          new  CustomAnalyzer_Syn_stp(),
  };

  // Scorers to use.
  private static final Similarity[] scorers = {
          new ClassicSimilarity(),
          new BM25Similarity(),
          new LMDirichletSimilarity(),
          new LMJelinekMercerSimilarity(0.7f),
  };
  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    Indexer indexer = new Indexer();
    // Use all analyzer-scorer combinations.
    for (Analyzer analyzer : analyzers) {
      for (Similarity scorer : scorers) {
        indexer.indexAllDocuments(INDEX_DIRECTORY, analyzer, scorer);
        Querier querier = new Querier("./page1-search-engine/data/topics/topics.txt");
        querier.queryIndex(INDEX_DIRECTORY, analyzer, scorer);
      }
    }
  }
}