package com.example.fbisparser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is to remove unnecessary information in a text string for FBIS.
 */
public class FbisTextAugment {
	private String 	inputString;

	public FbisTextAugment(String inputString) {
		this.inputString = inputString;
	}

	public String augmentString() {
		String textContent = this.inputString;
		// Replace &hyph; with a hyphen
		String textContentNew = textContent.replaceAll("&hyph;", "-");
		// Remove content between [ and ] except when it is [Text]
		String replacedString = textContentNew.replaceAll("\\[(?!Text\\]).*?\\]", "[]");

		String removeLeftSquareBracket = replacedString.replaceAll("\\[", "");
		String removeRightSquareBracket = removeLeftSquareBracket.replaceAll("\\]", "");
		String startPattern = "Text";
		String removeStringContent = removeRightSquareBracket.replace(startPattern, "");

		String removeDelimiter = removeStringContent.replaceAll("-", "");
		String removeTriangleTag = removeDelimiter.replaceAll("<[^>]*>", "");
		String removeLanguageTag = removeTriangleTag.replaceAll("Language: ", "");

		String removeBfnOne = removeLanguageTag.replaceAll("Article Type:BFN", "");

		// Change line remove
		String changeLineRemove = removeBfnOne.replaceAll("\n", "").trim();

		String removeMultiSpace = changeLineRemove.replaceAll("\\s+", " ");
		String removeBfnTwo = removeMultiSpace.replaceAll("BFN", "");

		// Remove ( ) and digit from the origianl string
		Pattern patternToMatch = Pattern.compile("[()\\d]");

		Matcher matcherString = patternToMatch.matcher(removeBfnTwo);

		String stringRemoveCurlyBreacketAndDigit = matcherString.replaceAll("");
		/*
		
		 */

		// Remove money symbol

		Pattern patternForMoneny = Pattern.compile("\\$");

		Matcher matcherforMoney = patternForMoneny.matcher(stringRemoveCurlyBreacketAndDigit);

		String removeMoneySymbolString = matcherforMoney.replaceAll("");

		// Remove |

		Pattern patternPipeCharater = Pattern.compile("\\|");

		Matcher matcherPipe = patternPipeCharater.matcher(removeMoneySymbolString);

		// Replace all occurrences of the pipe character with an empty string
		String pipeRemoval = matcherPipe.replaceAll("");

		// Remove comma
		Pattern patternComma = Pattern.compile(",");

		Matcher matcherComma = patternComma.matcher(pipeRemoval);

		String removealComma = matcherComma.replaceAll("");

		// Remove extra string aiagn

		String removeMultiSpaceAgain = removealComma.replaceAll("\\s+", " ");
		String finalStringText = removeMultiSpaceAgain;

		return finalStringText;
	}
}
