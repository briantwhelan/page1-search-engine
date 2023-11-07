package com.example;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Test
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException {
        FBIS96_parser my_fbis_parser = new FBIS96_parser();
        my_fbis_parser.load_file();
        // Return a FBIS contain a class
        // DOCNO,title and text
        ArrayList<FBIS95_structure>my_FBIS_collection = my_fbis_parser.getMy_fbis_container();


    }
}
