package com.example.capstone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceList extends Fragment // Fragment 클래스를 상속받아야한다
{
    public static RecyclerView recyclerView;
    public static ViewGroup container;
    public static Button button_near;
    public static Button button_camera;

    //이거 버튼에서 라디오버튼같은 선택지버튼으로 변경해서 없애버리기기
    public static boolean button_near_check = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container=container;
        View view = inflater.inflate(R.layout.activity_placelist, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.placelist_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext()); //리스트뷰를 띄워준다
        RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(DbClass.nearFranchises);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRecyclerViewAdapter);


        //이것도 하단바마냥 수정하기
        button_near = view.findViewById(R.id.button_near);
        button_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_near_check=true;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext()); //리스트뷰를 띄워준다
                RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(DbClass.nearFranchises);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
            }
        });
        button_camera = view.findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_near_check=false;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext()); //리스트뷰를 띄워준다
                RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(DbClass.Franchises);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myRecyclerViewAdapter);
            }
        });


        return view;
    }
}