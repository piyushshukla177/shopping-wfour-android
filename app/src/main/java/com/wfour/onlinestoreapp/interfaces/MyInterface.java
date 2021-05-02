package com.wfour.onlinestoreapp.interfaces;

import com.wfour.onlinestoreapp.objects.ProductObj;

public interface MyInterface {
    void onClick(int position, ProductObj productObj);
    void deleteItem(int position);
    void onInCrease(double money, int position);
    void onDeCrease(double money, int position);
}
