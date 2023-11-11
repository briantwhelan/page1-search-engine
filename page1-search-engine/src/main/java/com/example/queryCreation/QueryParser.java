package com.example.queryCreation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    private enum Tags {
        BEGIN("<top>"),
        END("</top>"),
        NUMBER("<num> Number:"),
        TITLE("<title>"),
        DESCRIPTION("<desc> Description:"),
        NARRATIVE("<narr> Narrative:");

        private final String tag;

        Tags(final String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }

    private final static Path DATA_PATH = Paths.get("./data/queryCreationdataset/topics.txt");

    public List<Query> parseQueries() {
        List<Query> queries = new ArrayList<>();
        Path filePath = DATA_PATH;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            Query currentQuery = null; // Start with null to indicate no active query
            String line;
            Tags currentTag = null;

            while ((line = reader.readLine()) != null) {
                Tags foundTag = findTag(line);

                if (foundTag != null) {
                    if (foundTag == Tags.BEGIN) {
                        // Handle previous query object if it exists.
                        if (currentQuery != null) {
                            queries.add(currentQuery);
                        }
                        // Create a new Query object for the next query block.
                        currentQuery = new Query();
                    } else {
                        // If the current tag is NUMBER or TITLE, we should fill the fields right away.
                        if (foundTag == Tags.NUMBER || foundTag == Tags.TITLE) {
                            fillQueryFields(foundTag, line, currentQuery);
                        }
                        // Set the current tag for DESCRIPTION and NARRATIVE, which span multiple lines.
                        currentTag = foundTag;
                    }
                } else {
                    // Continue adding content for multiline fields (DESCRIPTION, NARRATIVE).
                    if (currentTag != null && currentQuery != null) {
                        fillQueryFields(currentTag, line, currentQuery);
                    }
                }

                // Check for the end of a query block.
                if (foundTag == Tags.END) {
                    // Add the completed query object to the list.
                    if (currentQuery != null) {
                        queries.add(currentQuery);
                        currentQuery = null; // Reset for the next query block
                    }
                    currentTag = null; // Reset the tag as well.
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }

    private Tags findTag(String line) {
        for (Tags tag : Tags.values()) {
            if (line.startsWith(tag.getTag())) {
                return tag;
            }
        }
        return null;
    }

    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)$");

    private void fillQueryFields(Tags tag, String line, Query query) {
        // No need for a check; the tag presence is already determined before this method is called.
        String content;
        switch (tag) {
            case NUMBER:
                content = line.replace(Tags.NUMBER.getTag(), "").trim();
                query.setNumber(content);
                break;
            case TITLE:
                // Use regex matcher to find title content after the tag
                Matcher titleMatcher = TITLE_PATTERN.matcher(line);
                if (titleMatcher.find()) {
                    content = titleMatcher.group(1).trim();
                    query.setTitle(content);
                }
                break;
            case DESCRIPTION:
                content = query.getDescription() != null ? query.getDescription() : "";
                query.setDescription(content + " " + line.trim());
                break;
            case NARRATIVE:
                content = query.getNarrative() != null ? query.getNarrative() : "";
                query.setNarrative(content + " " + line.trim());
                break;
            default:
                break;
        }
    }

}
