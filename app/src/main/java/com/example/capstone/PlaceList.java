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

import java.util.ArrayList;

public class PlaceList extends Fragment // Fragment 클래스를 상속받아야한다
{
    public static RecyclerView recyclerView;
    public static ViewGroup container;
    public static Button button_near;
    public static Button button_camera;

    public static ArrayList<FranchiseDTO> Franchises = new ArrayList<>();
    //이거 버튼에서 라디오버튼같은 선택지버튼으로 변경해서 없애버리기기
    public static boolean isCheckedButtonNear = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        View view = inflater.inflate(R.layout.activity_placelist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.placelist_recyclerview);

        //이것도 하단바마냥 수정하기
        button_near = view.findViewById(R.id.button_near);
        button_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckedButtonNear = true;

                DataAdapter dataAdapter = new DataAdapter(container.getContext(),recyclerView);
                dataAdapter.execute(String.valueOf(Maps.beforeLocation.getLatitude()), String.valueOf(Maps.beforeLocation.getLongitude()));
            }
        });
        button_camera = view.findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckedButtonNear = false;
                DataAdapter dataAdapter = new DataAdapter(container.getContext(),recyclerView);
                dataAdapter.execute(String.valueOf(Maps.beforeCamera.latitude), String.valueOf(Maps.beforeCamera.longitude));
            }
        });
        return view;
    }
}
