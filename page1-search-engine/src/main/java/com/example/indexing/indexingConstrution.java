package com.example.indexing;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.example.fbisparser.Fbis95Structure;
import com.example.fbisparser.Fbis96Parser;
import com.example.fr.FederalRegisterDocument;
import com.example.fr.FederalRegisterReader;
import com.example.fr.FederalRegisterTextAugment;
import com.example.ft.FinancialTimesDocument;
import com.example.ft.FinancialTimesReader;
import com.example.ft.FinancialTimesTextAugment;
import com.example.latimes.LosAngelesTimesDocument;
import com.example.latimes.LosAngelesTimesReader;
import com.example.latimes.LosAngelesTimesTextAugment;

public class indexingConstrution {

	public static final String FT_PATH = "./data/ft";
	public static final String LATIMES_PATH = "./data/latimes";
	public static final String FR_PATH = "./data/fr94";
	public static final String DIRTORY_ADDRESS = "./dir";
	private final String DOC_NUMBE_MARCO = "docno";
	private final String TITLE_MARCO = "title";
	private final String CONTENT_MARCO = "content";
	private ArrayList<Document> LuenceAllDocuemnt;
	public indexingConstrution() throws IOException {
		this.LuenceAllDocuemnt = new ArrayList<>();

		this.createDoucmentSameFieldForAllCorpus();
		// construcntion indexing
		// this.constructIndex();

	}

	public ArrayList<Document> getLuenceAllDocuemnt() {
		return LuenceAllDocuemnt;
	}

	public void createDoucmentSameFieldForAllCorpus() throws IOException {

		System.out.println("Start loading FBIS");
		// 1 FBIS
		Fbis96Parser myFbisParser = new Fbis96Parser();
		myFbisParser.loadFiles();
		ArrayList<Fbis95Structure> myFbisCollection = myFbisParser.getMyFbisContainer();

		for (int indexi = 0; indexi < myFbisCollection.size(); indexi++) {
			Fbis95Structure currentDocument = myFbisCollection.get(indexi);

			String tempDOC = currentDocument.getDocno();
			String tempTitle = currentDocument.getTitle().toLowerCase();
			String tempContent = currentDocument.getTitle().toLowerCase();
			//
			Document temPDocuement = new Document();
			// DOCNO not cut
			temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
			temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
			temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));

			this.LuenceAllDocuemnt.add(temPDocuement);
		}
		// 2 FINAL time
		ArrayList<FinancialTimesDocument> myfinalTime = FinancialTimesReader.readDocuments(FT_PATH);
		for (int indexi = 0; indexi < myfinalTime.size(); indexi++) {

			FinancialTimesDocument currentDocument = myfinalTime.get(indexi);

			String tempDOC = currentDocument.getNumber();
			String tempTitle = currentDocument.getHeadline().toLowerCase();
			String tempContent = currentDocument.getText().toLowerCase().trim();

			FinancialTimesTextAugment myFinalTimeAugment = new FinancialTimesTextAugment(tempTitle, tempContent);

			Document temPDocuement = new Document();
			temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
			temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
			temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));
			this.LuenceAllDocuemnt.add(temPDocuement);

		}
		// 3 FR
		ArrayList<FederalRegisterDocument> myFR = FederalRegisterReader.readDocuments(FR_PATH);

		for (int indexi = 0; indexi < myFR.size(); indexi++) {

			FederalRegisterDocument currentDocument = myFR.get(indexi);

			String tempDOC = currentDocument.getNumber();
			String tempTitle = currentDocument.getTitle().toLowerCase();
			String tempContent = currentDocument.getText();

			FederalRegisterTextAugment myFRAugment = new FederalRegisterTextAugment(tempContent);
			String frAugment = myFRAugment.augmentString().trim();

			Document temPDocuement = new Document();
			temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
			temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
			temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));
			this.LuenceAllDocuemnt.add(temPDocuement);

		}
		// 4 LosAngelesTimesReader.readDocuments(LATIMES_PATH);

		// CONTENT merge graphic and text

		ArrayList<LosAngelesTimesDocument> myLST = LosAngelesTimesReader.readDocuments(LATIMES_PATH);
		for (int indexi = 0; indexi < myLST.size(); indexi++) {

			LosAngelesTimesDocument currentDocument = myLST.get(indexi);

			String tempDOC = currentDocument.getNumber();
			String tempTitle = currentDocument.getHeadline();
			String tempContent = currentDocument.getText();
			String graphicContent = currentDocument.getGraphic().trim().toLowerCase();
			String newMergeContent = tempContent + graphicContent;
			LosAngelesTimesTextAugment myLAaugment = new LosAngelesTimesTextAugment(tempTitle, newMergeContent);

			String augmentTITLE = myLAaugment.augment_title();
			String augmentContent = myLAaugment.augment_Content();

			Document temPDocuement = new Document();
			temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
			temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
			temPDocuement.add(new TextField(CONTENT_MARCO, newMergeContent, Field.Store.YES));
			this.LuenceAllDocuemnt.add(temPDocuement);

		}
		System.out.println("FInishing LOADING documents into memory");

	}

}
