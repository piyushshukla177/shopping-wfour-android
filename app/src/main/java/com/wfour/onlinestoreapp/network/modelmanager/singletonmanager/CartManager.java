package com.wfour.onlinestoreapp.network.modelmanager.singletonmanager;

import android.util.Log;

import com.wfour.onlinestoreapp.objects.CartObj;

import java.util.ArrayList;

public class CartManager {
    private static CartManager mInstance;
    public ArrayList<CartObj> productList = new ArrayList();

    private CartManager() {
    }
    public static CartManager getInstance() {
        if (mInstance == null) {
            {
                mInstance = new CartManager();
            }
        }
        return mInstance;
    }

    public void removeItem(int position){
        productList.remove(position);
    }
    public ArrayList<CartObj> getArray(){
        return this.productList;
    }
    public void addItem(CartObj productObj){
        productList.add(productObj);
    }
    public double  getTotal(){
        double total = 0;
        if(productList.size()!= 0) {
            for (int i = 0; i < productList.size(); i++) {
               if(productList.get(i).getIs_point()==1){
                   total+=0;
               }
               else {
                   total +=productList.get(i).getTotalMoney();
               }

            }
        }
            return Math.round(total*100)/100.0d;
    }
    public boolean  isPay(){
        boolean total = false;
        boolean total2 = false;
        if(productList.size()!= 0) {
            for (int i = 0; i < productList.size(); i++) {
                Log.e("CartManager", "isPay: "+productList.get(i).getIs_point() );
                if(productList.get(i).getIs_point()==1){
                    total=true;
                }
                if(productList.get(i).getIs_point()==0){
                    total2=true;
                }


            }
        }
        if (total&&total2){
            return true;
        }
        else {
            return false;
        }

    }
    public double  getTotalPoint(){
        double total = 0;
        if(productList.size()!= 0) {
            for (int i = 0; i < productList.size(); i++) {
                if(productList.get(i).getIs_point()==1){
                    total +=productList.get(i).getTotalMoney();
                }


            }
        }
        return Math.round(total*100)/100.0d;
    }
}
