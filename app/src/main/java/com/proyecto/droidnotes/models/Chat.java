package com.proyecto.droidnotes.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private long timestamp;
    private ArrayList<String>ids;

    // CONSTRUCTOR VACIO
    public Chat() {
    }

    //CONTRUCTOR CON LOS TRES CAMPOS
    public Chat(String id, long timestamp, ArrayList<String> ids) {
        this.id = id;
        this.timestamp = timestamp;
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}