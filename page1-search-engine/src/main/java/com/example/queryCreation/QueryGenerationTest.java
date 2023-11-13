package com.example.queryCreation;

import com.example.queryCreation.ProcessingFromTopicParser;
import java.io.IOException;
import java.util.ArrayList;

public class QueryGenerationTest {
  public static void main(String[] args) throws IOException {
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    ArrayList<String> myQueryList = myqueryGeneration.getQueryList();


  }
}
