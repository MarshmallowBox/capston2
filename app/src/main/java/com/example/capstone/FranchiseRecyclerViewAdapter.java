package com.example.capstone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


public class FranchiseRecyclerViewAdapter extends RecyclerView.Adapter<FranchiseRecyclerViewAdapter.ViewHolder> {

    private ArrayList<FranchiseDTO> franchisesDTOs = null;

    FranchiseRecyclerViewAdapter(ArrayList<FranchiseDTO> Franchises) {
        franchisesDTOs = Franchises;
    }


    @NonNull
    @Override
    public FranchiseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        FranchiseRecyclerViewAdapter.ViewHolder vh = new FranchiseRecyclerViewAdapter.ViewHolder(view);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                FranchiseDTO franchiseDTO = null;

                for (FranchiseDTO franchise : franchisesDTOs) {
                    if (((TextView) v.findViewById(R.id.recyclerview_item_name)).getText().equals(franchise.name)) {
                        franchiseDTO = franchise;
                        break;
                    }
                }

                if (franchiseDTO != null) {
                    Maps.singleMarker.setMap(null);
                    Maps.singleMarker = new Marker();
                    Maps.singleMarker.setPosition(new LatLng(franchiseDTO.latitude, franchiseDTO.longitude));//위경도

                    Maps.singleMarker.setIcon(OverlayImage.fromResource(R.drawable.search_marker));//기본제공 마커
                    Maps.singleMarker.setWidth(100);
                    Maps.singleMarker.setHeight(125);
                    Maps.singleMarker.setCaptionOffset(0); //캡션 위치
                    Maps.singleMarker.setAnchor((new PointF(0.5f, 1)));
                    Maps.singleMarker.setCaptionTextSize(10);

                    Maps.singleMarker.setCaptionText(franchiseDTO.name); //메인캡션
                    Maps.singleMarker.setTag(franchiseDTO);//인포뷰에 전달할 태그값
                    Maps.singleMarker.setSubCaptionText(franchiseDTO.category); //서브캡션
                    Maps.singleMarker.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                    Maps.singleMarker.setSubCaptionTextSize(10); //서브캡션 크기
                    Maps.singleMarker.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기

                    final FranchiseDTO finalFranchiseDTO1 = franchiseDTO;
                    Maps.singleMarker.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {

                            //클릭시 카메라 이동
//                            Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarker.getPosition()).animate(CameraAnimation.Easing));
                            //infoWindow에 franchises값 태그로 전달
                            Maps.infoWindow.setTag(finalFranchiseDTO1);
                            //인포뷰 활성화
//                            Maps.infoWindow.open(Maps.singleMarker);
                            Maps.infoWindow.performClick();
                            return true;
                        }
                    });

//                    Maps.singleMarker.setMap(Maps.naverMap); //지도에 추가, null이면 안보임
                    Maps.singleMarker.setMap(null); //지도에 추가, null이면 안보임
//                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarker.getPosition()));
                    Maps.singleMarker.performClick();
//하단 정보창 닫기
                    if (Maps.slidingUpPanel != null &&
                            (Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                        Maps.slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
            }

        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FranchiseRecyclerViewAdapter.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분

        holder.name.setText(franchisesDTOs.get(position).name);
        holder.address.setText(franchisesDTOs.get(position).address);
        holder.category.setText(franchisesDTOs.get(position).category);
        holder.tel.setText(franchisesDTOs.get(position).tel.equals("") ? "전화번호 없음" : franchisesDTOs.get(position).tel);


    }

    @Override
    public int getItemCount() {
        //카운터
        return franchisesDTOs.size();
    }

    //소스코드 절약해주는 부분
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView category;
        TextView tel;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.recyclerview_item_name);
            address = view.findViewById(R.id.recyclerview_item_address);
            category = view.findViewById(R.id.recyclerview_item_category);
            tel = view.findViewById(R.id.recyclerview_item_tel);
        }
    }

}

