package com.example.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

public class LoginInfo extends AppCompatActivity {

    String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);

        TextView tvNickname = findViewById(R.id.tvNickname);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnSignout = findViewById(R.id.btnSignout);
        Button btnNext = findViewById(R.id.button2);
        //순서대로 각각 이메일, 나잇대, 성별, 생일을 보여주는 TextView 선언
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAgeRange = findViewById(R.id.tvAgeRange);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvBirthday = findViewById(R.id.tvBirthday);

        Intent intent = getIntent();
        strNickname = intent.getExtras().getString("name");
        strProfile = intent.getExtras().getString("profile");
        //이메일, 나잇대, 성별, 생일을 intent에서 가져와서 각 String에 저장함
        strEmail = intent.getExtras().getString("email");
        strAgeRange = intent.getExtras().getString("ageRange");
        strGender = intent.getExtras().getString("gender");
        strBirthday = intent.getStringExtra("birthday");    //같은 함수인가봐

        tvNickname.setText(strNickname);
        Glide.with(this).load(strProfile).into(ivProfile);
        //받아온 정보를 각 TextView에 표시함
        tvEmail.setText(strEmail);
        tvAgeRange.setText(strAgeRange);
        tvGender.setText(strGender);
        tvBirthday.setText(strBirthday);




        btnNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {//다음 버튼을 누르면
                Toast.makeText(getApplicationContext(), "정상적으로 메인문으로 갑니다~", Toast.LENGTH_SHORT).show();

//                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {//로그아웃실행
//                    @Override
//                    public void onCompleteLogout() {
//                        //로그아웃 성공 시 로그인 화면(LoginActivity)로 이동
//                        Intent intent = new Intent(LoginInfo.this, MainActivity.class);
//                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        if (strNickname == "none")
//                            intent.putExtra("name", "일반사용자");
//                        else
//                            intent.putExtra("name", strNickname);
//
//                        if (strEmail=="none")
//                            intent.putExtra("Email", "00000@naver.com");
//                        else
//                            intent.putExtra("Email", strEmail);
//                        startActivity(intent);
//                    }
//                });
                Intent intent = new Intent(LoginInfo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", strNickname);
                intent.putExtra("Email", strEmail);
                startActivity(intent);
            }
        });


        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {//로그아웃 버튼 클릭하면
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {//로그아웃실행
                    @Override
                    public void onCompleteLogout() {
                        //로그아웃 성공 시 로그인 화면(LoginActivity)로 이동
                        Intent intent = new Intent(LoginInfo.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) { //탈퇴버튼 클릭시
                new AlertDialog.Builder(LoginInfo.this)//팝업창 실행
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {//회원탈퇴 실행
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {//실패시
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {//로그인 세션 닫혀있을 시 다시 로그인해달라는 toast, 로그인 창으로 이동
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginInfo.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {  //가입되지 않은 계정 회원탈퇴 요구시 toast메세지 띄우고 로그인창으로 이동
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginInfo.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {  //회원탈퇴에 성공하면 로그인창으로 이동
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginInfo.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {   //아니오 버튼 클릭 시 팝업창을 닫는다
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();   //팝업창 닫음
                            }
                        }).show();
            }
        });

        //자동로그인 추가
        btnNext.performClick();
    }
}