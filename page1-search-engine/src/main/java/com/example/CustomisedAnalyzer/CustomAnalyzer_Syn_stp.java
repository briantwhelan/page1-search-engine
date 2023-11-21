package com.example.CustomisedAnalyzer;

import  java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.tartarus.snowball.ext.EnglishStemmer;
/*

This analyzer is from this work.
https://github.com/search?q=cs7is3&type=repositories&p=3
 */
public class CustomAnalyzer_Syn_stp extends StopwordAnalyzerBase{
  private BufferedReader countries;

  private BufferedReader drugReader;
  private BufferedReader dieaseReader;

  public CustomAnalyzer_Syn_stp(){
    super(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
  }

  @Override
  protected TokenStreamComponents createComponents(String s) {
    //https://livebook.manning.com/book/lucene-in-action-second-edition/chapter-4/76
    final Tokenizer tokenizer = new StandardTokenizer();
    TokenStream tokenStream = new ClassicFilter(tokenizer);
    tokenStream = new LowerCaseFilter(tokenStream);
    tokenStream = new TrimFilter(tokenStream);
    tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(generateStopWordList(),true));
    tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());
    tokenStream = new FlattenGraphFilter(new SynonymGraphFilter(tokenStream, generateSynonymMap(), true));
    tokenStream = new FlattenGraphFilter(new WordDelimiterGraphFilter(tokenStream, WordDelimiterGraphFilter.SPLIT_ON_NUMERICS |
        WordDelimiterGraphFilter.GENERATE_WORD_PARTS |
        WordDelimiterGraphFilter.GENERATE_NUMBER_PARTS |
        WordDelimiterGraphFilter.PRESERVE_ORIGINAL , null));
    return new TokenStreamComponents(tokenizer, tokenStream);
  }

  /**
   * Generate Synonym Map for country name in text
   * @return Synonym Map
   */
  private SynonymMap generateSynonymMap( ) {
    SynonymMap synMap = new SynonymMap(null, null, 0);


    try {
      //Create FSTSynonymMap to use country in text
      final SynonymMap.Builder builder = new SynonymMap.Builder(true);
// Country
      countries = new BufferedReader(new FileReader("world_countries.txt"));
      String curCountry = countries.readLine();
      while(curCountry != null) {
        builder.add(new CharsRef("country"), new CharsRef(curCountry), true);
        builder.add(new CharsRef("countries"), new CharsRef(curCountry), true);
        curCountry = countries.readLine();
      }
      synMap = builder.build();
    } catch (Exception e) {
      System.out.println(String.format("ERROR: " + e.getLocalizedMessage() + " happened while creating synonym map"));
    }
    // Drug
    // https://www.merriam-webster.com/thesaurus/drug
    // https://www.thesaurus.com/browse/disease

    try {
      //Create FSTSynonymMap to use country in text
      final SynonymMap.Builder builder = new SynonymMap.Builder(true);
//"drugSynnous.txt"
      drugReader = new BufferedReader(new FileReader("drugSynnous.txt"));
      // new BufferedReader(new FileReader("world_countries.txt"));
      String curDrug= drugReader.readLine();
      while(curDrug != null) {
        builder.add(new CharsRef("drug"), new CharsRef(curDrug.toLowerCase()), true);

        curDrug = drugReader.readLine();
      }
      synMap = builder.build();
    } catch (Exception e) {
      System.out.println(String.format("ERROR: " + e.getLocalizedMessage() + " happened while creating synonym map"));
    }
    // Diease
    try {
      //Create FSTSynonymMap to use country in text
      final SynonymMap.Builder builder = new SynonymMap.Builder(true);
//"drugSynnous.txt"
      dieaseReader = new BufferedReader(new FileReader("diseaseSynomous.txt"));
      // new BufferedReader(new FileReader("world_countries.txt"));
      String currDiease= dieaseReader.readLine();
      while(currDiease != null) {
        builder.add(new CharsRef("disease"), new CharsRef(currDiease.toLowerCase()), true);

        currDiease = dieaseReader.readLine();
      }
      synMap = builder.build();
    } catch (Exception e) {
      System.out.println(String.format("ERROR: " + e.getLocalizedMessage() + " happened while creating synonym map"));
    }


    return synMap;
  }

  /**
   * Generate StopWord List for most commonly used words
   * @return stopWord List
   */
  private List<String> generateStopWordList()
  {
    ArrayList<String> stopWordList = new ArrayList();
    try {
      BufferedReader words = new BufferedReader(new FileReader("common_word_list.txt"));
      String word = words.readLine();
      while(word != null) {
        stopWordList.add(word);
        word = words.readLine();
      }
    } catch (Exception e) {
      System.out.println(String.format("ERROR: " + e.getLocalizedMessage() + "happened while creating stopword list"));
    }
    return stopWordList;
  }
}
