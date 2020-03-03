package com.comsol.fleamarket_demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by xeeshan7860 on 1/7/2018.
 */

public class AllMarkets2 extends AppCompatActivity
{
    DatabaseReference Def;
    ListView listView;
    ArrayList<MarketDataModel> list=new ArrayList<>();
    CustomAdapter_MarketsList customAdapter_marketsList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_layout);
        listView = findViewById(R.id.ListView);
        customAdapter_marketsList=new CustomAdapter_MarketsList(this,list);
        listView.setAdapter(customAdapter_marketsList);
        Def= FirebaseDatabase.getInstance().getReference("Markets");
        Def.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MarketDataModel value=dataSnapshot.getValue(MarketDataModel.class);
                list.add(value);
                customAdapter_marketsList.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
