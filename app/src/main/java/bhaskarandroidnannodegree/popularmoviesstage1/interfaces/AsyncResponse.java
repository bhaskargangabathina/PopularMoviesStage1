package bhaskarandroidnannodegree.popularmoviesstage1.interfaces;

import java.util.List;

import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;

/*
*
* Custom interface to impliment
* a call back function that returns results
* from an async task
*
* */

public interface AsyncResponse {

    void onTaskCompleted(List<Movie> results);

}
