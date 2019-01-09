package com.alialaoui.guesstheagechallenge.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alialaoui.guesstheagechallenge.R;
import com.squareup.picasso.Picasso;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String[] mData;
    private int[] buttonPics;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int currentLevelNum;

    final private int VIEWSIZE = 100; //if you change this in xml, you should change it here

    Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, String[] data, int[] buttonPics, int currentLevelNum) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.buttonPics = buttonPics;
        this.context = context; //needed for bitmap
        this.currentLevelNum = currentLevelNum; //needed to load specific icons
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the Button in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //used to get photos
        if(currentLevelNum > position) {
            Picasso.get().load(buttonPics[position]).resize(convertDpToPixel(VIEWSIZE, context), convertDpToPixel(VIEWSIZE, context)).into(holder.myImage);
            holder.myText.setText(mData[position]);
        }
        else if(currentLevelNum == position) {
            Picasso.get().load(R.drawable.ark).resize(convertDpToPixel(VIEWSIZE, context), convertDpToPixel(VIEWSIZE, context)).into(holder.myImage);
            holder.playText.setText("Play");
            holder.myText.setText(mData[position]);
        }
        else {
            Picasso.get().load(R.drawable.zamok).resize(convertDpToPixel(VIEWSIZE, context), convertDpToPixel(VIEWSIZE, context)).into(holder.myImage);
            //#1d40d1
            holder.myText.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            holder.myText.setText(mData[position]);
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImage;
        TextView myText;
        TextView playText;

        ViewHolder(View itemView) {
            super(itemView);

            myImage = itemView.findViewById(R.id.info_text);
            myImage.setAlpha(.5f);
            myImage.setOnClickListener(this);

            myText = itemView.findViewById(R.id.levelNumText);
            playText = itemView.findViewById(R.id.playNowText);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/GillSansC-Bold.ttf");
            myText.setTypeface(font);
            playText.setTypeface(font);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }

}