package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Objects;

public class PopupActivity extends Activity {

    CheckBox star;
    Button map;
    Button call;
    Button load;
    Button seeReview;
    Button addReview;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);



        //데이터 가져오기
        Intent intent = getIntent();
        final ArrayList<String> data = new ArrayList<>(Objects.requireNonNull(intent.getStringArrayListExtra("data")));
        ((TextView)findViewById(R.id.popup_title)).setText(data.get(1) + "의 상세정보");
        ((TextView)findViewById(R.id.popup_name)).setText(data.get(1));
        ((TextView)findViewById(R.id.popup_address)).setText(data.get(2));
        ((TextView)findViewById(R.id.popup_category)).setText(data.get(3));
        ((TextView)findViewById(R.id.popup_tell)).setText(data.get(4).equals("")?"전화번호 없음":data.get(4));
        final FranchiseDTO franchiseDTO = new FranchiseDTO(Integer.parseInt(data.get(0)), data.get(1), data.get(2), data.get(3), data.get(4), Double.parseDouble(data.get(5)), Double.parseDouble(data.get(6)));
        ((TextView)findViewById(R.id.popup_review)).setText("리뷰: "+data.get(7)+"개");

    star = findViewById(R.id.popup_star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (star.isChecked()) {
                    star.setButtonDrawable(R.drawable.baseline_star_border_black_36dp);

                } else {
                    star.setButtonDrawable(R.drawable.baseline_star_black_36dp);
                }

            }
        });
        map = findViewById(R.id.popup_map);

        if (Integer.parseInt(data.get(8)) != R.id.mapmode){
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.ui.bottomNavigationView.setSelectedItemId(R.id.mapmode);
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

                Maps.singleMarkers.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        //클릭시 카메라 이동
                        Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarkers.getPosition()).animate(CameraAnimation.Easing));
                        //infoWindow에 franchises값 태그로 전달
                        Maps.infoWindow.setTag(franchiseDTO);
                        //인포뷰 활성화
                        Maps.infoWindow.open(Maps.singleMarkers);
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
                finish();
            }
        });
        }else{
            map.setTextColor(Color.parseColor("#FFFFFFFF"));
        }


        call = findViewById(R.id.popup_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (franchiseDTO.tel.equals("")) {
                    Toast.makeText(getApplicationContext(), "번호가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + franchiseDTO.tel));
                    startActivity(intent);
                }
            }
        });

        load = findViewById(R.id.popup_load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] kakaomapParameters = new String[]{"CAR", "PUBLICTRANSIT", "FOOT", "BICYCLE"};
                String url = "kakaomap://route?sp=" + Maps.beforeLocation.getLatitude() + "," + Maps.beforeLocation.getLongitude() + "&ep=" + franchiseDTO.latitude + "," + franchiseDTO.longitude + "&by=" + kakaomapParameters[0];
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    url = "market://details?id=net.daum.android.map";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    e.getStackTrace();
                }

               /* AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                builder2.setPositiveButton("T-Map",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "tmap://route?goalx=" + franchiseDTO.longitude + "&goaly=" + franchiseDTO.latitude + "&goalname=" + franchiseDTO.name + "";
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    url = "market://details?id=com.skt.tmap.ku";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                    e.getStackTrace();
                                }
                            }
                        }).setNegativeButton("Kakao Map",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "kakaomap://route?sp=" + Maps.beforeLocation.getLatitude() + "," + Maps.beforeLocation.getLongitude() + "&ep=" + franchiseDTO.latitude + "," + franchiseDTO.longitude + "&by=CAR";
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    url = "market://details?id=net.daum.android.map";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                    e.getStackTrace();
                                }
                            }
                        });
                builder2.show();*/

            }
        });
         seeReview = findViewById(R.id.popup_see_review);
         addReview = findViewById(R.id.popup_add_review);
    }

    /*//확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭시
        /*if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            finish();
            return true;
        }*/
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼
        finish();
    }
}
