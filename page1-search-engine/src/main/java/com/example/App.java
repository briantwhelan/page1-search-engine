package com.example;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException {
        FBIS96_parser my_fbis_parser = new FBIS96_parser();
        my_fbis_parser.load_file();



    }
}
