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


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FranchiseDTO> memberDTOs = new ArrayList<>();

    RecyclerViewAdapter(ArrayList<FranchiseDTO> Franchises) {
        memberDTOs.addAll(Franchises);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FranchiseDTO franchiseDTO = null;

                for (FranchiseDTO franchise : memberDTOs) {
                    if (((TextView) v.findViewById(R.id.recyclerview_item_name)).getText().equals(franchise.name)) {
                        franchiseDTO = franchise;
                        break;
                    }
                }

                if (franchiseDTO != null) {
                    Maps.singleMarkers.setMap(null);
                    Maps.singleMarkers = new Marker();
                    Maps.singleMarkers.setPosition(new LatLng(franchiseDTO.latitude, franchiseDTO.longitude));//위경도
                    Maps.singleMarkers.setIcon(MarkerIcons.RED);//기본제공 마커
                    //마커 크기지정 아마 3:4비율인듯
//                        marker.setWidth(90);
//                        marker.setHeight(120);
                    Maps.singleMarkers.setCaptionText(franchiseDTO.name); //메인캡션
                    Maps.singleMarkers.setTag(franchiseDTO);//인포뷰에 전달할 태그값
                    Maps.singleMarkers.setSubCaptionText(franchiseDTO.category); //서브캡션
                    Maps.singleMarkers.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                    Maps.singleMarkers.setSubCaptionTextSize(10); //서브캡션 크기
                    Maps.singleMarkers.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기

                    final FranchiseDTO finalFranchiseDTO1 = franchiseDTO;
                    Maps.singleMarkers.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {

                            //클릭시 카메라 이동
                            Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarkers.getPosition()).animate(CameraAnimation.Easing));
                            //infoWindow에 franchises값 태그로 전달
                            Maps.infoWindow.setTag(finalFranchiseDTO1);
                            //인포뷰 활성화
                            Maps.infoWindow.open(Maps.singleMarkers);
                            Maps.infoWindow.performClick();
                            return true;
                        }
                    });

                    Maps.singleMarkers.setMap(Maps.naverMap); //지도에 추가, null이면 안보임
                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarkers.getPosition()));
                    Maps.singleMarkers.performClick();
//하단 정보창 닫기
                    if (Maps.mLayout != null &&
                            (Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                        Maps.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
            }

        });
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
//        if (isNear) {
//            System.out.println("aa");
//            ((RowCell) holder).imageView.setImageResource(R.drawable.appicon64);
//            ((RowCell) holder).name.setText(DbClass.nearFranchises.get(position).name);
//            ((RowCell) holder).address.setText(DbClass.nearFranchises.get(position).address);
//            ((RowCell) holder).category.setText(DbClass.nearFranchises.get(position).category);
//            ((RowCell) holder).tel.setText(DbClass.nearFranchises.get(position).tel);
//        } else {
//            System.out.println("bb");
//
//            ((RowCell) holder).imageView.setImageResource(R.drawable.appicon64);
//            ((RowCell) holder).name.setText(DbClass.Franchises.get(position).name);
//            ((RowCell) holder).address.setText(DbClass.Franchises.get(position).address);
//            ((RowCell) holder).category.setText(DbClass.Franchises.get(position).category);
//            ((RowCell) holder).tel.setText(DbClass.Franchises.get(position).tel);
//        }


    }

    @Override
    public int getItemCount() {

        //카운터
        return memberDTOs.size();
    }

    //소스코드 절약해주는 부분
    private static class RowCell extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        TextView address;
        TextView category;
        TextView tel;

        RowCell(View view) {
            super(view);
            imageView = view.findViewById(R.id.recyclerview_image);
            name = view.findViewById(R.id.recyclerview_item_name);
            address = view.findViewById(R.id.recyclerview_item_address);
            category = view.findViewById(R.id.recyclerview_item_category);
            tel = view.findViewById(R.id.recyclerview_item_tel);
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

