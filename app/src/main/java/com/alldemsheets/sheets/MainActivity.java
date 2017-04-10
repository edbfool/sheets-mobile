package com.alldemsheets.sheets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String SHEET = "com.alldemsheets.sheets";
    ListView sheetListView;
    ArrayList<String> sheetList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                URL url = null;
                try {
                    url = new URL("http://www.alldemsheets.com/mobile.php");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null){
                        System.out.println(line);
                        sheetList.add(line);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        });
        thread.start();

        sheetListView = (ListView) findViewById(R.id.sheetList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, sheetList);
        sheetListView.setAdapter(adapter);

        sheetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (parent.getItemAtPosition(position).toString());
                Toast.makeText(MainActivity.this, selected, Toast.LENGTH_SHORT).show();

                Intent newActivity = new Intent(MainActivity.this, IntentActivity.class);
                newActivity.putExtra(SHEET, selected);

                startActivity(newActivity);
            }
        });
    }
}
