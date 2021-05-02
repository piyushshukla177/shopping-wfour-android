package com.wfour.onlinestoreapp.objects;

/**
 * Created by Suusoft on 11/02/2017.
 */

public class MenuLeft {

    private int id, icon;
    private String name;
    private boolean selected;

    public MenuLeft() {
    }

    public MenuLeft(int id, int icon, String name, boolean selected) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
