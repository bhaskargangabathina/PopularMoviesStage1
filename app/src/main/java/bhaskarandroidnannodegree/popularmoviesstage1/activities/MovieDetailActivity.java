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

/*
*
* Detail activity and displays the detail view of a movie
* Inflates MovieDetailFragment for the main UI
*
* */

public class MovieDetailActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
        //getSupportActionBar().setElevation(0f);
    }

}
