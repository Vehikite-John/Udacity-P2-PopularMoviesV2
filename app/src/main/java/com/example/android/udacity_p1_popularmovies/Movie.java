package com.example.android.udacity_p1_popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jdavet on 12/30/2016.
 */

public class Movie implements Parcelable {
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

    private Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        voteAverage = in.readDouble();
        plotSynopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeDouble(voteAverage);
        parcel.writeString(plotSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel (Parcel parcel) { return new Movie(parcel); }

        public Movie[] newArray(int i) { return new Movie[i]; }
    };

}
