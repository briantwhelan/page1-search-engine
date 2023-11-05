package com.example;

public class FBIS_text_augment {
    private String input_string;


    public FBIS_text_augment(String input_string) {
        this.input_string = input_string;
    }

    public String augment_string()
    {
        String text_content = this.input_string;
        String text_content_new = text_content.replaceAll("&hyph;", "-");
        System.out.println(text_content_new);
        String start_pattern = "[Text]";
        int start_index = text_content_new.indexOf(start_pattern);
        String final_string_text = "";
        if( start_index !=-1)
        {
            String cut_string = text_content_new.substring(start_index);
            String remove_string_content = cut_string.replace(start_pattern, "");
            // remove remove -- string into a string
            String delimiter = "--";

            int delimiter_index = remove_string_content.indexOf(delimiter) + delimiter.length();
            final_string_text = remove_string_content.substring(delimiter_index).trim();
        }
        String square_braket_regex = "\\[.*?\\]";
        String remove_reg_string = text_content_new.replaceAll(square_braket_regex, "");
        // Get the substring from the delimiter to the end of the original string


        String remove_bn_string = remove_reg_string.replace("BFN", "");


        String delimiter = "--";

        int delimiter_index = remove_bn_string.indexOf(delimiter) + delimiter.length();

        String remov_delie = remove_bn_string.substring(delimiter_index).trim();
        final_string_text = remov_delie;


        System.out.println(final_string_text);

        return final_string_text;

    }
}
