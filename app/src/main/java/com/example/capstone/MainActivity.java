package com.example.capstone;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.naver.maps.map.CameraUpdate;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;
    @SuppressLint("StaticFieldLeak")
    public static TextView user_city, user_money;
    @SuppressLint("StaticFieldLeak")
    public static RadioGroup radioGroup;
    public static Fragment mapsFrag;
    public static BottomNavigationView bottomNavigationView;
    public static int originmoney = 0;  //여기에 초기 가지고있는 돈 넣어놓으면 됨
    public static DbCon.Member Member;
    public static DrawerLayout drawerLayout;
    public static TextView textView;
    public static String user_id;
    public static TextView textView1;
    public static String strStartWithQRCode;
    public static boolean flag = false;
    public static boolean QRFlag = false;
    public FragmentManager fragmentManager;
    public FranchiseDTO franchiseDTO;
    Fragment placeListFrag;
    Fragment myPlaceFrag;
    Fragment moneyFrag;
    DbCon.Search Search;
    DbCon.DataAdapter dataAdapter;
    DbCon.Qr qr;
    private SearchView searchView;
    private IntentIntegrator qrScan;

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
        //이메일, 나잇대, 성별, 생일을 intent에서 가져와서 각 String에 저장함
        System.out.println("시작이다!!!");

        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        strNickname = Objects.requireNonNull(intent.getExtras()).getString("name");
        strEmail = intent.getStringExtra("email");
        strProfile = intent.getExtras().getString("profile");
        strAgeRange = intent.getExtras().getString("ageRange");
        strGender = intent.getExtras().getString("gender");
        strBirthday = intent.getStringExtra("birthday");    //같은 함수인가봐
        strEmail = intent.getExtras().getString("email");
        strStartWithQRCode = intent.getExtras().getString("startWithQRCode");


        NavigationView navigationView = findViewById(R.id.nav_view);
        if (!strNickname.equals("비회원")) {
            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setVisible(false);
        }

