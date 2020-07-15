package com.example.capstone;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;

public class InfoWindowAdapter extends InfoWindow.ViewAdapter {
    @NonNull
    private final Context context;
    private View rootView;
    private TextView name;
    private TextView address;

    public InfoWindowAdapter(@NonNull Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(@NonNull InfoWindow infoWindow) {

        if (rootView == null) {
            rootView = View.inflate(context, R.layout.view_custom_info_window, null);
            name = rootView.findViewById(R.id.view_custom_info_window_title);
            address = rootView.findViewById(R.id.view_custom_info_window_category);
        }

        if (infoWindow.getMarker() != null) {
            FranchiseDTO tags = (FranchiseDTO) infoWindow.getMarker().getTag();
            assert tags != null;
            name.setText(tags.name);
            address.setText(tags.address);
        }
        return rootView;
    }
}