package com.example.capstone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPlace extends Fragment // Fragment 클래스를 상속받아야한다
{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_myplace,container,false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myplace_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext()); //리스트뷰를 띄워준다
        RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(DbClass.nearFranchises);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        return view;
    }
}