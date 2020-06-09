package com.example.capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DbClass extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";
    private static final String TAG_CATEGORY ="category";
    private static final String TAG_TEL ="tel";
    private static final String TAG_LATITUDE ="latitude";
    private static final String TAG_LONGITUDE ="longitude";
    public static ArrayList<FranchiseDTO> Franchises = new ArrayList<>();
    public static ArrayList<FranchiseDTO> nearFranchises = new ArrayList<>();
    public static ArrayList<Marker> Markers = new ArrayList<>();
    public static ArrayList<Marker> nearMarkers = new ArrayList<>();
    private static TextView mTextViewResult;
    static ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();;
    ListView mListViewList;
    EditText mEditTextSearchKeyword1, mEditTextSearchKeyword2;
    static String mJsonString;
    public static Context mContext;
    public static String searchKeyword3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public static class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }
        public static void showResult(){
            try {
                System.out.println("과여어어어언?");
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                //여기는 현재위치 관련
                if(searchKeyword3.equals("true")){
                    System.out.println("현재위치");
                    nearFranchises.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String id = item.getString(TAG_ID);
                        String name = item.getString(TAG_NAME);
                        String address = item.getString(TAG_ADDRESS);
                        String category = item.getString(TAG_CATEGORY);
                        String tel = item.getString(TAG_TEL);
                        String latitude = item.getString(TAG_LATITUDE);
                        String longitude = item.getString(TAG_LONGITUDE);
                        HashMap<String,String> hashMap = new HashMap<>();
                        nearFranchises.add(new FranchiseDTO(Integer.parseInt(id),name,address,category,tel,Double.parseDouble(latitude),Double.parseDouble(longitude)));

                        mArrayList.add(hashMap);
                    }
                    System.out.println("**************");
                    System.out.println(nearFranchises.size());
                    System.out.println("**************");

                    ///////
                    System.out.println(nearMarkers.size());

                    for(Marker marker: nearMarkers){
                        marker.setMap(null);

                    }
                    nearMarkers.clear();
                    System.out.println(nearMarkers.size());


//카메라 이동시 마커관련
                    RadioButton category1 = (RadioButton) UI.radioGroup.getChildAt(UI.radioGroup.getCheckedRadioButtonId());
//                    //화면에 마커 visible
                    for (final FranchiseDTO franchise : nearFranchises) {
                        if (category1.getText().equals(franchise.category) || category1.getText().equals("전체")) {
                            final Marker marker = new Marker();
                            marker.setPosition(new LatLng(franchise.latitude, franchise.longitude));//위경도

//        marker.setIcon(OverlayImage.fromResource(R.drawable.appicon64)); //아이콘 직접 지정
                            marker.setIcon(MarkerIcons.YELLOW);//기본제공 마커
                            //특정 줌 레벨에서만 캡션이 나타나도록 지정
                            marker.setCaptionMinZoom(12);
//        marker.setCaptionMaxZoom(16);
                            //마커 크기지정 아마 3:4비율인듯
                            marker.setWidth(90);
                            marker.setHeight(120);
                            marker.setCaptionText(franchise.name); //메인캡션
                            marker.setTag(franchise);//인포뷰에 전달할 태그값
                            marker.setSubCaptionText(franchise.category); //서브캡션
                            marker.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                            marker.setSubCaptionTextSize(10); //서브캡션 크기
                            marker.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기

                            marker.setOnClickListener(new Overlay.OnClickListener() {
                                @Override
                                public boolean onClick(@NonNull Overlay overlay) {
                                    //클릭시 카메라 이동
                                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(marker.getPosition()).animate(CameraAnimation.Easing));
                                    //infoWindow에 franchises값 태그로 전달
                                    Maps.infoWindow.setTag(franchise);
                                    //인포뷰 활성화
                                    Maps.infoWindow.open(marker);
                                    return true;
                                }
                            });
                            marker.setMap(Maps.naverMap); //지도에 추가, null이면 안보임
                            nearMarkers.add(marker);
                        }
                    }
                    System.out.println(nearMarkers.size());

                    ///////
                } else{
                    System.out.println("카메라");

                    Franchises.clear();

                    //여기는 카메라위치 관련
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String id = item.getString(TAG_ID);
                        String name = item.getString(TAG_NAME);
                        String address = item.getString(TAG_ADDRESS);
                        String category = item.getString(TAG_CATEGORY);
                        String tel = item.getString(TAG_TEL);
                        String latitude = item.getString(TAG_LATITUDE);
                        String longitude = item.getString(TAG_LONGITUDE);
                        HashMap<String,String> hashMap = new HashMap<>();
                        Franchises.add(new FranchiseDTO(Integer.parseInt(id),name,address,category,tel,Double.parseDouble(latitude),Double.parseDouble(longitude)));

                        mArrayList.add(hashMap);
                    }
                    System.out.println("**************");
                    System.out.println(Franchises.size());
                    System.out.println("**************");

                    ///////
                    System.out.println(Markers.size());

                    for(Marker marker: Markers){
                        marker.setMap(null);

                    }
                    Markers.clear();
                    System.out.println(Markers.size());


//카메라 이동시 마커관련
                    RadioButton category1 = (RadioButton) UI.radioGroup.getChildAt(UI.radioGroup.getCheckedRadioButtonId());
//                    //화면에 마커 visible
                    for (final FranchiseDTO franchise : Franchises) {
                        if (category1.getText().equals(franchise.category) || category1.getText().equals("전체")) {
                            final Marker marker = new Marker();
                            marker.setPosition(new LatLng(franchise.latitude, franchise.longitude));//위경도

//        marker.setIcon(OverlayImage.fromResource(R.drawable.appicon64)); //아이콘 직접 지정
                            marker.setIcon(MarkerIcons.YELLOW);//기본제공 마커
                            //특정 줌 레벨에서만 캡션이 나타나도록 지정
                            marker.setCaptionMinZoom(12);
//        marker.setCaptionMaxZoom(16);
                            //마커 크기지정 아마 3:4비율인듯
                            marker.setWidth(90);
                            marker.setHeight(120);
                            marker.setCaptionText(franchise.name); //메인캡션
                            marker.setTag(franchise);//인포뷰에 전달할 태그값
                            marker.setSubCaptionText(franchise.category); //서브캡션
                            marker.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                            marker.setSubCaptionTextSize(10); //서브캡션 크기
                            marker.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기

                            marker.setOnClickListener(new Overlay.OnClickListener() {
                                @Override
                                public boolean onClick(@NonNull Overlay overlay) {
                                    //클릭시 카메라 이동
                                    Maps.naverMap.moveCamera(CameraUpdate.scrollTo(marker.getPosition()).animate(CameraAnimation.Easing));
                                    //infoWindow에 franchises값 태그로 전달
                                    Maps.infoWindow.setTag(franchise);
                                    //인포뷰 활성화
                                    Maps.infoWindow.open(marker);
                                    return true;
                                }
                            });
                            marker.setMap(Maps.naverMap); //지도에 추가, null이면 안보임
                            Markers.add(marker);
                        }
                    }
                    System.out.println(Markers.size());

                    ///////
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            try {
                searchKeyword3 = params[2];

            } catch (Exception e){
                e.getStackTrace();
            }

            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/query.php";
            System.out.println(serverURL);
            String postParameters = "name=jsh&latitude=" + searchKeyword1 + "&longitude=" + searchKeyword2; // 여기서 범위지정 가능활듯? 범위를 변수로 줘서 + - 시키면????
            System.out.println(postParameters);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
}