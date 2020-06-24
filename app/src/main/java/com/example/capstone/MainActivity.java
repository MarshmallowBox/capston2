package com.example.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static String strNickname, strEmail;
    public static UI ui;
    static TextView tv_userId, tv_userEmail;
    private SearchView searchView;
    private MenuItem mSearch;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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
//        ui.createNav_Drawer(strNickname, strEmail);
        Toast.makeText(this, "이름 : " + strNickname + "이메일 : " + strEmail, Toast.LENGTH_SHORT).show();
        createNav_Drawer(strNickname, strEmail);

        //로그인값
        //tv_userId = (TextView)findViewById(R.id.user_id);

        //Toast.makeText(MainActivity.this, strNickname , Toast.LENGTH_SHORT).show();

        //v_userId.setText("시발");
        //new Intent(getApplicationContext(), LoginInfo.class);
        // startActivity(intent);
    }
    void createNav_Drawer(String Name, String Email) {
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                        Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                        ui.show();
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
                                        MainActivity.this.setResult(LoginActivity.LOG_OUT_FLAG = 1, intent);
                                        MainActivity.this.finish();
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
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(getApplicationContext(), HelpPopupActivity.class);
                        startActivity(intent2);

                        break;
                    case R.id.preferences:
                        Toast.makeText(MainActivity.this, "설정", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


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
                System.out.println(s);
                DbCon.Search Search = new DbCon.Search();
                Search.execute(s,"1");//인자로 city 스트링 보내면 해당 도시만 출력가능
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //입력할때마다 이벤트
                //여기다 자동완성 넣으면 갸꿀

                System.out.println(s);
                DbCon.Search Search = new DbCon.Search();
                Search.execute(s,"2");//인자로 city 스트링 보내면 해당 도시만 출력가능
                System.out.println("잉기모링");
                System.out.println(DbCon.Franchises);
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