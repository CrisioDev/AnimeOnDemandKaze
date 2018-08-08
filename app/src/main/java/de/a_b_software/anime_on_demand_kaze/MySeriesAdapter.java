package de.a_b_software.anime_on_demand_kaze;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class MySeriesAdapter extends RecyclerView.Adapter<MySeriesAdapter.ViewHolder> {
    private String[] mData;
    private String[] picData;
    private String[] linkData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MySeriesAdapter(Context context, String[][] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data[0];
        this.linkData = data[1];
        this.picData = data[2];
    }

    // inflates the cell layout from xml when needed
    @Override
    public MySeriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.series_item, parent, false);
        return new MySeriesAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(MySeriesAdapter.ViewHolder holder, int position) {
        holder.myTextView.setText(mData[position]);
        new MyAnimeListAdapter.DownloadImageTask((ImageView) holder.myImageView)
                .execute(picData[position]);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            myImageView = (ImageView) itemView.findViewById(R.id.infoPic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id] + " " + linkData[id] + " " + picData[id];
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
