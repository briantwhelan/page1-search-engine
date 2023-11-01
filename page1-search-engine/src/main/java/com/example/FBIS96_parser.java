package com.example;

import org.apache.lucene.document.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * This class is to Foreign Broadcast Information Service (1996)
 *
 * Struture of this file and each file contain multiple of this structure
 * <DOC>
 * 	<DOCNO> </DOCNO>
 * 	<HT>  </HT>
 * 	<HEADER>
 * 	<H2>   </H2>
 * 	<DATE1>  </DATE1>
 * 	<H3> <TI>  </TI></H3>
 * 	</HEADER>
 * 	<TEXT>
 * </TEXT>
 * </DOC>
 *
 *
 * Useful information : <DOCNO>  <TEXT> <TI>
 *
 *     <TI> is nested within <H3><TI>
 *
 *         </TI></H3>
 *
 *
 *         DOC number
 *
 *         Text
 *
 *         TI
 *         These three are suffificent
 */
public class FBIS96_parser {

    private ArrayList<Document>
}