//        TextView tv_userId = (TextView) context.findViewById(R.id.user_id);
//        tv_userId.setText(name);
        // xml 파일에서 넣어놨던 header 선언
        // header에 있는 리소스 가져오기
        //로그인시 유저의 아이디값 로그인인텐트에서 받아와 이름변경
        textView = navigationView.getHeaderView(0).findViewById(R.id.user_id);
        textView1 = navigationView.getHeaderView(0).findViewById(R.id.user_info);
        user_money = navigationView.getHeaderView(0).findViewById(R.id.user_money);
        user_city = navigationView.getHeaderView(0).findViewById(R.id.user_city);
        textView.setText(strNickname);
        textView1.setText(strEmail);
        user_city.setText("지역을 선택하세요.");


        if (Member != null) {
            Member.cancel(true);
            Member = null;
        }
        Member = new DbCon.Member();
        if (Member != null) {
            Member.execute(strEmail, strNickname, "0");//보니까 member 테이블에 등록 된후에도 로그인시에 도시 입력하라고 뜨는데 member에 없을떄랑 다르게 밑에있는 뒤로가기버튼이 먹힘
        }


        System.out.println(strNickname);

        System.out.println("야호!!!");


        //UI
        String[] categoryTitle = {"전체", "음식", "카페", "유통", "의료", "유흥", "헬스", "미용", "학원", "의류", "기타"};

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
                    if (dataAdapter != null) {
                        dataAdapter.cancel(true);
                        dataAdapter = null;
                    }
                    dataAdapter = new DbCon.DataAdapter(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers);
                    Log.i("DataAdapter", "현위치");
                    if (dataAdapter != null) {
                        dataAdapter.execute(String.valueOf(Maps.beforeCamera.latitude), String.valueOf(Maps.beforeCamera.longitude), String.valueOf(MainActivity.user_city.getText()));
                    }
                } else {
                    if (PlaceList.isCheckedButtonNear) {
                        PlaceList.button_near.performClick();
                    } else {
                        PlaceList.button_camera.performClick();
                    }
                }
                if (Maps.mLayout != null &&
                        (Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    Maps.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
                //비회원 관리
                if (MainActivity.strNickname.equals("비회원")) {
                    if (item.getItemId() == R.id.myplaces || item.getItemId() == R.id.edit) {
                        Toast.makeText(MainActivity.this, "로그인후 이용가능한 기능입니다.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
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
                        horizontalScrollView.setVisibility(View.INVISIBLE);
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

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.detach(myPlaceFrag).attach(myPlaceFrag).commit();
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

                        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.detach(moneyFrag).attach(moneyFrag).commit();
                        break;
                }
                return true;
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (!flag) {
                        sleep(100);
                        Log.d("Ssibal", "쓰레드");
                    }

                    if (strStartWithQRCode == null && !strNickname.equals("비회원") && user_city.getText().equals("지역을 선택하세요.")) {
                        Intent newUser = new Intent(MainActivity.this, SettingPopupActivity.class);
                        newUser.putExtra("mode", "new");
                        startActivity(newUser);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.login:
//                        Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//                        LayoutInflater inflater = getLayoutInflater();
//                        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.button_test, null);
//                        alertDialog.setView(view);
//                        final AlertDialog dialog = alertDialog.create();
//                        dialog.show();  // 로그인팝업
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "로그인 클릭", Toast.LENGTH_SHORT).show();
                        finish();

                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater2 = getLayoutInflater();
                        View view2 = inflater2.inflate(R.layout.user_data, null);
                        builder.setView(view2);
                        TextView ni = view2.findViewById(R.id.tvNickname);
                        ni.setText(strNickname);
                        TextView em = view2.findViewById(R.id.tvEmail);
                        em.setText(strEmail);
                        TextView ag = view2.findViewById(R.id.tvAgeRange);
                        ag.setText(strAgeRange);
                        ImageView ivProfile = view2.findViewById(R.id.ivProfile);
                        Glide.with(getApplicationContext()).load(strProfile).into(ivProfile);

                        final AlertDialog dialog2 = builder.create();
                        dialog2.show();
//                        builder.setTitle("회원정보");
//                        builder.setMessage("회원정보를 보시겠습니까?");
//                        builder.setPositiveButton("예",
//
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent = new Intent();
//                                        intent.putExtra("logout_user", textView.getText().toString() + "님이 로그아웃 하셨습니다.");
//                                        setResult(LoginActivity.LOG_OUT_FLAG = 1, intent);
//                                        finish();
//                                    }
//                                });
//                        builder.setNegativeButton("아니오",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
////                                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                        builder.show();

                        break;
                    case R.id.scan_QR:
                        qrScan = new IntentIntegrator(MainActivity.this);
                        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
                        qrScan.setPrompt("QR코드를 스캔해보아요~");
                        qrScan.initiateScan();

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

        if (MainActivity.strNickname.equals("비회원")) {
            Intent setting = new Intent(MainActivity.this, SettingPopupActivity.class);
            setting.putExtra("mode", "notmember");
            startActivity(setting);
        }

        if (strStartWithQRCode != null) {
            String[] array = strStartWithQRCode.split(",");
            if (array[0].equals("Normal")) {
                Toast.makeText(this, "Normal", Toast.LENGTH_SHORT).show();
            }
            if (array[0].equals("Create_Marker")) {

                if (qr != null) {
                    qr.cancel(true);
                    qr = null;
                }
                qr = new DbCon.Qr();
                if (qr != null) {

                    qr.execute(array[1]);
                }

                Thread QRThread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            while (!QRFlag) {
                                sleep(100);
                                Log.d("Ssibal", "쓰레드");
                            }
                            Intent intent = new Intent(MainActivity.this, InfoPopupActivity.class);
                            FranchiseDTO tags = DbCon.Qr.QrFranchise.get(0);
                            intent.putExtra("id", tags.id);
                            intent.putExtra("name", tags.name);
                            intent.putExtra("address", tags.address);
                            intent.putExtra("category", tags.category);
                            intent.putExtra("tel", tags.tel);
                            intent.putExtra("latitude", tags.latitude);
                            intent.putExtra("longitude", tags.longitude);
                            startActivity(intent);
                            QRFlag = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                QRThread.start();
                Toast.makeText(this, "Create_Marker, FranchiseID=" + array[1], Toast.LENGTH_SHORT).show();
            }

        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 왼쪽 상단 버튼 눌렀을 때
//                Toast.makeText(MainActivity.this, "햄버거", Toast.LENGTH_SHORT).show();
                user_money.setText(String.valueOf(MoneyRecyclerViewAdapter.leftovermoney));
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
                Search = new DbCon.Search(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers, PlaceList.container.getContext(), PlaceList.recyclerView);
                if (Search != null) {
                    Search.execute(s, "1", String.valueOf(MainActivity.user_city.getText()));//인자로 city 스트링 보내면 해당 도시만 출력가능
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
                Search = new DbCon.Search(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers, PlaceList.container.getContext(), PlaceList.recyclerView);
                if (Search != null) {
                    Search.execute(s, "2", String.valueOf(MainActivity.user_city.getText()));//인자로 city 스트링 보내면 해당 도시만 출력가능
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                MainActivity.drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String[] array = result.getContents().split("=");

                if (array.length == 3 && array[1].equals("Create_Marker&target")) {
                    if (qr != null) {
                        qr.cancel(true);
                        qr = null;
                    }
                    qr = new DbCon.Qr();
                    if (qr != null) {

                        qr.execute(array[2]);
                    }

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                while (!MainActivity.QRFlag) {
                                    sleep(100);
                                    Log.d("Ssibal", "쓰레드");
                                }
                                Intent intent = new Intent(MainActivity.this, InfoPopupActivity.class);
                                FranchiseDTO tags = DbCon.Qr.QrFranchise.get(0);
                                intent.putExtra("id", tags.id);
                                intent.putExtra("name", tags.name);
                                intent.putExtra("address", tags.address);
                                intent.putExtra("category", tags.category);
                                intent.putExtra("tel", tags.tel);
                                intent.putExtra("latitude", tags.latitude);
                                intent.putExtra("longitude", tags.longitude);
                                startActivity(intent);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }

                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}