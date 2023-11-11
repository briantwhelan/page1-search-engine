package com.example.queryCreation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessingFromTopicParser {

	public ProcessingFromTopicParser() {
	}

	public String[] stringRemovalNoiseAndToken(String inputString) {
		final String LARGE_STOPWORD_ADDRESS_173STOPWORDS = "./data/queryCreationdataset/173Stopwords.txt";

		// Stopwords list
		// https://gist.github.com/larsyencken/1440509
		ArrayList<String> myStopWordList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(LARGE_STOPWORD_ADDRESS_173STOPWORDS))) {
			String line;

			// Read each line from the text file and add it to the ArrayList
			while ((line = reader.readLine()) != null) {
				myStopWordList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] wordsInput = inputString.split("\\s+");
		StringBuilder builderForStopwordRemovaled = new StringBuilder();

		for (String word : wordsInput) {
			if (!myStopWordList.contains(word.toLowerCase())) {
				builderForStopwordRemovaled.append(word);
				builderForStopwordRemovaled.append(' ');
			}
		}

		String temp1String = builderForStopwordRemovaled.toString().trim();
		// Remove puncation
		String[] tokenizedKeywords = temp1String.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		// https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java

		return tokenizedKeywords;

	}


	public void run() {
		QueryParser my = new QueryParser();

		List<Query> local = my.parseQueries();
		ArrayList<String> OutputList = new ArrayList<>();
		for (int i = 0; i < local.size(); i++) {
			Query temp = local.get(i);
			String title = temp.getTitle();
			String nattive = temp.getNarrative();
			String desc = temp.getDescription();
			String finalString = title + nattive + desc;
			// System.out.println(finalString);
			String[] tokenizedKeywords = stringRemovalNoiseAndToken(finalString);
			List<String> tokenizedKeywordsList = Arrays.asList(tokenizedKeywords);
			TFIDFCalculator calculator = new TFIDFCalculator();
			// Compution the each one TFIDF score
			// we have score and term
			ArrayList<Double> scoreForTFIDF = new ArrayList<Double>();
			ArrayList<StructureForTDIDFStorage> myStorageStucture = new ArrayList<>();

			for (int indexIDF = 0; indexIDF < tokenizedKeywordsList.size(); indexIDF++) {
				String currentTerm = tokenizedKeywordsList.get(indexIDF);
				double currentTFIDFScire = calculator.tfIdf(tokenizedKeywordsList, currentTerm);
				scoreForTFIDF.add(currentTFIDFScire);
				StructureForTDIDFStorage tempS = new StructureForTDIDFStorage(currentTFIDFScire, currentTerm);
				myStorageStucture.add(tempS);

			}
			// Sortding in desecingd
			Collections.sort(scoreForTFIDF);
			Collections.reverse(scoreForTFIDF);

			ArrayList<Double> firstFiveScore = new ArrayList<>();
			// top 10
			for (int getIndex = 0; getIndex < 10; getIndex++) {
				firstFiveScore.add(scoreForTFIDF.get(getIndex));
			}
			//
			/*
			 * Remove unique value
			 * https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist
			 * -in-java
			 */
			ArrayList<Double> uniqueList = (ArrayList) firstFiveScore.stream().distinct().collect(Collectors.toList());
			// Comparere
			// myStorageStucture
			StringBuilder currentKeywordsBulder = new StringBuilder();
			ArrayList<String> finalKeyword = new ArrayList<>();
			for (int indexUnique = 0; indexUnique < uniqueList.size(); indexUnique++) {
				double valueT = uniqueList.get(indexUnique);
				for (int indexForStruoew = 0; indexForStruoew < myStorageStucture.size(); indexForStruoew++) {

					double doubelForStore = myStorageStucture.get(indexForStruoew).getTdidfScore();
					Double d1 = valueT;
					Double d2 = doubelForStore;
					if (Double.compare(d1, d2) == 0) {

						String currentKeywodsToAdd = myStorageStucture.get(indexForStruoew).getKeywordTerms();

						finalKeyword.add(currentKeywodsToAdd);

					}
				}
			}
			// unique
			/*
			 * Remove unique value
			 * https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist
			 * -in-java
			 */
			ArrayList<String> uniqueListKeywords = (ArrayList) finalKeyword.stream().distinct()
					.collect(Collectors.toList());
			// StringBuilder currentKeywordsBulder = new StringBuilder();
			for (int indexKeyowrdL = 0; indexKeyowrdL < uniqueListKeywords.size(); indexKeyowrdL++) {
				String keyworT = uniqueListKeywords.get(indexKeyowrdL);
				currentKeywordsBulder.append(keyworT);
				currentKeywordsBulder.append(" ");
			}
			String finalStringCurrent = currentKeywordsBulder.toString();
			System.out.println(finalStringCurrent);
			OutputList.add(finalStringCurrent.trim());



		}

		String filePath = "./data/queryfile/query.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : OutputList) {
				writer.write(line);
				writer.newLine();
			}
			System.out.println("The list has been written to " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
