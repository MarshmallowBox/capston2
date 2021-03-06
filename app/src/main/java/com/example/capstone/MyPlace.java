package com.example.capstone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class MyPlace extends Fragment // Fragment 클래스를 상속받아야한다
{
    public static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static DbCon.MyPlaceAdapter myPlaceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.detach(this).attach(this).commit();

        View view = inflater.inflate(R.layout.activity_myplace, container, false);
        recyclerView = view.findViewById(R.id.myplace_recyclerview);
        System.out.println("찜목록보여주기");

        if (myPlaceAdapter != null) {
            myPlaceAdapter.cancel(true);
            myPlaceAdapter = null;
        }
        myPlaceAdapter = new DbCon.MyPlaceAdapter(Objects.requireNonNull(container).getContext(), recyclerView);
        myPlaceAdapter.execute(String.valueOf(DbCon.Members.get(0).member_id), "0", "call");//Zzim.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");

        return view;
    }
}