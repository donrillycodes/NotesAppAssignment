package com.adedeji.notesapp;

public class Note {
    private String id;
    private String title;
    private String content;
    private long timestamp;

    // Required empty constructor for Firebase
    public Note() {
        this.timestamp = System.currentTimeMillis();
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}