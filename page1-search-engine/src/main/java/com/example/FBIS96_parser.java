package com.example;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

   // private ArrayList<Document>


    public ArrayList<String> getFbis_file_name_collectio() {
        return fbis_file_name_collection;
    }

    private ArrayList<String>fbis_file_name_collection;

    public FBIS96_parser() {

        this.fbis_file_name_collection = new ArrayList<>();
    }


    public void folder_loader()
    {

        String folder_path = "./Assignment Two/fbis/";


        File folder = new File(folder_path);


        File[] listOfFiles = folder.listFiles();



        if (folder.exists() && folder.isDirectory()) {

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    this.fbis_file_name_collection.add(file.getName());
                }
            }
        } else {
            System.out.println("Not existed");
        }

        // Remove readMe file
        this.fbis_file_name_collection.remove("readmefb.txt");
        this.fbis_file_name_collection.remove("readchg.txt");



    }


    /**
     * @ input
     * @ output
     * @ throws IOException
     */
    public void load_file() throws IOException {


        // Loading folder


        folder_loader();
        // Load file


        for (String each_file:this.fbis_file_name_collection)
        {
            String folder_path = "./Assignment Two/fbis/";
            String new_address = folder_path+"/"+each_file;
            File cuurent_file = new File(new_address);
            org.jsoup.nodes.Document doc = Jsoup.parse(cuurent_file);

            /**
             * public class Elements
             * extends java.util.ArrayList<org.jsoup.nodes.Element>
             */
            // Parase DOC strcgure
            Elements elements_list = doc.getElementsByTag("DOC");
            ArrayList<FBIS95_structure>my_fbis_container = new ArrayList<>();

            for(Element single_element : elements_list)
            {
                System.out.println("docno");
                Elements doc_no_el = doc.select("DOCNO");
                String docno_content = doc_no_el.first().text();


                System.out.println(docno_content);
                System.out.println("TITLE");
                Elements title_el  = doc.getElementsByTag("TI");
                String title_content = title_el.first().text();
                System.out.println(title_content);

                System.out.println("text");
                Elements text_el = doc.select("TEXT");
                String text_content = text_el.first().text();
                FBIS_text_augment my_text_augment = new FBIS_text_augment(text_content);
                // Augment and remove unnessary string such as[ ]
                String final_string_text = my_text_augment.augment_string();

                System.out.println(final_string_text);



                FBIS95_structure temp_structure = new FBIS95_structure(docno_content,title_content,final_string_text);

                my_fbis_container.add(temp_structure);



                System.out.println("----------------");

            }
        }



    }
}
