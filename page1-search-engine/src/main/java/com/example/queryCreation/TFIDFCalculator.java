package com.example.queryCreation;
//https://gist.github.com/guenodz/d5add59b31114a3a3c66#file-tfidfcalculator-java-L8
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// modify from the this code
/**
 * @author Mohamed Guendouz
 */
public class TFIDFCalculator {


  public TFIDFCalculator() {
  }


  public double tf(List<String> doc, String term) {
    double result = 0;
    for (String word : doc) {
      if (term.equalsIgnoreCase(word))
        result++;
    }
    return result / doc.size();
  }


  public double idf(List<String> docs, String term) {
    double n = 0;

      for (String word : docs) {
        if (term.equalsIgnoreCase(word)) {
          n++;
          break;
        }
      }

    return Math.log(docs.size() / n);
  }
  public void printTFMatrix(double matrix[][])
  {
    System.out.println("TF-IDF Matrix:");
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        System.out.printf("%.4f ", matrix[i][j]);
      }
      System.out.println();
    }
  }


  public  double tfIdf(List<String> doc, String term) {
    double td_value = tf(doc, term);
    double idfV = idf(doc, term);
    return td_value* idfV;

  }
  public   double[][] constructTFIDFMatrix(List<String> keywordsList ) {

   int sizeOfkeywordsList = keywordsList.size();
   double tfMaatrxi[][] = new double[sizeOfkeywordsList][sizeOfkeywordsList];
// Word
   for(int i =0;i<sizeOfkeywordsList;i++ )
   {
     // Vocab
     for(int j =0;j<sizeOfkeywordsList;j++ )
     {
        String firstIndexITerm = keywordsList.get(i);
       String secondIndexJTerm = keywordsList.get(j);
       if(firstIndexITerm.equals(secondIndexJTerm))
       {
          double tfIDFValue = tfIdf(keywordsList,firstIndexITerm);

          tfMaatrxi[i][j]=tfIDFValue;

       }
       else{
         tfMaatrxi[i][j]=0.0;
       }
     }
   }

    return tfMaatrxi;


  }





}

