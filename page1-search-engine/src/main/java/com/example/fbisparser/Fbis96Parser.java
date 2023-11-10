package com.example.fbisparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses Foreign Broadcast Information Service documents from 1996. Usage:
 * 
 * <pre>
 * Fbis96Parser myFbisParser = new Fbis96Parser();
 * myFbisParser.loadFiles();
 * ArrayList<Fbis95Structure> myFbisCollection = myFbisParser.getMyFbisContainer();
 * </pre>
 * 
 * Each file contains multiple structures like:
 * 
 * <pre>
 * {@code
 * <DOC>
 *     <DOCNO> </DOCNO>
 *     <HT>  </HT>
 *     <HEADER>
 *         <H2>   </H2>
 *         <DATE1>  </DATE1>
 *         <H3> <TI>  </TI></H3>
 *     </HEADER>
 *     <TEXT>
 *     </TEXT>
 * </DOC>
 * }
 * </pre>
 * 
 * The useful information is contained within the <DOCNO>, <TEXT>, and <TI>
 * tags. The <TI> is nested within
 * <H3><TI></TI></H3>. Only the DOC number, Text, and TI are sufficient.
 */
@SuppressWarnings("CheckStyle")
public class Fbis96Parser {

	private ArrayList<String> fbisFileNameCollection;
	private ArrayList<Fbis95Structure> myFbisContainer;

	public Fbis96Parser() {
		fbisFileNameCollection = new ArrayList<>();
		myFbisContainer = new ArrayList<>();
	}

	public ArrayList<String> getFbisFileNameCollection() {
		return fbisFileNameCollection;
	}

	public ArrayList<Fbis95Structure> getMyFbisContainer() {
		return myFbisContainer;
	}

	/**
	 * Loads the list of file names from the specified folder.
	 */
	public void folderLoader() {
		String folderPath = "./data/fbis/";
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		if (folder.exists() && folder.isDirectory()) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					fbisFileNameCollection.add(file.getName());
				}
			}
		} else {
			System.out.println("Folder does not exist.");
		}

		// Remove readme files
		fbisFileNameCollection.remove("readmefb.txt");
		fbisFileNameCollection.remove("readchg.txt");
	}

	/**
	 * Loads files and parses the content into structures.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void loadFiles() throws IOException {
		folderLoader();

		for (String eachFile : fbisFileNameCollection) {
			String folderPath = "./data/fbis";
			String newAddress = folderPath + "/" + eachFile;
			File currentFile = new File(newAddress);
			Document doc = Jsoup.parse(currentFile, "UTF-8");

			Elements elementsList = doc.getElementsByTag("DOC");

			for (Element singleElement : elementsList) {
				String docnoContent = singleElement.getElementsByTag("DOCNO").text().trim();
				String titleContent = singleElement.getElementsByTag("TI").text().trim();
				Element textElement = singleElement.getElementsByTag("TEXT").first();
				String textContent = textElement != null ? textElement.text().trim() : "";

				FbisTextAugment myTextAugment = new FbisTextAugment(textContent);
				String finalStringText = myTextAugment.augmentString();
				System.out.println("________________");
				// System.out.println(finalStringText);

				Fbis95Structure tempStructure = new Fbis95Structure(docnoContent, titleContent, finalStringText);
				myFbisContainer.add(tempStructure);
			}
		}
	}
}
