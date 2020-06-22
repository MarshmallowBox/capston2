package com.example.capstone;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.naver.maps.map.CameraUpdate;

class UI {

    private Activity context;
    public BottomNavigationView bottomNavigationView;
    public static RadioGroup radioGroup;
    public static Fragment mapsFrag;
    Fragment placeListFrag;
    Fragment myPlaceFrag;
    Fragment moneyFrag;

    UI(Activity context) {
        this.context = context;
    }

    void createCategory() {
        String[] categoryTitle = {"전체", "일반휴게음식-일반한식","음식","의료","약국","보건","숙박"};

        radioGroup = context.findViewById(R.id.radiogroup);
        radioGroup.setPadding(12, 12, 12, 12);

        for (int i = 0; i < categoryTitle.length; i++) {
            final RadioButton radioButton = new RadioButton(context);

            //radioButton 레이아웃 디자인
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(12, 12, 12, 12);
            radioButton.setLayoutParams(lp);

            //배경 이미지 또는 xml파일
            radioButton.setBackgroundResource(R.drawable.checkbox_round_bg);
            //아이콘과 텍스트 간격
//            radioButton.setCompoundDrawablePadding(12);
            //라디오버튼 아이콘
            radioButton.setButtonDrawable(R.drawable.baseline_sentiment_satisfied_black_18dp);
            //텍스트
            radioButton.setText(categoryTitle[i]);
            //아이디설정
            radioButton.setId(i);

            //그룹에 추가
            radioGroup.addView(radioButton);
        }

        //첫번째 라디오버튼(전체 카테고리) 셋팅
        RadioButton firstBitton = (RadioButton) radioGroup.getChildAt(0);
        //체크상태로 만들기
        firstBitton.setChecked(true);
        //아이콘 삽입
        firstBitton.setButtonDrawable(R.drawable.baseline_sentiment_very_satisfied_black_18dp);

        //전체 라디오버튼에 대한 이벤트
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(i);
                    //체크한 버튼아이디와 각버튼아이디 비교
                    if (checkedId == btn.getId()) {
                        //일치하면 체크활성화
                        btn.setButtonDrawable(R.drawable.baseline_sentiment_very_satisfied_black_18dp);
                        Toast.makeText(context, btn.getText() + " 선택", Toast.LENGTH_SHORT).show();


                    } else {
                        //일치하지않으면 해제상태로 변경
                        btn.setButtonDrawable(R.drawable.baseline_sentiment_satisfied_black_18dp);
                    }
                }

                Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.naverMap.getCameraPosition().target));

            }
        });
    }

    void createNav_Bottom() {

        final HorizontalScrollView horizontalScrollView = context.findViewById(R.id.scrollview);
//

        mapsFrag = new Maps();
        MainActivity.fragmentManager.beginTransaction().replace(R.id.Main_Frame, mapsFrag).commit();
        placeListFrag = new PlaceList();
        MainActivity.fragmentManager.beginTransaction().add(R.id.Main_Frame, placeListFrag).commit();
        MainActivity.fragmentManager.beginTransaction().hide(placeListFrag).commit();

//
        bottomNavigationView = context.findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                //fragmentManager 오류잡아서 메인거 안불러오고 이클래스에서 호출할수있도록 변경

                switch (item.getItemId()) {
                    case R.id.mapmode:
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        if (mapsFrag == null) {
                            mapsFrag = new Maps();
                            mapsFrag.setRetainInstance(true);
                            MainActivity.fragmentManager.beginTransaction().add(R.id.Main_Frame, mapsFrag).commit();
                        }
                        if (mapsFrag != null)
                            MainActivity.fragmentManager.beginTransaction().show(mapsFrag).commit();
                        if (placeListFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.list:
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        if (placeListFrag == null) {
                            placeListFrag = new PlaceList();
                            MainActivity.fragmentManager.beginTransaction().add(R.id.Main_Frame, placeListFrag).commit();
                        }
                        if (mapsFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            MainActivity.fragmentManager.beginTransaction().show(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.myplaces:
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        if (myPlaceFrag == null) {
                            myPlaceFrag = new MyPlace();
                            MainActivity.fragmentManager.beginTransaction().add(R.id.Main_Frame, myPlaceFrag).commit();
                        }
                        if (mapsFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            MainActivity.fragmentManager.beginTransaction().show(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.edit:
                        horizontalScrollView.setVisibility(View.INVISIBLE);
                        if (moneyFrag == null) {
                            moneyFrag = new Money();
                            MainActivity.fragmentManager.beginTransaction().add(R.id.Main_Frame, moneyFrag).commit();
                        }
                        if (mapsFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            MainActivity.fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            MainActivity.fragmentManager.beginTransaction().show(moneyFrag).commit();
                        break;
                }
                return true;
            }
        });

    }


    void createNav_Drawer(String Name, String Email) {
        NavigationView navigationView = context.findViewById(R.id.nav_view);
//        TextView tv_userId = (TextView) context.findViewById(R.id.user_id);
//        tv_userId.setText(name);
        // xml 파일에서 넣어놨던 header 선언
        // header에 있는 리소스 가져오기
        //로그인시 유저의 아이디값 로그인인텐트에서 받아와 이름변경
        final TextView textView = navigationView.getHeaderView(0).findViewById(R.id.user_id);
        textView.setText(Name);
        final TextView textView1 = navigationView.getHeaderView(0).findViewById(R.id.user_info);
        textView1.setText(Email);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.login:
                        Toast.makeText(context, "로그인", Toast.LENGTH_SHORT).show();
                        show();
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("회원정보");
                        builder.setMessage("회원정보를 보시겠습니까?");
                        builder.setPositiveButton("예",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.putExtra("logout_user", textView.getText().toString() + "님이 로그아웃 하셨습니다.");
                                        context.setResult(LoginActivity.LOG_OUT_FLAG = 1, intent);
                                        context.finish();
                                    }
                                });
                        builder.setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                                    }
                                });
                        builder.show();

                        break;
                    case R.id.user_info:
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                        LayoutInflater inflater = context.getLayoutInflater();
//                        View view1 = inflater.inflate(R.layout.user_data,null);
//                        builder1.setView(view1);
//                        final AlertDialog dialog1 = builder1.create();
//                        dialog1.show();

                        break;
                    case R.id.info:
                        Toast.makeText(context, "도움말", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.preferences:
                        Toast.makeText(context, "설정", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.button_test, null);
        builder.setView(view);
//        final Button submit = (Button) view.findViewById(R.id.login_button);
//        final EditText email = (EditText) view.findViewById(R.id.edittextEmailAddress);
//        final EditText password = (EditText) view.findViewById(R.id.edittextPassword);

        final AlertDialog dialog = builder.create();
//        submit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                String strEmail = email.getText().toString();
////                String strPassword = password.getText().toString();
//                Toast.makeText(context.getApplicationContext(), "옴뇸뇸뇸뇸",Toast.LENGTH_LONG).show();
//
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }


}
