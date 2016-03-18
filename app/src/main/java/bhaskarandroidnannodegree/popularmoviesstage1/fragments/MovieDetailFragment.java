package bhaskarandroidnannodegree.popularmoviesstage1.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import bhaskarandroidnannodegree.popularmoviesstage1.R;
import bhaskarandroidnannodegree.popularmoviesstage1.asynctasks.FetchTrailReview;
import bhaskarandroidnannodegree.popularmoviesstage1.interfaces.OnTrailerResponse;
import bhaskarandroidnannodegree.popularmoviesstage1.model.Movie;
import bhaskarandroidnannodegree.popularmoviesstage1.model.MovieReview;
import bhaskarandroidnannodegree.popularmoviesstage1.model.TrailerInfo;
import bhaskarandroidnannodegree.popularmoviesstage1.utils.Util;

/*
*
* the main UI of MovieDetailActivity and handles the logic for the view
*
* */

public class MovieDetailFragment extends Fragment implements OnTrailerResponse {

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static String KEY_MOVIE = "movie";
    private String movieId;
    private String strTitle;
    private String strPosterImage;
    private String strReleaseDate;
    private String strRatings;
    private String strOverView;
    private TextView title;
    private ImageView poster;
    private TextView releaseDate;
    private TextView ratings;
    private TextView overview;
    private LinearLayout llReviews, llTrailers;
    private TextView tvNoTrailer, tvNoReview;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Movie movie = (Movie) bundle.getSerializable(KEY_MOVIE);
            movieId = movie.getId();
            strTitle = movie.getTitle();
            strOverView = movie.getOverview();
            strPosterImage = movie.getPoster();
            strReleaseDate = movie.getReleaseDate();
            strRatings = movie.getVoteAverage();
        }
        llReviews = (LinearLayout) rootView.findViewById(R.id.llReviews);
        llTrailers = (LinearLayout) rootView.findViewById(R.id.llTrailers);
        tvNoReview = (TextView) rootView.findViewById(R.id.noReviews);
        tvNoTrailer = (TextView) rootView.findViewById(R.id.noTrailers);
        DisplayInfo(rootView);
        if(!Util.isEmpty(movieId)) {
            FetchTrailReview weatherTask = new FetchTrailReview(this);
            weatherTask.execute(movieId);
        }

        return rootView;
    }

    public void setDetails(){
        if(!Util.isEmpty(strTitle)) {
            title.setText(strTitle);
        }
        if(!Util.isEmpty(strPosterImage)) {
            Picasso.with(getActivity()).load(strPosterImage).into(poster);
        }
        if(!Util.isEmpty(strReleaseDate)) {
            releaseDate.setText(strReleaseDate);
        }
        if(!Util.isEmpty(strRatings)) {
            ratings.setText(strRatings + "/10");
        }
        if(!Util.isEmpty(strOverView)) {
            overview.setText(strOverView);
        }
    }

    private void DisplayInfo(View v){
        title = (TextView) v.findViewById(R.id.movie_title_view);
        poster = (ImageView) v.findViewById(R.id.poster_image_view);
        releaseDate = (TextView) v.findViewById(R.id.release_date);
        ratings = (TextView) v.findViewById(R.id.ratings_view);
        overview = (TextView) v.findViewById(R.id.synopsis_view);
        setDetails();
    }

    @Override
    public void onFetchSuccess(ArrayList<TrailerInfo> trailerList,
                               ArrayList<MovieReview> movieReviewsList) {
        LayoutInflater layoutInflater =
            (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llReviews.removeAllViews();
        llTrailers.removeAllViews();
        if(trailerList.size() >0) {
            for (int i = 0; i < trailerList.size(); i++) {
                View view = layoutInflater.inflate(R.layout.list_trailer_movie, null);
                TextView trailer = (TextView) view.findViewById(R.id.trailer_name);
                ImageView trailerImg = (ImageView) view.findViewById(R.id.youtubeImg);
                String source = trailerList.get(i).getSource();
                trailer.setText(trailerList.get(i).getName());
                final String BASE_URL = "http://img.youtube.com/vi/";
                final String url = BASE_URL + source + "/0.jpg";
                Picasso.with(getActivity()).load(url).into(trailerImg);
                trailerImg.setAdjustViewBounds(true);

                final String trailerUrl = "https://www.youtube.com/watch?v=" + source;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                        getActivity().startActivity(intent);
                    }
                });
                llTrailers.addView(view);
            }
        }else {
            tvNoTrailer.setVisibility(View.VISIBLE);
        }
        if(movieReviewsList.size() >0) {
        for (int i=0; i<movieReviewsList.size(); i++){
            View view = layoutInflater.inflate(R.layout.list_review_movie, null);
            TextView author = (TextView) view.findViewById(R.id.review_author);
            TextView contentView = (TextView) view.findViewById(R.id.review_content);
            TextView urlView = (TextView) view.findViewById(R.id.review_url);
            author.setText("Author:  " + movieReviewsList.get(i).getAuthor());
            contentView.setText("Content:  " + movieReviewsList.get(i).getContent());
            urlView.setText("Look more at:  " + movieReviewsList.get(i).getUrl());
            llReviews.addView(view);
        }
    }else {
        tvNoReview.setVisibility(View.VISIBLE);
    }
    }
}
