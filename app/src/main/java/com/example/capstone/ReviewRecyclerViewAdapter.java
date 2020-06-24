package com.example.capstone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ReviewDTO> reviewDTOS = null;

    ReviewRecyclerViewAdapter(ArrayList<ReviewDTO> reviews) {
        reviewDTOS=reviews;
    }


    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.review_popup_item, parent, false) ;
        ReviewRecyclerViewAdapter.ViewHolder vh = new ReviewRecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ViewHolder holder, final int position) {
        //데이터를 넣어주는 부분
        ReviewDTO item = reviewDTOS.get(position) ;
//       holder.name.setText(reviewDTOS.get(position).userName);
       holder.date.setText(reviewDTOS.get(position).date);
       holder.text.setText(reviewDTOS.get(position).text);
       holder.star.setRating((float) reviewDTOS.get(position).score);
       if(reviewDTOS.get(position).userID!=1){
           ((Button)holder.delete).setVisibility(View.INVISIBLE);
       }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDTOS.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reviewDTOS.size());
                //DB에서도 삭제 하기

            }
        });

    }

    @Override
    public int getItemCount() {
        //카운터
        return reviewDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView text;
        RatingBar star;

        Button delete;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.review_popup_item_name);
            date = view.findViewById(R.id.review_popup_item_date);
            text = view.findViewById(R.id.review_popup_item_text);
            star = view.findViewById(R.id.review_popup_item_star);
            delete = view.findViewById(R.id.review_popup_item_delete);
        }

    }


}

