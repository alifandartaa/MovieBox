package com.example.consumerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.consumerapp.R;
import com.example.consumerapp.model.Favorite;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private ArrayList<Favorite> listFavorite = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public FavoriteAdapter() {
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<Favorite> items) {
        listFavorite.clear();
        listFavorite.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.FavoriteViewHolder holder, int position) {
        holder.bind(listFavorite.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listFavorite.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFavorite.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvType;
        ImageView imgPoster;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_fav_title);
            tvDescription = itemView.findViewById(R.id.tv_fav_overview);
            imgPoster = itemView.findViewById(R.id.img_fav_poster);
            tvType = itemView.findViewById(R.id.tv_fav_type);
        }

        void bind(Favorite favorite) {
            tvTitle.setText(favorite.getTitle());
            tvType.setText(favorite.getType());
            tvDescription.setText(favorite.getDesc());
            Glide.with(itemView.getContext())
                    .load(favorite.getPoster())
                    .apply(new RequestOptions().override(110, 130))
                    .into(imgPoster);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Favorite favorite);
    }
}
