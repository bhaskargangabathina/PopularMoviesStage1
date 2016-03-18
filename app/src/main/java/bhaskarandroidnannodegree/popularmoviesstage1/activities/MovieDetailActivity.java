package bhaskarandroidnannodegree.popularmoviesstage1.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import bhaskarandroidnannodegree.popularmoviesstage1.R;
import bhaskarandroidnannodegree.popularmoviesstage1.fragments.MovieDetailFragment;
import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;

/*
*
* Detail activity and displays the detail view of a movie
* Inflates MovieDetailFragment for the main UI
*
* */

public class MovieDetailActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getFragmentManager();
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getSerializableExtra(MovieDetailFragment.KEY_MOVIE);
        if (savedInstanceState == null) {
            MovieDetailFragment newDesriptionFragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable(MovieDetailFragment.KEY_MOVIE, movie);
            newDesriptionFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.container, newDesriptionFragment)
                    .commit();
        }
        //getSupportActionBar().setElevation(0f);
    }

}
