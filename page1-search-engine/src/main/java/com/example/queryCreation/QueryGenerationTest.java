package com.example.queryCreation;

import com.example.queryCreation.ProcessingFromTopicParser;
import java.util.ArrayList;

public class QueryGenerationTest {
  public static void main(String[] args) {
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    ArrayList<String> myQueryList = myqueryGeneration.getQueryList();


  }
}
