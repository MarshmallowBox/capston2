package com.example.capstone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class PlaceList extends Fragment
{
    public static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static ViewGroup container;
    @SuppressLint("StaticFieldLeak")
    public static Button button_near;
    @SuppressLint("StaticFieldLeak")
    public static Button button_camera;
    //이거 버튼에서 라디오버튼같은 선택지버튼으로 변경해서 없애버리기기
    public static boolean isCheckedButtonNear = true;
    @SuppressLint("StaticFieldLeak")
    public static DbCon.MarkerAdapter MarkerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        PlaceList.container = container;
        View view = inflater.inflate(R.layout.activity_placelist, container, false);
        recyclerView = view.findViewById(R.id.placelist_recyclerview);

        button_near = view.findViewById(R.id.button_near);
        button_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckedButtonNear = true;
                button_near.setBackgroundResource(R.drawable.selected_button);
//                button_camera.setBackgroundColor(Color.parseColor("#4000C0FF"));
                button_camera.setBackgroundResource(R.drawable.border);


                if (MarkerAdapter != null) {
                    MarkerAdapter.cancel(true);
                    MarkerAdapter = null;
                }
                MarkerAdapter = new DbCon.MarkerAdapter(Objects.requireNonNull(container).getContext(), recyclerView);
                MarkerAdapter.execute(String.valueOf(Maps.beforeLocationPoint.getLatitude()), String.valueOf(Maps.beforeLocationPoint.getLongitude()), String.valueOf(MainActivity.user_city.getText()));


            }
        });
        button_camera = view.findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckedButtonNear = false;
                button_near.setBackgroundResource(R.drawable.border);
                button_camera.setBackgroundResource(R.drawable.selected_button);

                if (MarkerAdapter != null) {
                    MarkerAdapter.cancel(true);
                    MarkerAdapter = null;
                }
                MarkerAdapter = new DbCon.MarkerAdapter(Objects.requireNonNull(container).getContext(), recyclerView);
                MarkerAdapter.execute(String.valueOf(Maps.beforeCameraPoint.latitude), String.valueOf(Maps.beforeCameraPoint.longitude), String.valueOf(MainActivity.user_city.getText()));


            }
        });
        return view;
    }
}
