package com.example.tristrum.tuttlemongo;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tristrum on 4/22/17.
 */

public class CollectActivity extends AppCompatActivity {
    RecyclerView.Adapter myAdaptor;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> names;
    ArrayList<String> urls;
    ArrayList<String> levels;
    SavedHelper mydb;
    OkHttpClient client;
    String firstname;

    @BindView(R.id.RecycleView) RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        mydb = new SavedHelper(this);
        names = new ArrayList<String>();
        urls = new ArrayList<String>();
        levels = new ArrayList<String>();

        Cursor resultSet = mydb.getDataSearch(1);
        while (resultSet.moveToNext()) {
            names.add(resultSet.getString(1));
            levels.add(resultSet.getString(2));
            urls.add(resultSet.getString(3));
        }


        if (names.isEmpty()) {
            names.add("");
            levels.add("");
            urls.add("");
            client = new OkHttpClient();
            final String url = "http://uinames.com/api/";
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        Request request = new Request.Builder()
                                .url(url)
                                .build();


                        Response response = client.newCall(request).execute();
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            firstname = json.getString("name") + " " + json.getString("surname");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        levels.clear();
                        names.clear();
                        urls.clear();
                        mydb.insertMonster(firstname, "1", "https://robohash.org/" + firstname + ".png?set=set3");
                        names.add(firstname);
                        levels.add("1");
                        urls.add("https://robohash.org/" + firstname + ".png?set=set3");
                        mydb.close();
                        myAdaptor.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } else {
            mydb.close();
        }

        myAdaptor = new MonsterAdapter(urls, names, levels, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        System.out.println(myAdaptor + "HEH");
        myRecyclerView.setAdapter(myAdaptor);
        myRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }









}
