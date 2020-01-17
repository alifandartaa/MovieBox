package com.example.secondappcataloguemovie.adapter;

import android.util.Log;
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
import com.example.secondappcataloguemovie.R;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CardViewViewHolder> {
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter() {

    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<Movie> items) {
        listMovie.clear();
        listMovie.addAll(items);
        notifyDataSetChanged();
    }

    public void clearData(ArrayList<Movie> items){
        listMovie.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {
        holder.bind(listMovie.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listMovie.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvOverview;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvOverview = itemView.findViewById(R.id.tv_item_overview);
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            Glide.with(itemView)
                    .load(movie.getPosterPath())
                    .apply(new RequestOptions().override(110, 130))
                    .into(imgPoster);
            Log.d("cek", "tes123" + (movie.getPosterPath()));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }
}
