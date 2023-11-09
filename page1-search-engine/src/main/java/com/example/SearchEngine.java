package com.example;

import com.example.ft.FinancialTimesReader;
import com.example.fbis_parser.*;
/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  public static final String FT_PATH = "./data/ft";

  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    FinancialTimesReader.readDocuments(FT_PATH);

    Fbis96Parser my_fbis_parser = new Fbis96Parser();
    my_fbis_parser.loadFiles();
    // Return a FBIS contain a class
    // DOCNO,title and text
    ArrayList<Fbis95Structure> my_FBIS_collection = my_fbis_parser.getMyFbisContainer();
  }
}
