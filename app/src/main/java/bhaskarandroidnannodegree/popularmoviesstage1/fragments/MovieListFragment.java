package bhaskarandroidnannodegree.popularmoviesstage1.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import bhaskarandroidnannodegree.popularmoviesstage1.R;
import bhaskarandroidnannodegree.popularmoviesstage1.interfaces.AsyncResponse;
import bhaskarandroidnannodegree.popularmoviesstage1.asynctasks.FetchMoviesTask;
import bhaskarandroidnannodegree.popularmoviesstage1.adapters.ImageAdapter;
import bhaskarandroidnannodegree.popularmoviesstage1.interfaces.OnMovieClickListener;
import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;
import bhaskarandroidnannodegree.popularmoviesstage1.activities.MovieDetailActivity;

/*
*
* the main UI of HomeActivity and handles the logic for the view
*
* */

public class MovieListFragment extends Fragment {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();
    private final String STORED_MOVIES = "stored_movies";
    private SharedPreferences prefs;
    private ImageAdapter mMoviePosterAdapter;
    String sortOrder;
    List<Movie> movies = new ArrayList<Movie>();
    private static MovieListFragment fragment;
    private static OnMovieClickListener listener;

    public static MovieListFragment getInstance(OnMovieClickListener cslistener) {
        fragment = new MovieListFragment();
        listener = cslistener;
        return fragment;
    }

   public MovieListFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies = new ArrayList<Movie>();
            storedMovies =
                (ArrayList<Movie>) savedInstanceState.getSerializable(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMoviePosterAdapter = new ImageAdapter(
                getActivity(),
                R.layout.list_item_poster,
                R.id.list_item_poster_imageview,
                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.main_movie_grid);
        gridView.setAdapter(mMoviePosterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie details = movies.get(position);
//                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
//                        .putExtra("movies_details", details);
//                startActivity(intent);
                listener.onMovieClicked(details);
            }

        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get sort order to see if it has recently changed
        String prefSortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(movies.size() > 0 && prefSortOrder.equals(sortOrder)) {
            updatePosterAdapter();
        }else{
            sortOrder = prefSortOrder;
            getMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> storedMovies = new ArrayList<Movie>();
        storedMovies.addAll(movies);
        outState.putSerializable(STORED_MOVIES, storedMovies);
    }

    private void getMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(new AsyncResponse() {
            @Override
            public void onTaskCompleted(List<Movie> results) {
                movies.clear();
                movies.addAll(results);
                updatePosterAdapter();
              if(movies.size()>0) {
                listener.onMoviesFetch(movies.get(0));
              }
            }
        });
        fetchMoviesTask.execute(sortOrder);
    }

    // updates the ArrayAdapter of poster images
    private void updatePosterAdapter() {
        mMoviePosterAdapter.clear();
        for(Movie movie : movies) {
            mMoviePosterAdapter.add(movie.getPoster());
        }
    }

}
