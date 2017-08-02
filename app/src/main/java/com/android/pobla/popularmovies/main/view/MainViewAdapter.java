package com.android.pobla.popularmovies.main.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.main.view.MainViewAdapter.MovieViewHolder;
import com.android.pobla.popularmovies.model.Movie;
import com.android.pobla.popularmovies.model.MovieSizes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainViewAdapter extends Adapter<MovieViewHolder>  {

  private final ItemClickListener itemClickListener;

  public MainViewAdapter(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }


  private final List<Movie> movies = new ArrayList<>();

  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, int position) {
    Movie movie = movies.get(position);
    Picasso.with(holder.moviePoster.getContext())
      .load(buildImageUrl(movie))
      .into(holder.moviePoster);
    holder.movieTitle.setText(movie.getTitle());

  }

  private String buildImageUrl(Movie movie) {
    return movie.getImageUrlWithSize(MovieSizes.W185);
  }

  @Override
  public int getItemCount() {
    return this.movies.size();
  }

  public void setMovies(List<Movie> movies) {
    this.movies.clear();
    this.movies.addAll(movies);
    notifyDataSetChanged();
  }

  class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

    final TextView movieTitle;
    final ImageView moviePoster;

    public MovieViewHolder(View itemView) {
      super(itemView);
      movieTitle = (TextView) itemView.findViewById(R.id.textview_main_movieTitle);
      moviePoster = (ImageView) itemView.findViewById(R.id.imageView_main_moviePoster);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int clickedPosition = getAdapterPosition();
      itemClickListener.onItemClick(movies.get(clickedPosition));
    }
  }

  public interface ItemClickListener {
    void onItemClick(Movie movie);
  }
}
