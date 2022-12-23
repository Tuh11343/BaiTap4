package com.example.android_baitap4;

import java.io.Serializable;

public class Channel implements Serializable {

    private String type, id;

    public Channel(String type, String id)
    {
        this.type = type;
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
