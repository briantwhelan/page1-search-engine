package com.example;



/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  private static final String INDEX_DIRECTORY = "./index";

  // Analyzers to use.
  private static final String[] ANALYZERS = {
          "EnglishAnalyzer"  // "StandardAnalyzer", "WhitespaceAnalyzer","SimpleAnalyzer"
  };
  private static final String[] SCORER = {
          "LMDirichletSimilarity", "BM25_ClassicSimilarity" , "Classic_LMDirichletSimilarity", "BM25_LMDirichletSimilarity"//,"BM25Similarity", "ClassicSimilarity", "BooleanSimilarity",
  };
  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    Indexer indexer = new Indexer();
    try {
      for (String analyzerType : ANALYZERS) {
        for (String scorer : SCORER) {
          indexer.indexDocuments(INDEX_DIRECTORY, analyzerType, scorer);
          Querier querier = new Querier("./data/topics/topics.txt");
          querier.queryIndex(INDEX_DIRECTORY, analyzerType, scorer);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}

