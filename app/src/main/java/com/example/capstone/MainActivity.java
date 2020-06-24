package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static boolean firsttime = true;
    public static String strNickname, strEmail;
    static TextView tv_userId, tv_userEmail;
    private SearchView searchView;
    private MenuItem mSearch;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public static UI ui;
    public static UI ui2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent2 = new Intent(this, HelpPopupActivity.class);
        startActivity(intent2);
        firsttime = true;


        //앱바(툴바) 생성부분인데 밑에 이벤트와같이 UI클래스에 같이 합칠수있는지 시도해보기
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.main);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_black_18dp); //뒤로가기 버튼 이미지 지정

        //이거 UI로 옮기는거 찾으면 더깔끔쓰~
        //하단바클릭이벤트를 위한 프레그먼트 생성

        //사용자 데이터
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        strNickname = intent.getExtras().getString("name");
        strEmail = intent.getExtras().getString("Email");


        //UI
        ui = new UI(this);
        ui.createCategory();
        ui.createNav_Bottom();
        ui.createNav_Drawer(strNickname, strEmail);
        Toast.makeText(this, "이름 : " + strNickname + "이메일 : " + strEmail, Toast.LENGTH_SHORT).show();


        //로그인값
        //tv_userId = (TextView)findViewById(R.id.user_id);

        //Toast.makeText(MainActivity.this, strNickname , Toast.LENGTH_SHORT).show();

        //v_userId.setText("시발");
        //new Intent(getApplicationContext(), LoginInfo.class);
        // startActivity(intent);
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
        mSearch = menu.findItem(R.id.search_button);
        searchView = (SearchView) mSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                Toast.makeText(MainActivity.this, s+"", Toast.LENGTH_SHORT).show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //입력할때마다 이벤트
                //여기다 자동완성 넣으면 갸꿀


                return false;
            }
        });
//메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //확장시
                Toast.makeText(MainActivity.this, "확장", Toast.LENGTH_SHORT).show();
                searchView.setIconified(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //축소시
                Toast.makeText(MainActivity.this, "축소", Toast.LENGTH_SHORT).show();


                searchView.setIconified(true);
                searchView.clearFocus();
                return true;
            }
        });
        return true;
    }

}