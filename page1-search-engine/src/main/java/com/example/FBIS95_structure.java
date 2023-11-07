package com.example;

import java.util.Objects;

public class FBIS95_structure {


    public String getDocno() {
        return docno;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    private  String docno;



    private  String title;
    private String text;


    public FBIS95_structure(String docno, String title, String text) {
        this.docno = docno;
        this.title = title;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FBIS95_structure that = (FBIS95_structure) o;
        boolean condition = (Objects.equals(docno, that.docno) && Objects.equals(title, that.title)  && Objects.equals(text, that.text));
        return condition;
    }

    @Override
    public String toString() {
        String output = "Current Doc :" +this.docno +"\n"+" title :" +this.title +"\n"+"text :"+this.text;
        return output;
    }


    @Override
    public int hashCode() {
        return Objects.hash(docno, title, text);
    }
}
