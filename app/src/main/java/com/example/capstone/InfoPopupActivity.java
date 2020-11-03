package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class InfoPopupActivity extends Activity {

    public static CheckBox star;
    Button map;
    Button call;
    Button road;
    Button review;
    Button addReview;

    Intent intent;
    public static int franchiseID;
    String name;
    String address;
    String category;
    String tel;
    double latitude;
    double longitude;
    DbCon.MyPlaceAdapter MyPlaceAdapter;
    public static TextView reviewCounter;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_popup);


        //데이터 가져오기
        intent = getIntent();
        franchiseID = intent.getExtras().getInt("id");
        name = intent.getExtras().getString("name");
        address = intent.getExtras().getString("address");
        category = intent.getExtras().getString("category");
        tel = intent.getExtras().getString("tel");
        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");

        DbCon.ReviewAdapter Review = new DbCon.ReviewAdapter();
        Review.execute(String.valueOf(franchiseID),"1",String.valueOf(DbCon.Members.get(0).member_id),"1","1","1");//excute해서 dbcon에있는 rowcount ( 기본값은 0) 이놈을 리뷰개수만큼 rowcount++해서 리뷰개수 가져오려고함


        ((TextView) findViewById(R.id.info_popup_title)).setText(name + "의 상세정보");
        ((TextView) findViewById(R.id.info_popup_name)).setText(name);
        ((TextView) findViewById(R.id.info_popup_address)).setText(address);
        ((TextView) findViewById(R.id.info_popup_category)).setText(category);
        ((TextView) findViewById(R.id.info_popup_tell)).setText(tel.equals("") ? "전화번호 없음" : tel);
        reviewCounter=findViewById(R.id.info_popup_review);
        final FranchiseDTO franchiseDTO = new FranchiseDTO(franchiseID, name, address, category, tel, latitude, longitude);


        star = findViewById(R.id.info_popup_star);
        //만약 해당가게id와 유저id가 이미 likes table에 들어가있다면 true 없다면 false
        if (MyPlaceAdapter != null) {
            MyPlaceAdapter.cancel(true);
            MyPlaceAdapter = null;
        }
        MyPlaceAdapter = new DbCon.MyPlaceAdapter();
        if (MyPlaceAdapter != null) {

            MyPlaceAdapter.execute(String.valueOf(DbCon.Members.get(0).member_id),String.valueOf(franchiseID),"call");//MyPlaceAdapter.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");
        }
        System.out.println("%$%$");
        System.out.println("%$%$");
        System.out.println("%$%$");
//        System.out.println(DbCon.MyPlaceAdapter.MyPlaceAdapterFranchise);
        System.out.println("%$%$");
        System.out.println("%$%$");
        System.out.println("%$%$");


        star.invalidate();
//            if(DbCon.DBString.isEmpty()){
//                System.out.println("하얀별");
//                star.setChecked(false);
//            }else{
//                System.out.println("검은별");
//                star.setChecked(true);
//            }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.bottomNavigationView.getSelectedItemId() == R.id.myplaces){
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.myplaces);
                }

                if(star.isChecked()){
                    System.out.println("체크되었음");

                    if (MyPlaceAdapter != null) {
                        MyPlaceAdapter.cancel(true);
                        MyPlaceAdapter = null;
                    }
                    MyPlaceAdapter = new DbCon.MyPlaceAdapter();
                    if (MyPlaceAdapter != null) {
                        MyPlaceAdapter.execute(String.valueOf(DbCon.Members.get(0).member_id),String.valueOf(franchiseID),"add");//MyPlaceAdapter.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");
                    }
                    System.out.println("찜목록추가됨");
                }else{
                    System.out.println("체크안되었음");

                    if (MyPlaceAdapter != null) {
                        MyPlaceAdapter.cancel(true);
                        MyPlaceAdapter = null;
                    }
                    MyPlaceAdapter = new DbCon.MyPlaceAdapter();
                    if (MyPlaceAdapter != null) {
                        MyPlaceAdapter.execute(String.valueOf(DbCon.Members.get(0).member_id),String.valueOf(franchiseID),"del");//MyPlaceAdapter.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");
                    }
                    System.out.println("찜목록삭제됨");
                }
            }
        });
        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    star.setButtonDrawable(R.drawable.baseline_star_black_36dp);
                }else {
                    star.setButtonDrawable(R.drawable.baseline_star_border_black_36dp);
                }
        }});

