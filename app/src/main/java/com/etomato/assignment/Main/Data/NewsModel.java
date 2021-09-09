package com.etomato.assignment.Main.Data;

public class NewsModel {
    private String content;
    private String image;
    private String Link;
    private int viewType;

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

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
