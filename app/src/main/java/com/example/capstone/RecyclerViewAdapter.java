package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FranchiseDTO> memberDTOs = new ArrayList<>();


    public RecyclerViewAdapter(FranchiseDTO Franchise) {
        memberDTOs.add(Franchise);
    }

    public RecyclerViewAdapter(ArrayList<FranchiseDTO> Franchises) {
        for (FranchiseDTO Franchise:Franchises) {
            if(  (((RadioButton) UI.radioGroup.getChildAt(UI.radioGroup.getCheckedRadioButtonId())).getText().equals(Franchise.category)
                    || ((RadioButton) UI.radioGroup.getChildAt(UI.radioGroup.getCheckedRadioButtonId())).getText().equals("전체"))  ){
                memberDTOs.add(Franchise);
            }

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Marker searchMarker=null;
                for(Marker marker:DbClass.Markers){
                    if(((FranchiseDTO) marker.getTag()).name.equals( ((TextView)v.findViewById(R.id.recyclerview_item_name)).getText())){
                        searchMarker = marker;
                        break;
                    }
                }
                if(searchMarker!=null){
                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(searchMarker.getPosition()));
                    //인포뷰 활성화
                    DbClass.Markers.get(DbClass.Markers.indexOf(searchMarker)).setMap(Maps.naverMap);
                    DbClass.Markers.get(DbClass.Markers.indexOf(searchMarker)).performClick();

                    Maps.infoWindow.performClick();
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

        public RowCell(View view) {
            super(view);
            imageView = view.findViewById(R.id.recyclerview_image);
            name = (TextView) view.findViewById(R.id.recyclerview_item_name);
            address = (TextView) view.findViewById(R.id.recyclerview_item_address);
            category = (TextView) view.findViewById(R.id.recyclerview_item_category);
            tel = (TextView) view.findViewById(R.id.recyclerview_item_tel);
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
