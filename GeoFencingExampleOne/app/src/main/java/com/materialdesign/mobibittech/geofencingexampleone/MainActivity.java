package com.materialdesign.mobibittech.geofencingexampleone;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    Button addButton,removeButton;
    ArrayAdapter arrayAdapter;
    public String TAG ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton =(Button)findViewById(R.id.addButton);
        removeButton = (Button)findViewById(R.id.removeButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(in);

            }
        });

        ListView fencingList = (ListView)findViewById(R.id.fencingList);
        if(MapsActivity.mGeofenceDataList==null) {
            MapsActivity.mGeofenceDataList = new ArrayList<>();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, MapsActivity.mGeofenceDataList);
fencingList.setAdapter(arrayAdapter);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        arrayAdapter.notifyDataSetChanged();
setLog("onRestart  size  of list"+arrayAdapter.getCount());
    }

    @Override
    protected void onResume() {

        super.onResume();
        setLog("onResume");
    }

    private void setLog(String msg)
    {
        Log.e(TAG,"MESSAGE IS  :"+msg);
    }
}
