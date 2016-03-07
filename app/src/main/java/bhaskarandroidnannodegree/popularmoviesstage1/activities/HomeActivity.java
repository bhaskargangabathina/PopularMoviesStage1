package bhaskarandroidnannodegree.popularmoviesstage1.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import bhaskarandroidnannodegree.popularmoviesstage1.R;
import bhaskarandroidnannodegree.popularmoviesstage1.fragments.MovieDetailFragment;
import bhaskarandroidnannodegree.popularmoviesstage1.fragments.MovieListFragment;

/*
*
* Main activity and entry point into the app
* Inflates MovieListFragment for the main UI
*
* */

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getFragmentManager();
    MovieListFragment fragment;
    MovieDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fragment = new MovieListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }else{
            fragment = (MovieListFragment) fragmentManager.getFragment(
                    savedInstanceState, "fragmentContent");
        }

       /* if (findViewById(R.id.fragment_container) != null){

            if (savedInstanceState == null) {
                detailFragment = new MovieDetailFragment();
                fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, detailFragment)
                    .commit();
            }else{
                detailFragment = (MovieDetailFragment) fragmentManager.getFragment(
                    savedInstanceState, "fragmentContentDetail");
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fragmentManager.putFragment(savedInstanceState, "fragmentContent", fragment);
    }

}
