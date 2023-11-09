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

        // Debugging: Print the number of queries found
        System.out.println("Number of queries parsed: " + queries.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_PATH.toFile()))) {
            for (Query query : queries) {
                writer.write("Query Number: " + query.getNumber());
                writer.newLine();
                writer.write("Title: " + query.getTitle());
                writer.newLine();
                writer.write("Description: " + query.getDescription());
                writer.newLine();
                writer.write("Narrative: " + query.getNarrative());
                writer.newLine();
                writer.write("---------------------------------------------------");
                writer.newLine();

                // Debugging: Print each query's title to the console
                System.out.println("Writing query: " + query.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
