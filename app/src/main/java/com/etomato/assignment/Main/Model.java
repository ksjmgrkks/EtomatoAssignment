package com.etomato.assignment.Main;

public class Model {
    private String content;
    private String image;
    private int viewType;

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
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
