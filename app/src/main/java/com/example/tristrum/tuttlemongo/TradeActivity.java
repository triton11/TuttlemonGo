package com.example.tristrum.tuttlemongo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tristrum on 4/23/17.
 */

public class TradeActivity extends AppCompatActivity {

    String namelevel;
    String namename;

    @BindView(R.id.imageView) ImageView image;
    @BindView(R.id.textView) TextView code;
    @BindView(R.id.button2) Button button;
    @BindView(R.id.txtMessageToSend) EditText et;

    Context context;

    SavedHelper mydb;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        Intent i = getIntent();
        namelevel = i.getStringExtra("name");
        ButterKnife.bind(this);
        context = this;

        String[] q = namelevel.split(" ");

        String url = "https://robohash.org/" + q[0] + " " + q[1] + ".png?set=set3";

        Picasso.with(this).load(url).resize(150, 150).into(image);

        StringBuilder sb = new StringBuilder();
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random r = new Random();
        for (int k = 0; k < 6; k++) {
            sb.append(chars[r.nextInt(26)]);
        }
        System.out.println(sb.toString());
        final String cc = sb.toString();

        code.setText(cc);
        button.setVisibility(View.GONE);

        RedisServer.getService().makePost(cc, namelevel).enqueue(new Callback<RedisServer.SetResponse>() {
            @Override
            public void onResponse(Call<RedisServer.SetResponse> call, Response<RedisServer.SetResponse> response) {
                String url = "http://ec2-34-197-228-35.compute-1.amazonaws.com/c2a10e81e210ceee008dd391f6c6ec43/GET/" + cc;
                changeB();
            }

            @Override
            public void onFailure(Call<RedisServer.SetResponse> call, Throwable t) {

            }
        });
    }

    public void onClickGo(View v) {
        mydb = new SavedHelper(this);
        String getcode = et.getText().toString();
        RedisServer.getService().getPost(getcode).enqueue(new Callback<RedisServer.GetResponse>() {
            @Override
            public void onResponse(Call<RedisServer.GetResponse> call, Response<RedisServer.GetResponse> response) {
                String[] we = response.body().item.split(" ");
                System.out.println(response.body().item);
                String firstname = we[0] + " " + we[1];
                String level = String.valueOf(Integer.parseInt(we[2]) + 1);

                mydb.insertMonster(firstname, level, "https://robohash.org/" + firstname + ".png?set=set3");
                String[] q = namelevel.split(" ");
                String title = q[0] + " " + q[1];
                mydb.deleteTitle(title);
                mydb.close();
                deleteOld();
            }

            @Override
            public void onFailure(Call<RedisServer.GetResponse> call, Throwable t) {

            }
        });



    }

    public void changeB() {
        System.out.println("VIEW BUTTON");
        button.setVisibility(View.VISIBLE);
    }

    public void deleteOld() {

        RedisServer.getService().deletePost(et.getText().toString()).enqueue(new Callback<RedisServer.DelResponse>() {
            @Override
            public void onResponse(Call<RedisServer.DelResponse> call, Response<RedisServer.DelResponse> response) {
                Intent i = new Intent(context, CollectActivity.class);
                context.startActivity(i);
            }
            @Override
            public void onFailure(Call<RedisServer.DelResponse> call, Throwable t) {

            }
        });


    }










}
