/*Classe Java che servirà per inizializzare la Recycler View
* e mostrare la lista di birre*/

package com.maxp.punkbeers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Beer> beers;
    ArrayList<String> beers_imgs;
    Context context;


    public MyAdapter(Context context, ArrayList<Beer> beers){
        this.context = context;
        this.beers = beers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.beer_tile, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Beer beer = beers.get(position);

        //Render del testo
        holder.beerName.setText(beer.getName());
        holder.shortDesc.setText(beer.getTagline());
        holder.longDesc.setText(beer.getDescription());

        //Render dell'immagine
        String img_url = beer.getImage_url();
        Picasso.get().load(img_url).resize(100,350).into(holder.beerImg);



    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int show = 0;

        TextView beerName, shortDesc, longDesc;
        ImageView beerImg;
        Button info;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //Inizializzazione degli elementi della view
            beerName = itemView.findViewById(R.id.beer_name);
            shortDesc = itemView.findViewById(R.id.beer_sDesc);
            longDesc = itemView.findViewById(R.id.beer_lDesc);
            beerImg = itemView.findViewById(R.id.beer_img);
            info = itemView.findViewById(R.id.info_btn);

            info.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            String sliderName = beerName.getText().toString();
            String sliderSDesc= shortDesc.getText().toString();
            String sliderLDesc = longDesc.getText().toString();

            String imgUrl = "";

            for (Beer b:
                 beers) {
                if (b.getName().equals(sliderName)){
                    imgUrl = b.getImage_url();
                }
            }

            PreferenceEditor.saveParams(context, sliderName, sliderSDesc, sliderLDesc, imgUrl);

            //Serve per rendere visibile il pannello ad ogni clic
            PreferenceEditor.setPanelAvailable(context, show++);
        }
    }
}
