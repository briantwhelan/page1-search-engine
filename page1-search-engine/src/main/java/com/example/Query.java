package com.example;


public class QueryObject {

    private String queryNum;
    private String queryId;
    private String title;
    private String description;
    private String narrative;

    QueryObject(){
        this.queryNum = "";
        this.queryId = "";
        this.title = "";
        this.narrative = "";
        this.description = "";
    }

    public String getTitle() { return title; }

    public String getNarrative() { return narrative; }

    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }

    public void setNarrative(String narrative) { this.narrative = narrative; }

    public void setQueryId(String queryId) { this.queryId = queryId; }

    public void setQueryNum(String queryNum) { this.queryNum = queryNum; }

    public void setDescription(String description) { this.description = description; }
}