package com.etomato.assignment.Main;

public class Model {

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    private String content;
    private String image;
    private int viewType;

    public Model(String content, String image , int viewType) {
        this.content = content;
        this.image = image;
        this.viewType = viewType;
    }
    public Model() {

    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public int getViewType() {
        return viewType;
    }
}
