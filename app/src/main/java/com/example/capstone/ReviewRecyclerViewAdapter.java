package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReviewDTO> reviewDTOS = new ArrayList<>();

    ReviewRecyclerViewAdapter(ArrayList<ReviewDTO> reviews) {
        System.out.println("ReviewRecyclerViewAdapter");
        reviewDTOS.addAll(reviews);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");

        //XML을 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);


        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        System.out.println("onBindViewHolder");
        //데이터를 넣어주는 부분
        ((RowCell) holder).name.setText("aaa");
        ((RowCell) holder).date.setText("bbb");
        ((RowCell) holder).text.setText("ccc");
        ((RowCell) holder).star.setRating((float) reviewDTOS.get(position).score);
      // ((RowCell) holder).name.setText(String.valueOf(reviewDTOS.get(position).FranchiseID));
      // ((RowCell) holder).date.setText(String.valueOf(reviewDTOS.get(position).ID));
      // ((RowCell) holder).text.setText(reviewDTOS.get(position).text);
      // ((RowCell) holder).star.setRating((float) reviewDTOS.get(position).score);
        ((RowCell) holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("삭제");
            }
        });

    }

    @Override
    public int getItemCount() {

        //카운터
        return reviewDTOS.size();
    }

    //소스코드 절약해주는 부분
    private static class RowCell extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView text;
        RatingBar star;
        Button delete;

        RowCell(View view) {
            super(view);
            System.out.println("RowCell");
            name = view.findViewById(R.id.review_popup_item_name);
            date = view.findViewById(R.id.review_popup_item_date);
            text = view.findViewById(R.id.review_popup_item_text);
            star = view.findViewById(R.id.review_popup_item_star);
            delete = view.findViewById(R.id.review_popup_item_delete);


        }
    }

//
//    private OnItemClickListener mListener = null ;
//
//    public interface OnItemClickListener {
//        void onItemClick(View v, int position) ;
//    }
//
//    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mListener = listener ;
//    }

}

