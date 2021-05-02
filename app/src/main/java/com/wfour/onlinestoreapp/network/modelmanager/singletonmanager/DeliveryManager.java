package com.wfour.onlinestoreapp.network.modelmanager.singletonmanager;

import com.wfour.onlinestoreapp.objects.DeliveryObj;

import java.util.ArrayList;

public class DeliveryManager {
    private static DeliveryManager mInstance;
    public ArrayList<DeliveryObj> deliveryObjs = new ArrayList();

    private DeliveryManager() {
    }
    public static DeliveryManager getInstance() {
        if (mInstance == null) {
            {
                mInstance = new DeliveryManager();
            }
        }
        return mInstance;
    }

    public ArrayList<DeliveryObj> getArray(){
        return this.deliveryObjs;
    }
    public void removeItem(int position){
        deliveryObjs.remove(position);
    }
    public void addItem(DeliveryObj deliveryObj){
        deliveryObjs.add(deliveryObj);
    }
}
