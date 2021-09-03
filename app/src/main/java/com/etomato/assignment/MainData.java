package com.etomato.assignment;

import com.google.gson.annotations.SerializedName;

public class MainData
{
    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}