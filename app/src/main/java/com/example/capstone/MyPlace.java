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
        System.out.println("찜목록보여주기");

   //     RecyclerView mRecyclerView = view.findViewById(R.id.myplace_recyclerview);
     //   FranchiseRecyclerViewAdapter mAdapter = new FranchiseRecyclerViewAdapter(DbCon.Zzim.DBString);
       // mRecyclerView.setAdapter(mAdapter);
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        //mAdapter.notifyDataSetChanged();//데이터변경시


        return view;
    }
}