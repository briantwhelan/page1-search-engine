package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {

    private static final Path OUTPUT_PATH = Paths.get("output.txt");

    public static void main(String[] args) {
        QueryParser queryParser = new QueryParser();
        List<Query> queries = queryParser.parseQueries();

      
        }
    }
}
