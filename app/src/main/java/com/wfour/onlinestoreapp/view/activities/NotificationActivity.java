package com.wfour.onlinestoreapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.NotificationObject;
import com.wfour.onlinestoreapp.view.adapters.NotificationAdapter;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    RecyclerView notification_recyclerview;
    ArrayList<NotificationObject> notification_list = new ArrayList();
    private NotificationAdapter notificationAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
    }

    void init() {
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notification");
        notification_recyclerview = findViewById(R.id.notification_recyclerview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("bill", "bill");
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        setData();
    }

    void setData() {

        NotificationObject obj;
        int i = 0;
        obj = new NotificationObject();
        obj.setNo_of_msg(String.valueOf(3));
        obj.setNotification_from("Faru riskadu ba feto");
        obj.setNotification_messege("Lorem ipsum dolor sit amet, consetetur sad\n" +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut");

        notification_list.add(obj);
        obj = new NotificationObject();
        obj.setNo_of_msg(String.valueOf(0));
        obj.setNotification_from("Faru riskadu ba feto");
        obj.setNotification_messege("Lorem ipsum dolor sit amet, consetetur sad\n" +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut");

        notification_list.add(obj);

        obj = new NotificationObject();
        obj.setNo_of_msg(String.valueOf(0));
        obj.setNotification_from("Faru riskadu ba feto");
        obj.setNotification_messege("Lorem ipsum dolor sit amet, consetetur sad\n" +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut");

        notification_list.add(obj);

        obj = new NotificationObject();
        obj.setNo_of_msg(String.valueOf(0));
        obj.setNotification_from("Faru riskadu ba feto");
        obj.setNotification_messege("Lorem ipsum dolor sit amet, consetetur sad\n" +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut");

        notification_list.add(obj);

        obj = new NotificationObject();
        obj.setNo_of_msg(String.valueOf(0));
        obj.setNotification_from("Faru riskadu ba feto");
        obj.setNotification_messege("Lorem ipsum dolor sit amet, consetetur sad\n" +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut");

        notification_list.add(obj);

//        notification_recyclerview.setHasFixedSize(true);
//        notificationAdapter = new NotificationAdapter(context, notification_list);
//        notification_recyclerview.setLayoutManager(LayoutManager);
//        notification_recyclerview.setAdapter(notificationAdapter);

        notification_recyclerview.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(context);
        notificationAdapter = new NotificationAdapter(context, notification_list);
        notification_recyclerview.setLayoutManager(LayoutManager);
        notification_recyclerview.setAdapter(notificationAdapter);
    }
}