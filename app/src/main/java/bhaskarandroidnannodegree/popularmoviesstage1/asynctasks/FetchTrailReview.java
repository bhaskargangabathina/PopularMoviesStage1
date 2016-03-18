/*
 * Copyright (C) 2016 Anupam Das
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bhaskarandroidnannodegree.popularmoviesstage1.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bhaskarandroidnannodegree.popularmoviesstage1.BuildConfig;
import bhaskarandroidnannodegree.popularmoviesstage1.interfaces.OnTrailerResponse;
import bhaskarandroidnannodegree.popularmoviesstage1.model.MovieReview;
import bhaskarandroidnannodegree.popularmoviesstage1.model.TrailerInfo;


/**
 * Created by anupam on 24/12/15.
 */
public class FetchTrailReview extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchTrailReview.class.getSimpleName();

    private boolean DEBUG = true;
    ArrayList<TrailerInfo> listTrailers = new ArrayList<>();
    ArrayList<MovieReview> listMovieReviews = new ArrayList<>();
    OnTrailerResponse listener;

    public FetchTrailReview(OnTrailerResponse response) {
        listener = response;
    }

    /**
     * Take the String representing the complete movie list in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String NAME = "name";
        final String SIZE = "size";
        final String SOURCE = "source";
        final String TYPE = "type";
        final String REVIEW_ID = "id";
        final String GENRE_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";
        final String TOTAL_PAGES_REVIEWS = "total_pages";
        final String TOTAL_RESULTS_REVIEWS = "total_results";
        final String PAGE = "page";
        final String MOVIE_ID = "id";

        final String RESULT1 = "trailers";
        final String RESULT2 = "reviews";
        final String RESULT3 = "genres";
        final String YOUTUBE = "youtube";


        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArrayTrailer = movieJson.getJSONObject(RESULT1).getJSONArray(YOUTUBE);
            JSONObject movieArrayReviews = movieJson.getJSONObject(RESULT2);
            JSONArray movieArrayGenres = movieJson.getJSONArray(RESULT3);

            String movie_id = movieJson.getString(MOVIE_ID);

            for (int i = 0; i < movieArrayTrailer.length(); i++) {

                JSONObject trailerInfo = movieArrayTrailer.getJSONObject(i);


                String name;
                String size;
                String source;
                String type;
                name = trailerInfo.getString(NAME);
                size = trailerInfo.getString(SIZE);
                source = trailerInfo.getString(SOURCE);
                type = trailerInfo.getString(TYPE);

                TrailerInfo trailer = new TrailerInfo();
                trailer.setName(name);
                trailer.setSize(size);
                trailer.setSource(source);
                trailer.setType(type);

                listTrailers.add(trailer);

            }

            String page = movieArrayReviews.getString(PAGE);
            String total_page = movieArrayReviews.getString(TOTAL_PAGES_REVIEWS);
            String total_results = movieArrayReviews.getString(TOTAL_RESULTS_REVIEWS);

            JSONArray reviews = movieArrayReviews.getJSONArray("results");

            for (int j = 0; j < reviews.length(); j++) {

                JSONObject reviewsInfo = reviews.getJSONObject(j);

                String id;
                String author;
                String content;
                String url;
                id = reviewsInfo.getString(REVIEW_ID);
                author = reviewsInfo.getString(AUTHOR);
                content = reviewsInfo.getString(CONTENT);
                url = reviewsInfo.getString(URL);

                MovieReview review = new MovieReview();
                review.setId(id);
                review.setAuthor(author);
                review.setContent(content);
                review.setUrl(url);

                listMovieReviews.add(review);
            }

            for (int i = 0; i < movieArrayGenres.length(); i++) {

                JSONObject gen = movieArrayGenres.getJSONObject(i);
                String genre_id = gen.getString(GENRE_ID);
                String name = gen.getString(NAME);

            }


        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the movieAPI query
            // Possible parameters are avaiable at OWM's forecast API page, at
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";
            final String DATA = "append_to_response";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(DATA, "trailers,reviews")
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v("BHASKAR", "URL="+url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onFetchSuccess(listTrailers, listMovieReviews);
    }
}