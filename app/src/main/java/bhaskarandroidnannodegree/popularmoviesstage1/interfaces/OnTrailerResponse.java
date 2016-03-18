package bhaskarandroidnannodegree.popularmoviesstage1.interfaces;

import java.util.ArrayList;

import bhaskarandroidnannodegree.popularmoviesstage1.model.MovieReview;
import bhaskarandroidnannodegree.popularmoviesstage1.model.TrailerInfo;

/**
 * Created by bhaskar.gangabattina on 3/18/2016.
 */
public interface OnTrailerResponse {
  void onFetchSuccess(ArrayList<TrailerInfo> trailerList, ArrayList<MovieReview> movieReviewsList);
}
