package com.example.queryCreation;

public class StructureForTDIDFStorage {
  private double tdidfScore;
  private String keywordTerms;


  public StructureForTDIDFStorage(double tdidfScore, String keywordTerms) {
    this.tdidfScore = tdidfScore;
    this.keywordTerms = keywordTerms;
  }

  public double getTdidfScore() {
    return tdidfScore;
  }

  public String getKeywordTerms() {
    return keywordTerms;
  }
}
