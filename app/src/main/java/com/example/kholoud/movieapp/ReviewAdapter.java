package com.example.kholoud.movieapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    @SuppressWarnings("NotUsed")
    private final static String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private final ArrayList<Reviews> reviews;
    private final OnItemClickListener onItemClickListener;

    public ReviewAdapter(ArrayList<Reviews> reviews, OnItemClickListener listener) {
        this.reviews = reviews;
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void read_reviews(Reviews review, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Reviews review = reviews.get(position);

        holder.mReview = review;
        holder.mContentView.setText(review.getContent());
        holder.mAuthorView.setText(review.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.read_reviews(review, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.review_content)
        TextView mContentView;
        @BindView(R.id.review_author)
        TextView mAuthorView;
        public Reviews mReview;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
//************************************************
    public void add(List<Reviews> reviews) {
        reviews.clear();
        reviews.addAll(reviews);
        notifyDataSetChanged();
    }
//***********************************
    public ArrayList<Reviews> getReviews() {
        return reviews;
    }
}
