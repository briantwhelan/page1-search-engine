package com.example;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.search.IndexSearcher;
/**
 *
 */

/**
 * Test
 *https://wordnet.princeton.edu/download
 */
public class App 
{

    public static void main( String[] args ) throws IOException {
        Fbis96Parser my_fbis_parser = new Fbis96Parser();
        my_fbis_parser.loadFiles();
        // Return a FBIS contain a class
        // DOCNO,title and text
        ArrayList<Fbis95Structure>my_FBIS_collection = my_fbis_parser.getMyFbisContainer();


    }
}
