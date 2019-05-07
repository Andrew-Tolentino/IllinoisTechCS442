package com.example.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ArticleFragment extends Fragment implements Serializable {

    // Create a new Article Fragment here
    public static final ArticleFragment newInstance(Article article, int size) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("article", article);
        bdl.putInt("size", size);

        articleFragment.setArguments(bdl);
        return articleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use the inflater passed in to build (inflate) the fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final Article article = (Article) getArguments().getSerializable("article");

        // Get TextViews
        TextView titleView = rootView.findViewById(R.id.fragment_title);
        titleView.setText(article.getTitle());
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize the implied Intent that will open up the URL through the phone's web browser
                Intent i = new Intent(Intent.ACTION_VIEW);

                // Set the data the intent is operating on by setting up the URL
                i.setData(Uri.parse(article.getUrl()));

                // Start activity and go to the stock's website
                startActivity(i);
            }
        });

        Log.d("Fragment", "Title: " + article.getTitle());

        TextView dateView = rootView.findViewById(R.id.fragment_date);
        if (!article.getDate().isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyy'T'HH:mm");
            try {
                dateView.setText(simpleDateFormat.parse(article.getDate()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else {
            dateView.setText(article.getDate());

        }


        TextView authorView = rootView.findViewById(R.id.fragment_author);
        authorView.setText(article.getAuthor());

        ImageView imageView = rootView.findViewById(R.id.fragment_image);
        Picasso picasso = new Picasso.Builder(getActivity()).build();
        picasso.load(article.getImageUrl())
                .error(R.drawable.image_not_found)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize the implied Intent that will open up the URL through the phone's web browser
                Intent i = new Intent(Intent.ACTION_VIEW);

                // Set the data the intent is operating on by setting up the URL
                i.setData(Uri.parse(article.getUrl()));

                // Start activity and go to the stock's website
                startActivity(i);
            }
        });

        TextView descriptionView = rootView.findViewById(R.id.fragment_description);
        descriptionView.setText(article.getDescription());

        descriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize the implied Intent that will open up the URL through the phone's web browser
                Intent i = new Intent(Intent.ACTION_VIEW);

                // Set the data the intent is operating on by setting up the URL
                i.setData(Uri.parse(article.getUrl()));

                // Start activity and go to the stock's website
                startActivity(i);
            }
        });

        descriptionView.setMovementMethod(new ScrollingMovementMethod());

        TextView countView = rootView.findViewById(R.id.fragment_count);
        countView.setText(Integer.toString(article.getPos()) + " of " + Integer.toString(getArguments().getInt("size")));


        return rootView;


    }
}
