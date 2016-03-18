package bhaskarandroidnannodegree.popularmoviesstage1.model;

/*
*
* simple object to store all the information
* for a movie being used in the UI
*
* Takes a string for the movie title, poster, overview, voteAverage
* and releaseDate
*
* Implements parcelable in order to easily pass between intents
*
* */

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Serializable {
  private String id;
  private String title;
  private String poster;
  private String overview;
  private String voteAverage;
  private String releaseDate;

  public Movie(String id, String title, String poster, String overview,
               String voteAverage, String releaseDate) {
    this.id = id;
    this.title = title;
    this.poster = poster;
    this.overview = overview;
    this.voteAverage = voteAverage;
    this.releaseDate = releaseDate;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getPoster() {
    return poster;
  }

  public String getOverview() {
    return overview;
  }

  public String getVoteAverage() {
    return voteAverage;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

}