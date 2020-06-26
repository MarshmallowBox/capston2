package com.example.capstone;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DbCon extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "store_id";
    private static final String TAG_STORE_ID = "st_id";
    private static final String TAG_S_ID = "s_id";
    private static final String TAG_ME_ID ="me_id";
    private static final String TAG_USERNAME ="me_id";
    private static final String TAG_SCORE ="me_id";
    private static final String TAG_REVIEWID ="review_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";
    private static final String TAG_CATEGORY ="category";
    private static final String TAG_TEL ="tel";
    private static final String TAG_LATITUDE ="latitude";
    private static final String TAG_LONGITUDE ="longitude";
    private static final String TAG_DATE ="date";
    private static final String TAG_REVIEWTXt ="reviewTXT";

    public static ArrayList<ReviewDTO> Reviews = new ArrayList<>();
    public static ArrayList<MemberDTO> Members = new ArrayList<>();
    static ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
    static String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    static class DataAdapter extends AsyncTask<String, Void, String> {
        final Activity activity;
        final NaverMap naverMap;
        private final String TAG_JSON = "webnautes";
        private final String TAG_ID = "store_id";
        private final String TAG_NAME = "name";
        private final String TAG_ADDRESS = "address";
        private final String TAG_CATEGORY = "category";
        private final String TAG_TEL = "tel";
        private final String TAG_LATITUDE = "latitude";
        private final String TAG_LONGITUDE = "longitude";
        private final String TAG = "PHP_Query";
        ArrayList<FranchiseDTO> Franchises = new ArrayList<>();        //        이거 어뎁터로 만들기
        ArrayList<Marker> Markers;
        Context context;
        RecyclerView recyclerView;
        String mJsonString = null;

        DataAdapter(Activity activity, NaverMap naverMap, ArrayList<Marker> Markers) {
            this.activity = activity;
            this.naverMap = naverMap;
            this.Markers = Markers;
            this.context = null;
            this.recyclerView = null;
        }

        DataAdapter(Context context, RecyclerView recyclerView) {
            this.activity = null;
            this.naverMap = null;
            this.Markers = null;
            this.context = context;
            this.recyclerView = recyclerView;
        }

        public ArrayList<FranchiseDTO> getFilteredData() {
            ArrayList<FranchiseDTO> categoryFilteredFranchises = new ArrayList<>();
            RadioButton checkedRadioButton = (RadioButton) MainActivity.radioGroup.getChildAt(MainActivity.radioGroup.getCheckedRadioButtonId());
            for (FranchiseDTO franchiseDTO : Franchises) {
                if (checkedRadioButton.getText().equals(franchiseDTO.category) || checkedRadioButton.getText().equals("전체")) {
                    categoryFilteredFranchises.add(franchiseDTO);
                }
            }
            return categoryFilteredFranchises;
        }

        public void setMarkersOnMap() {
            ArrayList<FranchiseDTO> beforeClusterData = getFilteredData();

            Clustering cluster = new Clustering(naverMap, beforeClusterData);
            final ArrayList<FranchiseDTO>[] clusterFranchises = cluster.getClusterData();
            for (Marker marker : Markers) {
                marker.setMap(null);
            }
            Markers.clear();
            for (int i = 0; i < clusterFranchises.length; i++) {


                if (clusterFranchises[i] != null) {
                    if (clusterFranchises[i].size() == 1) {
                        final FranchiseDTO franchise = clusterFranchises[i].get(0);
                        final Marker marker = new Marker();
                        marker.setPosition(new LatLng(franchise.latitude, franchise.longitude));//위경도
                        marker.setIcon(MarkerIcons.BLUE);//기본제공 마커
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
                                naverMap.moveCamera(CameraUpdate.scrollTo(marker.getPosition()).animate(CameraAnimation.Easing));
                                //infoWindow에 franchises값 태그로 전달
                                Maps.infoWindow.setTag(franchise);
                                //인포뷰 활성화
                                Maps.infoWindow.open(marker);

                                Maps.infoWindow.performClick();
                                return true;
                            }
                        });
                        marker.setMap(naverMap); //지도에 추가, null이면 안보임
                        Markers.add(marker);
                    } else if (clusterFranchises[i].size() > 1) {
                        final FranchiseDTO franchises = clusterFranchises[i].get(0);
                        double lat = 0;
                        double log = 0;
                        for (FranchiseDTO franchise : clusterFranchises[i]) {
                            lat += franchise.latitude;
                            log += franchise.longitude;
                        }
                        final Marker clusterMarker = new Marker();
                        int size = clusterFranchises[i].size();
//                            clustmarker.setPosition(new LatLng(clusterData[i].get(0).getPosition().latitude, clusterData[i].get(0).getPosition().longitude));//위경도
                        clusterMarker.setPosition(new LatLng(lat / size, log / size));//위경도
                        clusterMarker.setIcon(MarkerIcons.LIGHTBLUE);//기본제공 마커
                        clusterMarker.setCaptionOffset(-10);
                        //마커 크기지정 아마 3:4비율인듯
                        clusterMarker.setCaptionText((i + 1) + "번 cluster Maker"); //메인캡션
                        clusterMarker.setSubCaptionText(String.valueOf(size)); //서브캡션
                        clusterMarker.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                        clusterMarker.setSubCaptionTextSize(10); //서브캡션 크기
                        clusterMarker.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기
                        clusterMarker.setWidth(90 + (Math.min(size * 3, 150)));
                        clusterMarker.setHeight(120 + (Math.min(size * 4, 200)));
                        final int finalI = i;
                        clusterMarker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                naverMap.moveCamera(CameraUpdate.scrollTo(clusterMarker.getPosition()).animate(CameraAnimation.Easing));

                                RecyclerView mRecyclerView = activity.findViewById(R.id.maps_recyclerview);
                                FranchiseRecyclerViewAdapter mAdapter = new FranchiseRecyclerViewAdapter(clusterFranchises[finalI]);
                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

                                mAdapter.notifyDataSetChanged();//데이터변경시

                                if (Maps.mLayout != null &&
                                        (Maps.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                                    Maps.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                }

                                return true;
                            }
                        });
                        clusterMarker.setMap(naverMap); //지도에 추가, null이면 안보임
                        Markers.add(clusterMarker);

                    }

                }
            }
        }

        public void executeResult() {
            try {
                System.out.println("\n****************************");
                System.out.println("getData");
                System.out.println("****************************\n");

                Franchises.clear();
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String category = item.getString(TAG_CATEGORY);
                    String id = item.getString(TAG_ID);
                    String name = item.getString(TAG_NAME);
                    String address = item.getString(TAG_ADDRESS);
                    String tel = item.getString(TAG_TEL);
                    String latitude = item.getString(TAG_LATITUDE);
                    String longitude = item.getString(TAG_LONGITUDE);
                    Franchises.add(new FranchiseDTO(Integer.parseInt(id), name, address, category, tel, Double.parseDouble(latitude), Double.parseDouble(longitude)));
                }


            } catch (JSONException e) {
                Log.d(TAG, "JSONException : ", e);

            } catch (Exception e) {
                Log.d(TAG, "Exception : ", e);
            } finally {
                if (activity == null && naverMap == null && Markers == null && context != null && recyclerView != null) {

                    FranchiseRecyclerViewAdapter mAdapter = new FranchiseRecyclerViewAdapter(getFilteredData());
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mAdapter.notifyDataSetChanged();

//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context); //리스트뷰를 띄워준다
//                FranchiseRecyclerViewAdapter myFranchiseRecyclerViewAdapter = new FranchiseRecyclerViewAdapter(getFilteredData());
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(myFranchiseRecyclerViewAdapter);
                }
                if (activity != null && naverMap != null && Markers != null && context == null && recyclerView == null) {
                    setMarkersOnMap();

                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("DataAdapter", "onCancelled");
            if (mJsonString != null) {
                executeResult();
            }

        }

        @Override
        protected void onPreExecute() {
            Log.i("DataAdapter", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DataAdapter", "onPostExecute");
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            mJsonString = result;
            executeResult();
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            Log.i("DataAdapter", "doInBackground");
            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/query.php";
            System.out.println(serverURL);
            String postParameters = "name=jsh&latitude=" + searchKeyword1 + "&longitude=" + searchKeyword2 + "&city=" + searchKeyword3; // 여기서 범위지정 가능활듯? 범위를 변수로 줘서 + - 시키면????
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
        }

    }

    public static class Search extends AsyncTask<String, Void, String> {

        public static ArrayList<FranchiseDTO> Franchises = new ArrayList<>();
        String errorString = null;
        ArrayList<Marker> Markers;
        Activity activity;
        NaverMap naverMap;
        Context context;
        RecyclerView recyclerView;
        String searchMode;

        Search(Activity activity, NaverMap naverMap, ArrayList<Marker> Markers, Context context, RecyclerView recyclerView) {
            this.activity = activity;
            this.naverMap = naverMap;
            this.Markers = Markers;
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }

        public void showResult() {
            try {
                Franchises.clear();
                System.out.println("과여어어어언?");
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String id = item.getString(TAG_ID);
                    String name = item.getString(TAG_NAME);
                    String address = item.getString(TAG_ADDRESS);
                    String category = item.getString(TAG_CATEGORY);
                    String tel = item.getString(TAG_TEL);
                    String latitude = item.getString(TAG_LATITUDE);
                    String longitude = item.getString(TAG_LONGITUDE);
                    HashMap<String, String> hashMap = new HashMap<>();
                    Franchises.add(new FranchiseDTO(Integer.parseInt(id), name, address, category, tel, Double.parseDouble(latitude), Double.parseDouble(longitude)));
                    mArrayList.add(hashMap);
                }
                System.out.println("**************");
                System.out.println(Franchises);
                System.out.println("**************");
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            } finally {
                if (Franchises.size() != 0) {
                    if (MainActivity.bottomNavigationView.getSelectedItemId() == R.id.list) {
                        FranchiseRecyclerViewAdapter mAdapter = new FranchiseRecyclerViewAdapter(Franchises);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        mAdapter.notifyDataSetChanged();
                    }
                    if (MainActivity.bottomNavigationView.getSelectedItemId() == R.id.mapmode && searchMode.equals("1")) {
                        Maps.singleMarkers.setMap(null);
                        Maps.singleMarkers = new Marker();
                        Maps.singleMarkers.setPosition(new LatLng(Franchises.get(0).latitude, Franchises.get(0).longitude));//위경도
                        Maps.singleMarkers.setIcon(MarkerIcons.RED);//기본제공 마커
                        //마커 크기지정 아마 3:4비율인듯
//                        marker.setWidth(90);
//                        marker.setHeight(120);
                        Maps.singleMarkers.setCaptionText(Franchises.get(0).name); //메인캡션
                        Maps.singleMarkers.setTag(Franchises.get(0));//인포뷰에 전달할 태그값
                        Maps.singleMarkers.setSubCaptionText(Franchises.get(0).category); //서브캡션
                        Maps.singleMarkers.setSubCaptionColor(Color.BLUE); //서브캡션 색상
                        Maps.singleMarkers.setSubCaptionTextSize(10); //서브캡션 크기
                        Maps.singleMarkers.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기

                        Maps.singleMarkers.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                //클릭시 카메라 이동
                                Maps.naverMap.moveCamera(CameraUpdate.scrollTo(Maps.singleMarkers.getPosition()).animate(CameraAnimation.Easing));
                                //infoWindow에 franchises값 태그로 전달
                                Maps.infoWindow.setTag(Franchises.get(0));
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
                    }

                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            searchMode = searchKeyword2;
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/search.php";
            System.out.println(serverURL);
            String postParameters = "name=" + searchKeyword1 + "&tel=" + searchKeyword2; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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

    static class Zzim extends AsyncTask<String, Void, String> {
        ArrayList<FranchiseDTO> ZzimFranchise = new ArrayList<>();
        String mode = null;
        String errorString = null;
        String mJsonString = null;
        Context context= null;
        RecyclerView recyclerView= null;
        FranchiseRecyclerViewAdapter mAdapter= null;
        Zzim(){

        }
        Zzim(Context context, RecyclerView recyclerView) {

            this.context = context;
            this.recyclerView = recyclerView;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }
        public void showResult() {
            if (mode.equals("call")) {
                try {
                    ZzimFranchise.clear();
                    System.out.println("믿고있었다구");
                    System.out.println("111111");
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    System.out.println("222222");
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    System.out.println("333333");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        System.out.println(i);
                        JSONObject item = jsonArray.getJSONObject(i);
                        String id = item.getString(TAG_ID);
                        String name = item.getString(TAG_NAME);
                        String address = item.getString(TAG_ADDRESS);
                        String category = item.getString(TAG_CATEGORY);
                        String tel = item.getString(TAG_TEL);
                        String latitude = item.getString(TAG_LATITUDE);
                        String longitude = item.getString(TAG_LONGITUDE);
                        ZzimFranchise.add(new FranchiseDTO(Integer.parseInt(id),name,address,category,tel,Double.parseDouble(latitude),Double.parseDouble(longitude)));

                        if(InfoPopupActivity.franchiseID!=0){
                            if (String.valueOf(InfoPopupActivity.franchiseID).equals(item.getString(TAG_ID))) {
                                InfoPopupActivity.star.setChecked(true);
                            } else {
                                InfoPopupActivity.star.setChecked(false);
                            }
                        }

                    }
                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                } finally {
                    if(context!=null && recyclerView !=null){
                        mAdapter = new FranchiseRecyclerViewAdapter(ZzimFranchise);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));

                        mAdapter.notifyDataSetChanged();//데이터변경시
                    }
                    if(InfoPopupActivity.star!=null){

                        InfoPopupActivity.star.invalidate();
                    }
                }
            }
        }



        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            System.out.println(searchKeyword3);
            mode = searchKeyword3;
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/0_zzim.php";
            System.out.println(serverURL);
            String postParameters = "member_id=" + searchKeyword1 + "&store_id=" + searchKeyword2 + "&function=" + searchKeyword3; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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

    public static class Review extends AsyncTask<String, Void, String> {
        String errorString = null;
        static String mJsonString = null;
        static Activity activity=null;
        static RecyclerView mRecyclerView = null;
        static int rowCount;

        public static ArrayList<ReviewDTO> Reviews = new ArrayList<>();
        Review(){

        }
        Review(ReviewPopupActivity activity, RecyclerView mRecyclerView){
            this.activity=activity;
            this.mRecyclerView=mRecyclerView;
        }

        public static void showResult() {
            try {
                System.out.println("리뷰리뷰리뷰");
                JSONObject jsonObject = new JSONObject(mJsonString);
                System.out.println("111111");
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                System.out.println("222222");
                rowCount=0;
                Reviews.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String review_id = item.getString(TAG_REVIEWID);
                    String store_id = item.getString(TAG_STORE_ID);
                    String user_id = item.getString(TAG_ME_ID);
                    String user_name = item.getString(TAG_USERNAME);
                    String date = item.getString(TAG_DATE);
                    String score = item.getString(TAG_SCORE);
                    String text = item.getString(TAG_REVIEWTXt);
                    Reviews.add(new ReviewDTO(Integer.parseInt(review_id),Integer.parseInt(store_id),Integer.parseInt(user_id),user_name,date,Double.parseDouble(score),text));
                    rowCount++;
                }
                System.out.println("**************");
                System.out.println(Reviews);
                System.out.println("**************");
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }finally {
                if(InfoPopupActivity.reviewCounter!=null){
                    InfoPopupActivity.reviewCounter.setText("리뷰: " + rowCount + "개");
                }
                if(activity!=null && mRecyclerView!=null){
                    // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                    ReviewRecyclerViewAdapter mAdapter = new ReviewRecyclerViewAdapter(Reviews);
                    mRecyclerView.setAdapter(mAdapter);

                    // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ----리뷰---- " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String searchKeyword4 = params[3];
            String searchKeyword5 = params[4];
            String searchKeyword6 = params[5];

            System.out.println(searchKeyword1);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/review.php";
            System.out.println(serverURL);
            String postParameters = "st_id=" + searchKeyword1 + "&function=" + searchKeyword2 + "&me_id=" + searchKeyword3 + "&score=" + searchKeyword4 + "&reviewTXT=" + searchKeyword5 + "&date=" + searchKeyword6; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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

    public static class Member extends AsyncTask<String, Void, String> {
        private static final String TAG_MEMBER_ID = "member_id";
        private static final String TAG_TEL = "tel";
        private static final String TAG_NAME = "name";
        private static final String TAG_NICKNAME = "nickname";
        private static final String TAG_EMAIL = "email";
        private static final String TAG_AGERANGE = "agerange";
        private static final String TAG_GENDER = "gender";
        private static final String TAG_BIRTHDAY = "birthday";
        private static final String TAG_PROFILEIMG = "profileimg";
        private static final String TAG_STARTMONEY = "startmoney";
        private static final String TAG_CT_ID = "ct_id";
        private static final String TAG_CITY_NAME = "city_name";
        String errorString = null;

        public void showResult() {
            try {
                System.out.println("멤버멤버멤버");
                JSONObject jsonObject = new JSONObject(mJsonString);
                System.out.println("111111");
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                System.out.println("222222");
                Reviews.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String member_id = item.getString(TAG_MEMBER_ID);
                    String tel = item.getString(TAG_TEL);
                    String name = item.getString(TAG_NAME);
                    String nickname = item.getString(TAG_NICKNAME);
                    String email = item.getString(TAG_EMAIL);
                    String agerange = item.getString(TAG_AGERANGE);
                    String gender = item.getString(TAG_GENDER);
                    String birthday = item.getString(TAG_BIRTHDAY);
                    String profileimg = item.getString(TAG_PROFILEIMG);
                    String startmoney = item.getString(TAG_STARTMONEY);
                    String ct_id = item.getString(TAG_CT_ID);
                    MainActivity.user_city.setText(item.getString(TAG_CITY_NAME));
                    System.out.println(MainActivity.user_city);
                    Members.add(new MemberDTO(Integer.parseInt(member_id), tel, name, nickname, email, agerange, gender, birthday, profileimg, startmoney, Integer.parseInt(ct_id)));
                    System.out.println("12");
                }
                System.out.println("**************");
                System.out.println(Members);
                System.out.println("**************");
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            } finally {
                if(Members.size()==0){
                    MainActivity.flag = true;

                }else{
                    MainActivity.textView.setText(Members.get(0).name);
                    MainActivity.textView1.setText(Members.get(0).email);
                    MainActivity.user_money.setText(Members.get(0).startmoney);
                }



            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ----멤버---- " + result);
            if (result == null) {
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];


            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            System.out.println(searchKeyword3);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/member.php";
            System.out.println(serverURL);
            String postParameters = "email=" + searchKeyword1 + "&name=" + searchKeyword2 + "&city_name=" + searchKeyword3; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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

    public static class Money extends AsyncTask<String, Void, String> {
        String errorString = null;

        public static void showResult() {
            try {
                System.out.println("리뷰리뷰리뷰");
                JSONObject jsonObject = new JSONObject(mJsonString);
                System.out.println("111111");
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                System.out.println("222222");
                Reviews.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println("1");
                    JSONObject item = jsonArray.getJSONObject(i);
                    System.out.println("2");
                    String review_id = item.getString(TAG_REVIEWID);
                    System.out.println("3");
                    String store_id = item.getString(TAG_STORE_ID);
                    System.out.println("4");
                    String user_id = item.getString(TAG_ME_ID);
                    System.out.println("5");
                    String user_name = item.getString(TAG_USERNAME);
                    String date = item.getString(TAG_DATE);
                    System.out.println("6");
                    String score = item.getString(TAG_SCORE);
                    System.out.println("7");
                    String text = item.getString(TAG_REVIEWTXt);
                    System.out.println("8");
                    Reviews.add(new ReviewDTO(Integer.parseInt(review_id), Integer.parseInt(store_id), Integer.parseInt(user_id), user_name, date, Double.parseDouble(score), text));
                    System.out.println("9");
                }
                System.out.println("**************");
                System.out.println(Reviews);
                System.out.println("**************");
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ----리뷰---- " + result);
            if (result == null) {
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String searchKeyword4 = params[3];
            String searchKeyword5 = params[4];
            String searchKeyword6 = params[5];

            System.out.println(searchKeyword1);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/review.php";
            System.out.println(serverURL);
            String postParameters = "st_id=" + searchKeyword1 + "&function=" + searchKeyword2 + "&me_id=" + searchKeyword3 + "&score=" + searchKeyword4 + "&reviewTXT=" + searchKeyword5 + "&date=" + searchKeyword6; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
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
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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
