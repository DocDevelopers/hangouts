package com.docdevelopers.hangouts;

public class Datum {
    
    String author;
    String content;

    public Datum(String author, String title) {
        this.author = author;
        this.content = title;
    }
    
    public String toString() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
    
    public String getContent() {
        return content;
    }

}
