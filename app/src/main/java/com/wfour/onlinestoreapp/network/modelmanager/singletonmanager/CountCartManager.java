package com.wfour.onlinestoreapp.network.modelmanager.singletonmanager;

import com.wfour.onlinestoreapp.view.activities.CountObj;

import java.util.ArrayList;

public class CountCartManager {
    private static CountCartManager mInstance;
    public ArrayList<CountObj> countObjs = new ArrayList();

    private CountCartManager() {
    }
    public static CountCartManager getInstance() {
        if (mInstance == null) {
            {
                mInstance = new CountCartManager();
            }
        }
        return mInstance;
    }

    public ArrayList<CountObj> getArray(){
        return this.countObjs;
    }
    public void removeItem(int position){
        countObjs.remove(position);
    }
    public void addItem(CountObj countObj){
        countObjs.add(countObj);
    }
}
