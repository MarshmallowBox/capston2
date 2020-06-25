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

import java.util.Objects;

public class MyPlace extends Fragment // Fragment 클래스를 상속받아야한다
{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_myplace,container,false);
        System.out.println("찜목록보여주기");


        RecyclerView mRecyclerView = view.findViewById(R.id.myplace_recyclerview);

        DbCon.Zzim Zzim = new DbCon.Zzim(Objects.requireNonNull(container).getContext(),mRecyclerView);
        Zzim.execute("1","","call");//Zzim.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");

        return view;
    }
}