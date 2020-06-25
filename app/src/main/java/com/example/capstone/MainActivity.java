package com.example.capstone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.naver.maps.map.CameraUpdate;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static String strNickname, strEmail;
    @SuppressLint("StaticFieldLeak")
    public static TextView user_city;
    @SuppressLint("StaticFieldLeak")
    public static RadioGroup radioGroup;
    public static Fragment mapsFrag;
    public static BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager;
    Fragment placeListFrag;
    Fragment myPlaceFrag;
    Fragment moneyFrag;
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    public static DbCon.Search Search;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //앱바(툴바) 생성부분인데 밑에 이벤트와같이 UI클래스에 같이 합칠수있는지 시도해보기
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.main);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_black_18dp); //뒤로가기 버튼 이미지 지정

        //이거 UI로 옮기는거 찾으면 더깔끔쓰~
        //하단바클릭이벤트를 위한 프레그먼트 생성

        //사용자 데이터
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        strNickname = Objects.requireNonNull(intent.getExtras()).getString("name");
        strEmail = intent.getExtras().getString("Email");


        //UI
        String[] categoryTitle = {"전체", "음식","카페","유통","의료","유흥","헬스","미용","학원","의류","기타"};

        radioGroup = findViewById(R.id.radiogroup);
        radioGroup.setPadding(12, 12, 12, 12);

        for (int i = 0; i < categoryTitle.length; i++) {
            final RadioButton radioButton = new RadioButton(this);

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
                        Toast.makeText(MainActivity.this, btn.getText() + " 선택", Toast.LENGTH_SHORT).show();


                    } else {
                        //일치하지않으면 해제상태로 변경
                        btn.setButtonDrawable(R.drawable.baseline_sentiment_satisfied_black_18dp);
                    }
                }
                if (bottomNavigationView.getSelectedItemId() == R.id.mapmode) {
                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.naverMap.getCameraPosition().target));
                } else {
                    if (PlaceList.isCheckedButtonNear) {
                        PlaceList.button_near.performClick();
                    } else {
                        PlaceList.button_camera.performClick();
                    }
                }

            }
        });


        final HorizontalScrollView horizontalScrollView = findViewById(R.id.scrollview);
//

        mapsFrag = new Maps();
        fragmentManager.beginTransaction().replace(R.id.Main_Frame, mapsFrag).commit();
        placeListFrag = new PlaceList();
        fragmentManager.beginTransaction().add(R.id.Main_Frame, placeListFrag).commit();
        fragmentManager.beginTransaction().hide(placeListFrag).commit();

//
        bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.mapmode:
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        if (Maps.mLayout != null &&
                                (Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                            Maps.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                        Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.naverMap.getCameraPosition().target));
                        if (mapsFrag == null) {
                            mapsFrag = new Maps();
                            mapsFrag.setRetainInstance(true);
                            fragmentManager.beginTransaction().add(R.id.Main_Frame, mapsFrag).commit();
                        }
                        if (mapsFrag != null)
                            fragmentManager.beginTransaction().show(mapsFrag).commit();
                        if (placeListFrag != null)
                            fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.list:
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        if (PlaceList.isCheckedButtonNear) {
                            PlaceList.button_near.performClick();
                        } else {
                            PlaceList.button_camera.performClick();
                        }
                        if (placeListFrag == null) {
                            placeListFrag = new PlaceList();
                            fragmentManager.beginTransaction().add(R.id.Main_Frame, placeListFrag).commit();
                        }
                        if (mapsFrag != null)
                            fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            fragmentManager.beginTransaction().show(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.myplaces:
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        if (myPlaceFrag == null) {
                            myPlaceFrag = new MyPlace();
                            fragmentManager.beginTransaction().add(R.id.Main_Frame, myPlaceFrag).commit();
                        }
                        if (mapsFrag != null)
                            fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            fragmentManager.beginTransaction().show(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            fragmentManager.beginTransaction().hide(moneyFrag).commit();
                        break;
                    case R.id.edit:
                        searchView.setIconified(true);
                        searchView.clearFocus();
                        horizontalScrollView.setVisibility(View.INVISIBLE);
                        if (moneyFrag == null) {
                            moneyFrag = new Money();
                            fragmentManager.beginTransaction().add(R.id.Main_Frame, moneyFrag).commit();
                        }
                        if (mapsFrag != null)
                            fragmentManager.beginTransaction().hide(mapsFrag).commit();
                        if (placeListFrag != null)
                            fragmentManager.beginTransaction().hide(placeListFrag).commit();
                        if (myPlaceFrag != null)
                            fragmentManager.beginTransaction().hide(myPlaceFrag).commit();
                        if (moneyFrag != null)
                            fragmentManager.beginTransaction().show(moneyFrag).commit();
                        break;
                }
                return true;
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
//        TextView tv_userId = (TextView) context.findViewById(R.id.user_id);
//        tv_userId.setText(name);
        // xml 파일에서 넣어놨던 header 선언
        // header에 있는 리소스 가져오기
        //로그인시 유저의 아이디값 로그인인텐트에서 받아와 이름변경
        final TextView textView = navigationView.getHeaderView(0).findViewById(R.id.user_id);
        textView.setText(strNickname);
        final TextView textView1 = navigationView.getHeaderView(0).findViewById(R.id.user_info);
        textView1.setText(strEmail);
        final TextView user_money = navigationView.getHeaderView(0).findViewById(R.id.user_money);
        user_money.setText("100,000");
        MainActivity.user_city = navigationView.getHeaderView(0).findViewById(R.id.user_city);
        MainActivity.user_city.setText("화성시");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.login:
                        Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.button_test, null);
                        alertDialog.setView(view);
