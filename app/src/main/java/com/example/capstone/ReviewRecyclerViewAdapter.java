package com.example.capstone;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FranchiseDTO> memberDTOs = new ArrayList<>();

    ReviewRecyclerViewAdapter(ArrayList<FranchiseDTO> Franchises) {
        memberDTOs.addAll(Franchises);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);


        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분

        ((RowCell) holder).imageView.setImageResource(R.drawable.appicon64);
        ((RowCell) holder).name.setText(memberDTOs.get(position).name);
        ((RowCell) holder).address.setText(memberDTOs.get(position).address);
        ((RowCell) holder).category.setText(memberDTOs.get(position).category);
        ((RowCell) holder).tel.setText(memberDTOs.get(position).tel);

    }

    @Override
    public int getItemCount() {

        //카운터
        return memberDTOs.size();
    }

    //소스코드 절약해주는 부분
    private static class RowCell extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView text;
        TextView tel;

        RowCell(View view) {
            super(view);
            name = view.findViewById(R.id.review_popup_item_name);
            date = view.findViewById(R.id.review_popup_item_date);
            text = view.findViewById(R.id.review_popup_item_text);
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

