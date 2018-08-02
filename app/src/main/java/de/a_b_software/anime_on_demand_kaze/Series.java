package de.a_b_software.anime_on_demand_kaze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class Series extends AppCompatActivity implements MySeriesAdapter.ItemClickListener{
    MySeriesAdapter adapter;
    Map<String,String> logincookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        // data to populate the RecyclerView with
        String[][] data = new String[3][];
        data[0] = getIntent().getStringArrayExtra("EXTRA_TITLE_LIST");
        data[1] = getIntent().getStringArrayExtra("EXTRA_LINK_LIST");
        data[2] = getIntent().getStringArrayExtra("EXTRA_PIC_LIST");
        logincookie = (Map<String,String>) getIntent().getSerializableExtra("EXTRA_COOKIE");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycleSeries);
        int numberOfColumns = 1;
        GridLayoutManager lmngr = new GridLayoutManager(this,numberOfColumns);
        recyclerView.setLayoutManager(lmngr);
        adapter = new MySeriesAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, final int position) {
        Log.i("TAG", "You clicked anime \"" + adapter.getItem(position) + "\", which is at cell position " + position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response episodelink = Jsoup.connect("https://www.anime-on-demand.de"+adapter.getLink(position))
                            .referrer("https://www.anime-on-demand.de/anime/249")
                            .header("Accept","application/json, text/javascript, */*;")
                            .cookies(logincookie)
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();

                    System.out.println(episodelink.body());
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
