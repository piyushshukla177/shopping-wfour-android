package com.wfour.onlinestoreapp.objects;

import java.util.ArrayList;

public class PropetiesObj {
    private String name;
    private ArrayList<ColorProduct> colors;
    //private ArrayList<SizeProduct> sizes;

    public PropetiesObj() {
    }

    public PropetiesObj(String name, ArrayList<ColorProduct> colors) {
        this.name = name;
        this.colors = colors;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ColorProduct> getColors() {
        return colors;
    }

    public void setColors(ArrayList<ColorProduct> colors) {
        this.colors = colors;
    }

//    public ArrayList<SizeProduct> getSizes() {
//        return sizes;
//    }
//
//    public void setSizes(ArrayList<SizeProduct> sizes) {
//        this.sizes = sizes;
//    }
}
