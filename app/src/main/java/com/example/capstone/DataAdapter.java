package com.example.capstone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
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

class DataAdapter extends AsyncTask<String, Void, String> {
    private final String TAG_JSON = "webnautes";
    private final String TAG_ID = "id";
    private final String TAG_NAME = "name";
    private final String TAG_ADDRESS = "address";
    private final String TAG_CATEGORY = "category";
    private final String TAG_TEL = "tel";
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG = "PHP_Query";
    ArrayList<FranchiseDTO> Franchises = new ArrayList<>();        //        이거 어뎁터로 만들기


    ArrayList<Marker> Markers;
    final Activity activity;
    final NaverMap naverMap;
    Context context;
    RecyclerView recyclerView;
    String mJsonString=null;

    DataAdapter(Activity activity, NaverMap naverMap, ArrayList<Marker> Markers) {
        this.activity = activity;
        this.naverMap = naverMap;
        this.Markers=Markers;
        this.context=null;
        this.recyclerView=null;
    }
    DataAdapter(Context context, RecyclerView recyclerView) {
        this.activity = null;
        this.naverMap = null;
        this.Markers=null;
        this.context=context;
        this.recyclerView=recyclerView;
    }

    public ArrayList<FranchiseDTO> getFilteredData() {
        ArrayList<FranchiseDTO> categoryFilteredFranchises = new ArrayList<>();
        RadioButton checkedRadioButton = (RadioButton) UI.radioGroup.getChildAt(UI.radioGroup.getCheckedRadioButtonId());
        for (FranchiseDTO franchiseDTO : Franchises) {
            if (checkedRadioButton.getText().equals(franchiseDTO.category) || checkedRadioButton.getText().equals("전체")) {
                categoryFilteredFranchises.add(franchiseDTO);
            }
        }
        return categoryFilteredFranchises;
    }

    public void setMarkersOnMap() {
        ArrayList<FranchiseDTO> beforeClusterData = getFilteredData();

        Clustering cluster = new Clustering(naverMap,beforeClusterData);
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
            if( activity == null&&naverMap == null&&Markers==null&&context!=null&&recyclerView!=null){

                FranchiseRecyclerViewAdapter mAdapter = new FranchiseRecyclerViewAdapter(getFilteredData());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mAdapter.notifyDataSetChanged();

//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context); //리스트뷰를 띄워준다
//                FranchiseRecyclerViewAdapter myFranchiseRecyclerViewAdapter = new FranchiseRecyclerViewAdapter(getFilteredData());
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(myFranchiseRecyclerViewAdapter);
            }
            if(activity != null&&naverMap != null&&Markers!=null&&context==null&&recyclerView==null){
                setMarkersOnMap();

            }
        }
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("DataAdapter" , "onCancelled");
        if (mJsonString != null ) {
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
        mJsonString=result;
        executeResult();
    }

    @Override
    protected String doInBackground(String... params) {
        String searchKeyword1 = params[0];
        String searchKeyword2 = params[1];
        Log.i("DataAdapter", "doInBackground");
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
