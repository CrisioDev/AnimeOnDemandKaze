package de.a_b_software.anime_on_demand_kaze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class MyAnimes extends AppCompatActivity implements MyAnimeListAdapter.ItemClickListener{

    MyAnimeListAdapter adapter;
    Map<String,String> logincookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animes);

        // data to populate the RecyclerView with
        String[][] data = new String[3][];
        data[0] = getIntent().getStringArrayExtra("EXTRA_TITLE_LIST");
        data[1] = getIntent().getStringArrayExtra("EXTRA_LINK_LIST");
        data[2] = getIntent().getStringArrayExtra("EXTRA_PIC_LIST");
        logincookie = (Map<String,String>) getIntent().getSerializableExtra("EXTRA_COOKIE");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycleMyAnime);
        int numberOfColumns = 2;
        GridLayoutManager lmngr = new GridLayoutManager(this,numberOfColumns);
        recyclerView.setLayoutManager(lmngr);
        adapter = new MyAnimeListAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked anime \"" + adapter.getItem(position) + "\", which is at cell position " + position);
        getWebsite(position);
    }

    private void showSeries(String[][] titleArray){
        Intent myanimesintent = new Intent(this, Series.class);
        myanimesintent.putExtra("EXTRA_TITLE_LIST",titleArray[0]);
        myanimesintent.putExtra("EXTRA_LINK_LIST",titleArray[1]);
        myanimesintent.putExtra("EXTRA_PIC_LIST",titleArray[2]);
        myanimesintent.putExtra("EXTRA_COOKIE", (Serializable) logincookie);
        startActivity(myanimesintent);
    }

    private void getWebsite(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response rmyanimes = Jsoup.connect("https://www.anime-on-demand.de"+adapter.getLink(position)).cookies(logincookie).method(Connection.Method.GET).execute();
                    Document myanimes = rmyanimes.parse();
                    Elements Episodes = myanimes.getElementsByClass("episodebox");
                    String[][] episodesArray = new String[3][Episodes.size()];
                    for (int i = 0; i < Episodes.size(); i++){
                        episodesArray[0][i] = Episodes.get(i).getElementsByClass("episodebox-title").text();
                        episodesArray[1][i] = Episodes.get(i).getElementsByClass("button_to").first().getElementsByTag("input").first().attr("data-playlist");
                        episodesArray[2][i] = Episodes.get(i).getElementsByClass("episodebox-image").first().getElementsByTag("img").first().attr("src");
                    }
                    logincookie = rmyanimes.cookies();
                    showSeries(episodesArray);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }


}
