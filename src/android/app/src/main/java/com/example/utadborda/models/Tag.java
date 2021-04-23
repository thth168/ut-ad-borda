package com.example.utadborda.models;

public class Tag {

    private String TagName;
    private String TagId;


    public String getTagId() {
        return TagId;
    }

    public void setTagId(String tagId) {
        TagId = tagId;
    }

    public Tag(String tag, String id) {
        TagName = tag;
        TagId = id;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTag(String tag) {
        TagName = tag;
    }
}
