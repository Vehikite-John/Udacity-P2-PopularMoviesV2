package com.example.android.udacity_p1_popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by jdavet on 12/30/2016.
 */

public class MovieDetail extends AppCompatActivity {
    private TextView mMovieInfoDisplay;
    private String mMovieInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        mMovieInfoDisplay = (TextView) findViewById(R.id.tv_detail_display_title);
        Intent intentThatStartedActivity = getIntent();

        if (intentThatStartedActivity != null) {
            if (intentThatStartedActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieInfo = intentThatStartedActivity.getStringExtra(Intent.EXTRA_TEXT);
                mMovieInfoDisplay.setText(mMovieInfo);
            }
        }
    }
}
