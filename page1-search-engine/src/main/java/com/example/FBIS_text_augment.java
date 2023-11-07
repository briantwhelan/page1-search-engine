package com.example;

/**
 * This class is to remove unnecessary information in text string for FBIS
 */
public class FBIS_text_augment {
    private String input_string;


    public FBIS_text_augment(String input_string) {
        this.input_string = input_string;
    }

    public String augment_string()
    {
        String text_content = this.input_string;
        // Exsit bug here
        String text_content_new = text_content.replaceAll("&hyph;", "-");
        // Remove content between [ and ] except when it is [Text]
        // Some [Text]
        String replacedString = text_content_new.replaceAll("\\[(?!Text\\]).*?\\]", "[]");

        String remove_left_square_bracket = replacedString.replaceAll("\\[","");
        String remove_right_square_bracket = remove_left_square_bracket.replaceAll("\\]","");
        String start_pattern = "Text";
        String remove_string_content = remove_right_square_bracket.replace(start_pattern, "");

        String remove_delimiter = remove_string_content.replaceAll("-","");
        String remove_tringle_tag = remove_delimiter.replaceAll("<[^>]*>", "");
        String remove_language_tage = remove_tringle_tag.replaceAll("Language: ","");
        //Article Type:BFN

        String remove_bfn_one = remove_language_tage.replaceAll("Article Type:BFN","");

        //Chaneg line remove
        String change_line_remove =remove_bfn_one.replaceAll("\n", "").trim();

        String remove_mulit_space = change_line_remove.replaceAll("\\s+", " ");
        String remove_bfn_two = remove_mulit_space.replaceAll("BFN","");



        String final_string_text = remove_bfn_two;


       // System.out.println(final_string_text);

        return final_string_text;

    }
}
