package com.example.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static int LOG_OUT_FLAG = 0;
    //카카오
    private SessionCallback sessionCallback;

    //네이버
    private static String ID = "h5zWuad4aNh7mnhB5nph";
    private static String SECRET = "cStKOHwftZ";
    private static String NAME = "네이버 아이디로 로그인";
    public static OAuthLoginButton naverLogInButton;
    public static OAuthLogin naverLoginInstance;

    public Context context;
    Button btnGetApi, btnLogout;
    String QRCodeData;

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("종료");
        builder.setMessage("종료하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //카카오
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_test);


        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();

            if(uri != null) {
                QRCodeData = uri.getQueryParameter("mode")+","+uri.getQueryParameter("target");
                Log.d("MyTag","mode : " + QRCodeData);
            }
        }


        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();



        //네이버
        init();
        init_View();
        //자동로그인 추가
        btnGetApi.performClick();

        Button nologin = findViewById(R.id.nologin);
        nologin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {//비로그인 버튼 클릭하면
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", "비회원");
                intent.putExtra("email", "비로그인 사용자");
                intent.putExtra("profile", "");
                Log.d("nologin","mode : " + QRCodeData);
                intent.putExtra("startWithQRCode", QRCodeData);
                startActivity(intent);
                Toast.makeText(context,"비로그인 사용자",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    //////////////카카오////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String needsScopeAutority = ""; // 정보 제공이 허용되지 않은 항목의 이름을 저장하는 변수

                    // 이메일, 성별, 연령대, 생일 정보를 제공하는 것에 동의했는지 체크
                    if(result.getKakaoAccount().needsScopeAccountEmail()) {
                        needsScopeAutority = needsScopeAutority + "이메일";
                    }
                    if(result.getKakaoAccount().needsScopeGender()) {
                        needsScopeAutority = needsScopeAutority + ", 성별";
                    }
                    if(result.getKakaoAccount().needsScopeAgeRange()) {
                        needsScopeAutority = needsScopeAutority + ", 연령대";
                    }
                    if(result.getKakaoAccount().needsScopeBirthday()) {
                        needsScopeAutority = needsScopeAutority + ", 생일";
                    }

                    if(needsScopeAutority.length() != 0) { // 정보 제공이 허용되지 않은 항목이 있다면 -> 허용되지 않은 항목을 안내하고 회원탈퇴 처리
                        if(needsScopeAutority.charAt(0) == ',') {
                            needsScopeAutority = needsScopeAutority.substring(2);
                        }
                        Toast.makeText(getApplicationContext(), needsScopeAutority+"에 대한 권한이 허용되지 않았습니다. 개인정보 제공에 동의해주세요.", Toast.LENGTH_SHORT).show(); // 개인정보 제공에 동의해달라는 Toast 메세지 띄움

                        // 회원탈퇴 처리
                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                int result = errorResult.getErrorCode();

                                if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                    Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNotSignedUp() {
                                Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Long result) { }
                        });
                    } else { // 모든 항목에 동의했다면 -> 유저 정보를 가져와서 MainActivity에 전달하고 MainActivity 실행.
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("name", result.getNickname());
                        intent.putExtra("profile", result.getProfileImagePath());
                        Log.d("getKakaoAccount","mode : " + QRCodeData);
                        intent.putExtra("startWithQRCode", QRCodeData);

                        if (result.getKakaoAccount().hasEmail() == OptionalBoolean.TRUE)
                            intent.putExtra("email", result.getKakaoAccount().getEmail());
                        else
                            intent.putExtra("email", "none");
                        if (result.getKakaoAccount().hasAgeRange() == OptionalBoolean.TRUE)
                            intent.putExtra("ageRange", result.getKakaoAccount().getAgeRange().getValue());
                        else
                            intent.putExtra("ageRange", "none");
                        if (result.getKakaoAccount().hasGender() == OptionalBoolean.TRUE)
                            intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
                        else
                            intent.putExtra("gender", "none");
                        if (result.getKakaoAccount().hasBirthday() == OptionalBoolean.TRUE)
                            intent.putExtra("birthday", result.getKakaoAccount().getBirthday());
                        else
                            intent.putExtra("birthday", "none");

                        startActivity(intent);
                        Toast.makeText(context,"성공적으로 Main에 카카오 로그인정보 전송",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }



        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


//                        Log.d("KAKAO_API", "nickname: " + profile.getNickname());
//                        Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
//                        Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

    /////////네이버////////////
//    /**
//     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
//     객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
//     */
//    @SuppressLint("HandlerLeak")
//    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
//        @Override
//        public void run(boolean success) {
//            if (success) {
//                String accessToken = naverLoginInstance.getAccessToken(mContext);
//                String refreshToken = naverLoginInstance.getRefreshToken(mContext);
//                long expiresAt = naverLoginInstance.getExpiresAt(mContext);
//                String tokenType = naverLoginInstance.getTokenType(mContext);
////                mOauthAT.setText(accessToken);
////                mOauthRT.setText(refreshToken);
////                mOauthExpires.setText(String.valueOf(expiresAt));
////                mOauthTokenType.setText(tokenType);
////                mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());
//            } else {
//                String errorCode = naverLoginInstance.getLastErrorCode(mContext).getCode();
//                String errorDesc = naverLoginInstance.getLastErrorDesc(mContext);
//                Toast.makeText(mContext, "errorCode:" + errorCode
//                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
//            }
//        }
//    };


    /////////////////////////////네이버///////////////////////////////////////////
    //회원이름, 이메일, 별명, 프로필사진, 성별, 생일, 연령대
    //초기화
    private void init(){

        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this,ID,SECRET,NAME);
    }
    //변수 붙이기
    private void init_View(){
        naverLogInButton = (OAuthLoginButton)findViewById(R.id.buttonNaverLogin);

        //로그인 핸들러
        OAuthLoginHandler naverLoginHandler  = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공
                    new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
                    Toast.makeText(context,"로그인 성공",Toast.LENGTH_SHORT).show();
//                    finish();

                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

        };

        naverLogInButton.setOAuthLoginHandler(naverLoginHandler);
