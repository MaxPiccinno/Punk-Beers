package com.maxp.punkbeers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    RequestQueue requestQueue;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextInputEditText searchBar;
    ImageButton searchBtn, goBackBtn;
    ArrayList<Beer> beers = new ArrayList<Beer>();

    int page;
    boolean canScroll;

    //Elementi slider
    TextView sliderName, sliderSDesc, sliderLDesc;
    ImageView sliderImg;
    SlidingUpPanelLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue= Volley.newRequestQueue(this);

        searchBar = findViewById(R.id.search_bar);
        searchBtn = findViewById(R.id.search_btn);
        goBackBtn = findViewById(R.id.goback_btn);
        searchBtn.setOnClickListener(this);
        goBackBtn.setOnClickListener(this);

        goBackBtn.setVisibility(View.GONE);

        //Elementi slider
        sliderName = findViewById(R.id.beer_name_slide);
        sliderSDesc = findViewById(R.id.beer_sDesc_slide);
        sliderLDesc = findViewById(R.id.beer_lDesc_slide);
        sliderImg = findViewById(R.id.beer_img_slide);

        layout = findViewById(R.id.slidingPanel);
        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                findViewById(R.id.slidingLayout).setAlpha(1);

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){

                }

                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){

                }
            }
        });

        //Nasconde il Panel quando si clicca al di fuori di esso
        layout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        layout.setAnchorPoint((float) 0.5);
        layout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


        recyclerView = findViewById(R.id.recyclerView);

        //Inizializzazione della Recycler View

        myAdapter = new MyAdapter(this, beers);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //Scroll listener per rilevare la fine della pagina e caricare la successiva
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if(canScroll){
                        getNextPage();
                    }

                }
            }
        });


        page = 1;
        getRequest();

    }


    //Metodo per effettuare la richiesta al server
    private void getRequest(){

        canScroll = true;
        beers.clear();
        String url = getResources().getString(R.string.url);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest stringRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //Deserializzazione del json di risposta
                for(int i=0; i<response.length(); i++){

                    //Creazione degli oggetti Beer con i dati ricevuti dal server
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        Beer beer = new Beer();
                        beer.setId(jsonObject.getInt("id"));
                        beer.setName(jsonObject.getString("name"));
                        beer.setTagline(jsonObject.getString("tagline"));
                        beer.setDescription(jsonObject.getString("description"));
                        beer.setImage_url(jsonObject.getString("image_url"));

                        beers.add(beer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Nessuna connessione", BaseTransientBottomBar.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });

        requestQueue.add(stringRequest);

    }

    //Metodo per richiedere la pagina successiva
    public void getNextPage(){

        page++;

        String url = getResources().getString(R.string.url_page);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest stringRequest = new JsonArrayRequest((url + page), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //Deserializzazione del json di risposta
                for(int i=0; i<response.length(); i++){

                    //Creazione degli oggetti Beer con i dati ricevuti dal server
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        Beer beer = new Beer();
                        beer.setId(jsonObject.getInt("id"));
                        beer.setName(jsonObject.getString("name"));
                        beer.setTagline(jsonObject.getString("tagline"));
                        beer.setDescription(jsonObject.getString("description"));
                        beer.setImage_url(jsonObject.getString("image_url"));

                        beers.add(beer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Nessuna connessione", BaseTransientBottomBar.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });

        requestQueue.add(stringRequest);

    }

    //Metodo per effettuare la ricerca per nome
    public void searchBeer(String name){

        //Adatta la stringa per essere concatenata all'URL
        String nameToSearch = name.toLowerCase().replace(' ', '_');
        String url = getResources().getString(R.string.url_search);

        canScroll = false; //Evita il caricamento di pagine non coerenti ai risultati di ricerca
        beers.clear();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest stringRequest = new JsonArrayRequest((url + nameToSearch), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //Deserializzazione del json di risposta
                for(int i=0; i<response.length(); i++){

                    //Creazione degli oggetti Beer con i dati ricevuti dal server
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        Beer beer = new Beer();
                        beer.setId(jsonObject.getInt("id"));
                        beer.setName(jsonObject.getString("name"));
                        beer.setTagline(jsonObject.getString("tagline"));
                        beer.setDescription(jsonObject.getString("description"));
                        beer.setImage_url(jsonObject.getString("image_url"));

                        beers.add(beer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Nessuna connessione", BaseTransientBottomBar.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });

        requestQueue.add(stringRequest);

    }

    /*Listener che viene attivato quando rileva un cambiamento nelle Shared Preferences
    e aggiorna la view dello slider*/
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("sliderTitle")){
            sliderName.setText(PreferenceEditor.getTitle(this));
        }
        if(key.equals("sliderSDesc")){
            sliderSDesc.setText(PreferenceEditor.getSDesc(this));
        }
        if(key.equals("sliderLDesc")){
            sliderLDesc.setText(PreferenceEditor.getLDesc(this));
        }
        if(key.equals("imgUrl")){
            String imgUrl = PreferenceEditor.getImgUrl(this);
            Picasso.get().load(imgUrl).resize(200,750).into(sliderImg);
        }
        if(key.equals("showPanel")){
            //Ogni volta che show viene incrementato, rende il panel visibile
                layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceEditor.registerPref(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceEditor.unregisterPref(this, this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.search_btn){

            if(searchBar.getText().length()<1){
                searchBar.setError("Insert a valid name");
            }else{

                //Effettua la ricerca per nome della birra
                String searchString = searchBar.getText().toString();
                searchBeer(searchString);
                goBackBtn.setVisibility(View.VISIBLE);
            }
        }

        if(v.getId() == R.id.goback_btn){

            //Ritorna alla pagina principale
            getRequest();
            goBackBtn.setVisibility(View.GONE);
            searchBar.setText("");
        }

    }
}