//        final Button submit = (Button) view.findViewById(R.id.login_button);
//        final EditText email = (EditText) view.findViewById(R.id.edittextEmailAddress);
//        final EditText password = (EditText) view.findViewById(R.id.edittextPassword);

                        final AlertDialog dialog = alertDialog.create();
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
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("회원정보");
                        builder.setMessage("회원정보를 보시겠습니까?");
                        builder.setPositiveButton("예",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.putExtra("logout_user", textView.getText().toString() + "님이 로그아웃 하셨습니다.");
                                        setResult(LoginActivity.LOG_OUT_FLAG = 1, intent);
                                        finish();
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
                    case R.id.information:
                        Intent information = new Intent(MainActivity.this, HelpPopupActivity.class);
                        startActivity(information);

                        break;
                    case R.id.setting:
                        Intent setting = new Intent(MainActivity.this, SettingPopupActivity.class);
                        startActivity(setting);
                        break;
                }
                return true;
            }
        });


//        Toast.makeText(this, "이름 : " + strNickname + "이메일 : " + strEmail, Toast.LENGTH_SHORT).show();


        //권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String temp = ""; //파일 읽기 권한 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                temp += Manifest.permission.ACCESS_COARSE_LOCATION + " ";
            } //파일 쓰기 권한 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                temp += Manifest.permission.ACCESS_FINE_LOCATION + " ";
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                temp += Manifest.permission.RECEIVE_SMS + " ";
            }
            if (!TextUtils.isEmpty(temp)) { // 권한 요청
                ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
            } else { // 모두 허용 상태
                Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
            }
        }


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 왼쪽 상단 버튼 눌렀을 때
//                Toast.makeText(MainActivity.this, "햄버거", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
            case R.id.search_button:
//                Toast.makeText(MainActivity.this, item.getItemId() + ": search_button", Toast.LENGTH_SHORT).show();

                return true;

            default:
//                Toast.makeText(MainActivity.this, item + ":default", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_item, menu);
        final MenuItem mSearch = menu.findItem(R.id.search_button);
        searchView = (SearchView) mSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println(s);

                if (Search != null) {
                    Search.cancel(true);
                    Search = null;
                }
                Search = new DbCon.Search(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers,PlaceList.container.getContext(),PlaceList.recyclerView);
                if (Search != null) {
                    Search.execute(s,"1");//인자로 city 스트링 보내면 해당 도시만 출력가능
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //입력할때마다 이벤트
                //여기다 자동완성 넣으면 갸꿀
                System.out.println(s);

                if (Search != null) {
                    Search.cancel(true);
                    Search = null;
                }
                Search = new DbCon.Search(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers,PlaceList.container.getContext(),PlaceList.recyclerView);
                if (Search != null) {
                    Search.execute(s,"2");//인자로 city 스트링 보내면 해당 도시만 출력가능
                }

                return false;
            }
        });
        mSearch.setChecked(true);
//메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //확장시
                searchView.setIconified(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //축소시


                searchView.setIconified(true);
                searchView.clearFocus();

                return true;
            }
        });
        return true;
    }

}