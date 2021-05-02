package com.wfour.onlinestoreapp.network.modelmanager.singletonmanager;

import com.wfour.onlinestoreapp.objects.Person;
import java.util.ArrayList;

public class AddressManager {
    public ArrayList<Person> personList = new ArrayList<>();
    public static AddressManager mInstance;
    public AddressManager(){

    }
    public static AddressManager getInstance() {
        if (mInstance == null) {
            {
                mInstance = new AddressManager();
            }
        }
        return mInstance;
    }
    public void addItem (Person person){
        personList.add(person);
    }
    public void removeItem (int position){
        personList.remove(position);
    }
    public ArrayList<Person> getArray(){
        return this.personList;
    }
}
