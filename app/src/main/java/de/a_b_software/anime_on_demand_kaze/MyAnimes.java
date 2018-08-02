package de.a_b_software.anime_on_demand_kaze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;

public class MyAnimes extends AppCompatActivity implements MyAnimeListAdapter.ItemClickListener{

    MyAnimeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animes);

        // data to populate the RecyclerView with
        String[][] data = new String[3][];
        data[0] = getIntent().getStringArrayExtra("EXTRA_TITLE_LIST");
        data[1] = getIntent().getStringArrayExtra("EXTRA_LINK_LIST");
        data[2] = getIntent().getStringArrayExtra("EXTRA_PIC_LIST");

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
    }


}
