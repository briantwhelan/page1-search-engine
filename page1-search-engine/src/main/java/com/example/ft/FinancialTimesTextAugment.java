package com.example.ft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinancialTimesTextAugment {

  private String 	inputString_title;
  private String 	inputString_content;

  public FinancialTimesTextAugment(String inputString_title, String inputString_content) {
    this.inputString_title = inputString_title;
    this.inputString_content = inputString_content;
  }


  public String augment_title()
  {
    String textContent = this.inputString_title.toLowerCase().trim();

    String removalDash  = textContent.replaceAll("/", "");
    return removalDash;

  }
  public String augment_Content()
  {
    String textContent = this.inputString_content.toLowerCase().trim();
    // Rmove puncation

    // Remove all puncatation
    String removalPuncatation = textContent.replaceAll("\\p{Punct}", "").trim();

    // Remove ( ) and digit from the origianl string
    Pattern patternToMatch = Pattern.compile("[()\\d]");

    Matcher matcherString = patternToMatch.matcher(removalPuncatation);

    String stringRemoveCurlyBreacketAndDigit = matcherString.replaceAll("");

    // removal all space
    String removeMultiSpaceTwo = stringRemoveCurlyBreacketAndDigit.replaceAll("\\s+", " ");
    // Remove &
    String removalSyscialSymboOne = removeMultiSpaceTwo.replaceAll("&","");
    // Removeall ---

    String removalLine = removalSyscialSymboOne.replaceAll("-","");




    return removalLine;
  }
}
