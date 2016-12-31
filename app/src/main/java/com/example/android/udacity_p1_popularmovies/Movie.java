package com.example.android.udacity_p1_popularmovies;

/**
 * Created by jdavet on 12/30/2016.
 */

public class Movie {
    // Movie details layout contains title, release date,
    // movie poster, vote average, and plot synopsis.
    String title;
    // Date date;
    String releaseDate;
    String poster;
    double voteAverage;
    String plotSynopsis;

    public Movie(String mTitle, String mReleaseDate, String mPoster, double mVoteAverage, String mPlotSynopsis) {
        title = mTitle;
        releaseDate = mReleaseDate;
        poster = mPoster;
        voteAverage = mVoteAverage;
        plotSynopsis = mPlotSynopsis;
    }

    // TODO: Create method to parse date to Date object
}
