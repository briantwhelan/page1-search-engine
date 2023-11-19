package com.example.fr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FederalRegisterTextAugment {

  private String 	inputString;

  public FederalRegisterTextAugment(String inputString) {
    this.inputString = inputString;
  }
  /*

  <!-- PJG ITAG l=90 g=1 f=1 --> removel

  remove \n

  remove & puncation

  extract <USDEPT>DEPARTMENT OF AGRICULTURE</USDEPT> content for the tag
   */

  public String augmentString() {

    String textContent = this.inputString.toLowerCase();
    // Replace &hyph; with a hyphen
    String textContentNew = textContent.replaceAll("&hyph;", "-");


    // Change line remove
    String changeLineRemove = textContentNew.replaceAll("\n", "").trim();

    String removeMultiSpace = changeLineRemove.replaceAll("\\s+", " ");

    //
    // Remove ( ) and digit from the origianl string
    Pattern patternToMatch = Pattern.compile("[()\\d]");

    Matcher matcherString = patternToMatch.matcher(removeMultiSpace);

    String stringRemoveCurlyBreacketAndDigit = matcherString.replaceAll("");

    // Remove all puncatation
    String removalPuncatation = stringRemoveCurlyBreacketAndDigit.replaceAll("\\p{Punct}", "").trim();

    // removall all the space

    String removeMultiSpaceTwo = removalPuncatation.replaceAll("\\s+", " ");
// remove all "␣"

    String removalAllSpecialSymbol = removeMultiSpaceTwo.replaceAll("␣", "");

    return  removalAllSpecialSymbol;
  }
}
