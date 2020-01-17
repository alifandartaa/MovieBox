package com.example.secondappcataloguemovie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.secondappcataloguemovie.model.Movie;
import com.example.secondappcataloguemovie.model.TvShow;
import com.example.secondappcataloguemovie.R;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.CardViewViewHolder> {
    private ArrayList<TvShow> listTvShow = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public TvShowAdapter(){

    }

    public void setOnItemClickCallback(TvShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<TvShow> items) {
        listTvShow.clear();
        listTvShow.addAll(items);
        notifyDataSetChanged();
    }

    public void clearData(ArrayList<TvShow> items){
        listTvShow.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_tv_show, parent, false);
        return new TvShowAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {

        holder.bind(listTvShow.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listTvShow.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTvShow.size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvOverview;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvOverview = itemView.findViewById(R.id.tv_item_overview);
        }

        void bind(TvShow tvShow) {
            tvTitle.setText(tvShow.getTitle());
            tvOverview.setText(tvShow.getOverview());
            Glide.with(itemView)
                    .load(tvShow.getPosterPath())
                    .apply(new RequestOptions().override(110, 130))
                    .into(imgPoster);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow data);
    }
}
