package com.example.tristrum.tuttlemongo;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tristrum on 4/22/17.
 */
public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.MonsterViewHolder> {
    ArrayList<String> urls;
    ArrayList<String> names;
    ArrayList<String> levels;
    Context c;
    Intent i;


    public MonsterAdapter(ArrayList<String> url, ArrayList<String> name, ArrayList<String> level, Context co) {
        urls = url;
        names = name;
        levels = level;
        c = co;
        System.out.println(urls.get(0) + levels.get(0));
    }

    @Override
    public MonsterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MonsterViewHolder monsterViewHolder = new MonsterViewHolder(view, c);
        return monsterViewHolder;
    }

    @Override
    public void onBindViewHolder(MonsterViewHolder holder, final int position) {
        holder.name.setText(names.get(position));
        holder.level.setText(levels.get(position));
        if (!(urls.get(position).equals(""))) {
            Picasso.with(c).load(urls.get(position)).resize(150, 150).into(holder.image);

            holder.trade.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(c, TradeActivity.class);
                    intent.putExtra("name", names.get(position) + " " + levels.get(position));
                    c.startActivity(intent);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        System.out.println(urls.size());
        return urls.size();
    }


    public static class MonsterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.level) TextView level;
        @BindView(R.id.picture) ImageView image;
        @BindView(R.id.buttond) Button trade;

        public MonsterViewHolder(View itemView, Context c) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}


