package com.android.pobla.popularmovies.main.view;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MovieSizes;
import com.android.pobla.popularmovies.main.view.MainViewAdapter.MovieViewHolder;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainViewAdapter extends Adapter<MovieViewHolder> {

  private final ItemClickListener itemClickListener;

  public MainViewAdapter(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  private Cursor cursor;

  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, int position) {
    cursor.moveToPosition(position);
    Movie movie = MovieEntry.toMovie(cursor);
    Picasso.with(holder.moviePoster.getContext())
      .load(buildImageUrl(movie))
      .placeholder(R.drawable.ic_autorenew_black_24dp)
      .error(R.drawable.ic_error_outline_black_24dp)
      .into(holder.moviePoster);
    holder.movieTitle.setText(movie.getTitle());

  }

  private String buildImageUrl(Movie movie) {
    return movie.getImageUrlWithSize(MovieSizes.W185);
  }

  @Override
  public int getItemCount() {
    return (this.cursor != null) ? cursor.getCount() : 0;
  }

  public void setCursor(Cursor cursor) {
    if (this.cursor != null) {
      this.cursor.close();
    }
    this.cursor = cursor;
    notifyDataSetChanged();
  }

  class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    @BindView(R.id.textview_main_movieTitle)
    TextView movieTitle;
    @BindView(R.id.imageView_main_moviePoster)
    ImageView moviePoster;

    public MovieViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int clickedPosition = getAdapterPosition();
      cursor.moveToPosition(clickedPosition);
      Movie movie = MovieEntry.toMovie(cursor);
      itemClickListener.onItemClick(movie);
    }
  }

  public interface ItemClickListener {
    void onItemClick(Movie movie);
  }
}