//        star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (star.isChecked()) {
//                    star.setButtonDrawable(R.drawable.baseline_star_black_36dp);
//                    DbCon.MyPlaceAdapter MyPlaceAdapter = new DbCon.MyPlaceAdapter();
//                    MyPlaceAdapter.execute("1",String.valueOf(franchiseID),"add");//MyPlaceAdapter.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");
//                    System.out.println("찜목록추가됨");
//
//                } else {
//                    star.setButtonDrawable(R.drawable.baseline_star_border_black_36dp);
//                    DbCon.MyPlaceAdapter MyPlaceAdapter = new DbCon.MyPlaceAdapter();
//                    MyPlaceAdapter.execute("1",String.valueOf(franchiseID),"del");//MyPlaceAdapter.execute("멤버ID","스토어ID","기능(추가:1,삭제:2)");
//                    System.out.println("찜목록삭제됨");
//                }
//
//            }
//        });
        map = findViewById(R.id.info_popup_map);

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.mapmode);
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

                    Maps.singleMarker.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
                            //클릭시 카메라 이동
                            Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarker.getPosition()).animate(CameraAnimation.Easing));
                            //infoWindow에 franchises값 태그로 전달
                            Maps.infoWindow.setTag(franchiseDTO);
                            //인포뷰 활성화
//                            Maps.infoWindow.open(Maps.singleMarker);
                            Maps.infoWindow.performClick();
                            return true;
                        }
                    });

                    Maps.singleMarker.setMap(Maps.naverMap); //지도에 추가, null이면 안보임
                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarker.getPosition()));
//                    Maps.singleMarker.performClick();
//하단 정보창 닫기
                    if (Maps.slidingUpPanel != null &&
                            (Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                        Maps.slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                    finish();
                }
            });



        call = findViewById(R.id.info_popup_call);
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

        road = findViewById(R.id.info_popup_road);
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] kakaomapParameters = new String[]{"CAR", "PUBLICTRANSIT", "FOOT", "BICYCLE"};
                String url = "kakaomap://route?sp=" + Maps.beforeLocationPoint.getLatitude() + "," + Maps.beforeLocationPoint.getLongitude() + "&ep=" + franchiseDTO.latitude + "," + franchiseDTO.longitude + "&by=" + kakaomapParameters[0];
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
        review = findViewById(R.id.info_popup_see_review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addReviewIntent = new Intent(InfoPopupActivity.this, ReviewPopupActivity.class);
                addReviewIntent.putExtra("id", franchiseID);
                addReviewIntent.putExtra("name",name);
                addReviewIntent.putExtra("address",address);
                addReviewIntent.putExtra("category",category);
                addReviewIntent.putExtra("tel",tel);
                addReviewIntent.putExtra("latitude",latitude);
                addReviewIntent.putExtra("longitude",longitude);

                addReviewIntent.putExtra("mode","Review_popup_open");
                startActivity(addReviewIntent);

                //액티비티(팝업) 닫기
                finish();
            }
        });
        addReview = findViewById(R.id.info_popup_add_review);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addReviewIntent = new Intent(InfoPopupActivity.this, ReviewPopupActivity.class);
                addReviewIntent.putExtra("id", franchiseID);
                addReviewIntent.putExtra("name",name);
                addReviewIntent.putExtra("address",address);
                addReviewIntent.putExtra("category",category);
                addReviewIntent.putExtra("tel",tel);
                addReviewIntent.putExtra("latitude",latitude);
                addReviewIntent.putExtra("longitude",longitude);

                addReviewIntent.putExtra("mode","addReview_popup_open");
                startActivity(addReviewIntent);

                //액티비티(팝업) 닫기
                finish();
            }
        });
        if(MainActivity.strNickname.equals("비회원")){
            addReview.setVisibility(View.INVISIBLE);
            star.setVisibility(View.INVISIBLE);
        }
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