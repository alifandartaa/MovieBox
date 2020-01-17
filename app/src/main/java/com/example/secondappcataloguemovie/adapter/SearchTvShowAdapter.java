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
import com.example.secondappcataloguemovie.R;
import com.example.secondappcataloguemovie.model.TvShow;

import java.util.ArrayList;

public class SearchTvShowAdapter extends RecyclerView.Adapter<SearchTvShowAdapter.CardViewViewHolder> {
    private ArrayList<TvShow> listTvShow = new ArrayList<>();

    public SearchTvShowAdapter(){
    }

    public void setData(ArrayList<TvShow> items) {
        listTvShow.clear();
        listTvShow.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cv_tvshow_search, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {
        holder.bind(listTvShow.get(position));
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

        public void bind(TvShow tvShow) {
            tvTitle.setText(tvShow.getTitle());
            tvOverview.setText(tvShow.getOverview());
            Glide.with(itemView)
                    .load(tvShow.getPosterPath())
                    .apply(new RequestOptions().override(110, 130))
                    .into(imgPoster);
        }
    }
}
