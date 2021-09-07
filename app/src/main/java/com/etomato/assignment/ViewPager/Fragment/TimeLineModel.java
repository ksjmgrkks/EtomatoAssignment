package com.etomato.assignment.ViewPager.Fragment;

public class TimeLineModel {

    public TimeLineModel() {
    }
    
    public TimeLineModel(String date, String title, String contents) {
        this.date = date;
        this.title = title;
        this.contents = contents;
    }

    private String date;
    private String title;
    private String contents;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = "날짜"+date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = "제목"+title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = "내용"+contents;
    }


}