//        tv_mail = (TextView)findViewById(R.id.textView4);
        btnGetApi = (Button)findViewById(R.id.btngetapi);
        btnGetApi.setOnClickListener(this);
//        btnLogout = (Button)findViewById(R.id.logout2);
//        btnLogout.setOnClickListener(this);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btngetapi){
            new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
        }
//        if(v.getId() == R.id.logout2){
//            naverLoginInstance.logout(context);
//            tv_mail.setText((String) "");//메일 란 비우기
//        }

    }


    @SuppressLint("StaticFieldLeak")
    public class RequestApiTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {//작업이 실행되기 전에 먼저 실행.
            Toast.makeText(context,"RequestAPITask",Toast.LENGTH_SHORT).show();
        }

        @Override
        public String doInBackground(Void... params) {//네트워크에 연결하는 과정이 있으므로 다른 스레드에서 실행되어야 한다.

            String url = "https://openapi.naver.com/v1/nid/me";
            String at = naverLoginInstance.getAccessToken(context);

            return naverLoginInstance.requestApi(context, at, url);//url, 토큰을 넘겨서 값을 받아온다.json 타입으로 받아진다.
        }

        public void onPostExecute(String content) {//doInBackground 에서 리턴된 값이 여기로 들어온다.
            try {
                Toast.makeText(context,"성공적으로 Main에 네이버 로그인정보 전송",Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(content);
                JSONObject response = jsonObject.getJSONObject("response");
                //String email = response.getString("email");
                //tv_mail.setText(email);//메일 란 채우기

                //메인페이지로 데이터 보내기

                Toast.makeText(context,"데이터 받아오는중...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", response.getString("name"));
                intent.putExtra("profile", response.getString("profile_image"));
                intent.putExtra("email", response.getString("email"));
                Log.d("naver","mode : " + QRCodeData);
                intent.putExtra("startWithQRCode", QRCodeData);
                //intent.putExtra("", response.getString("id"));
                //intent.putExtra("", response.getString("nickname"));
                //intent.putExtra("ageRange", response.getString("age"));
                //intent.putExtra("gender", response.getString("gender"));
                //Toast.makeText(context,"이사람은 남자일까 여자일까 ? :"+response.getString("gender"),Toast.LENGTH_SHORT).show();


                //intent.putExtra("birthday", response.getString("birthday"));

                Toast.makeText(context,"성공적으로 Main에 네이버 로그인정보 전송",Toast.LENGTH_SHORT).show();
                startActivity(intent);

                finish();


            }
            catch (Exception e){
                e.printStackTrace();
            }
            /* Json 제공형식

                    "resultcode":"00",

                    "message":"success",

                    "response":{
                    "id":"tjdeh1230",
                    "nickname":"꼬리별",
                    "profile_image":"https://~~~.png",
                    "age":"20-29",
                    "gender":"M",
                    "email":"tjeh1230@naver.com",
                    "name":"김성도",
                    "birthday":"03-29"
                    }
            */

        }
    }


}