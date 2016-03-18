package bhaskarandroidnannodegree.popularmoviesstage1.interfaces;

import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;

/**
 * Created by bhaskar.gangabattina on 3/7/2016.
 */
public interface OnMovieClickListener {
  void onMovieClicked(Movie movie);
  void onMoviesFetch(Movie movie);
}
