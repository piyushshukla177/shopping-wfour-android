package com.wfour.onlinestoreapp.interfaces;

import com.wfour.onlinestoreapp.objects.Person;

public interface MyOnClick {
     void DeleteItem(int position);
     void EditItem (Person person, int position);
}
