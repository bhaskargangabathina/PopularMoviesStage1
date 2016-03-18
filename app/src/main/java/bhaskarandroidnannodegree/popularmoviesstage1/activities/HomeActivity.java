package bhaskarandroidnannodegree.popularmoviesstage1.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import bhaskarandroidnannodegree.popularmoviesstage1.R;
import bhaskarandroidnannodegree.popularmoviesstage1.fragments.MovieDetailFragment;
import bhaskarandroidnannodegree.popularmoviesstage1.fragments.MovieListFragment;
import bhaskarandroidnannodegree.popularmoviesstage1.interfaces.OnMovieClickListener;
import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;

/*
*
* Main activity and entry point into the app
* Inflates MovieListFragment for the main UI
*
* */

public class HomeActivity extends AppCompatActivity implements OnMovieClickListener {

  private FragmentManager fragmentManager = getFragmentManager();
  MovieListFragment fragment;
  MovieDetailFragment detailFragment;
  private boolean mHasTwoPane = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      //fragment = new MovieListFragment();
      fragment = MovieListFragment.getInstance(this);
      fragmentManager.beginTransaction()
          .add(R.id.container, fragment)
          .commit();
    } else {
      fragment = (MovieListFragment) fragmentManager.getFragment(
          savedInstanceState, "fragmentContent");
    }

    if (findViewById(R.id.fragment_container) != null) {
      mHasTwoPane = true;
      Log.v("BHASKAR", "mHasTwoPane="+mHasTwoPane);
        detailFragment = new MovieDetailFragment();
        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, detailFragment)
            .commit();
    }
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

  @Override
  public void onMovieClicked(Movie movie) {
    Log.v("BHASKAR", "mHasTwoPane="+mHasTwoPane);
    if (mHasTwoPane) {
        MovieDetailFragment newDesriptionFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
      args.putSerializable(MovieDetailFragment.KEY_MOVIE, movie);
        newDesriptionFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, newDesriptionFragment,
            "fragmentContentDetail").commit();
    } else {
      Intent intent = new Intent(this, MovieDetailActivity.class)
          .putExtra(MovieDetailFragment.KEY_MOVIE, movie);
      startActivity(intent);
    }
  }

  @Override
  public void onMoviesFetch(Movie movie) {
    if (mHasTwoPane) {
      MovieDetailFragment newDesriptionFragment = new MovieDetailFragment();
      Bundle args = new Bundle();
      args.putSerializable(MovieDetailFragment.KEY_MOVIE, movie);
      newDesriptionFragment.setArguments(args);
      fragmentManager.beginTransaction().replace(R.id.fragment_container, newDesriptionFragment,
          "fragmentContentDetail").commit();
    }
  }
}
