package com.example.android_baitap4;

import java.io.Serializable;
import java.util.ArrayList;

public class Official implements Serializable {

    private String name, title;
    private String address;
    private String party;
    private String phone;
    private String urls;
    private String email;
    private String photoURL;
    private ArrayList<Channel> channels;

    public Official()
    {
        name = "";
        title = "";
        address = "";
        party = "";
        phone = "";
        urls = "";
        email = "";
        photoURL = "";
        channels = new ArrayList<>();
    }

    public Official(String name, String title, String address, String party, String phones, String urls, String emails, String photoURL, ArrayList<Channel> channels) {
        this.name = name;
        this.title = title;
        this.address = address;
        this.party = party;
        this.phone = phones;
        this.urls = urls;
        this.email = emails;
        this.photoURL = photoURL;
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", party='" + party + '\'' +
                ", phones='" + phone + '\'' +
                ", urls='" + urls + '\'' +
                ", emails='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", channels=" + channels +
                '}';
    }
}
