package com.wfour.onlinestoreapp.widgets.tabLayout;

/**
 * Created by Suusoft on 11/03/2017.
 */

public class ItemTabRadius  {
    private int id, icon, img_bg;
    private String name;
    private boolean selected;


    public ItemTabRadius(int id, int icon, String name, boolean selected) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
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

    public int getImg_bg() {
        return img_bg;
    }

    public void setImg_bg(int img_bg) {
        this.img_bg = img_bg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
