package com.example.capstone;


import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Objects;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Maps extends Fragment implements OnMapReadyCallback, LocationListener// Fragment 클래스를 상속받아야한다
{
    //    public static final String TAG = "DemoActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public static NaverMap naverMap;
    public static InfoWindow infoWindow;
    public static Location beforeLocation;
    public static LatLng beforeCamera;
    public static double beforezoom;
    ///
    public static SlidingUpPanelLayout mLayout;
    public static Marker singleMarkers;
    static ArrayList<Marker> Markers;
    public DbCon.DataAdapter dataAdapter;
    private MapView mapView;
    private FusedLocationSource locationSource;

    //    public MarkerAdapter markerAdapter;
    ///
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_maps, container, false);

        //현재위치 받아오기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


//DB로 받아올 부분 건강식품 일반휴게음식 유통업 약국 귀금속 세탁소 컴퓨터 문구용품 학원 레져업소 보건위생 휴대폰

//        DbClass.GetData task= new DbClass.GetData();
//        task.execute("","");
        //여기에 현재 위치(위도, 경도) 넣으면 됩니당 그런데 만약 현재 위치에 상관없이 모든 데이터를 다불러올거라면 인자가 필요없긴함...
//        task.execute(String.valueOf(beforeCamera.latitude),String.valueOf(beforeCamera.longitude));//여기에 현재 위치(위도, 경도) 넣으면 됩니당 그런데 만약 현재 위치에 상관없이 모든 데이터를 다불러올거라면 인자가 필요없긴함...
        //서버에서 DB가져오는 모든 경우의수 다 따져볼것


        //mapView 활성화
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mLayout = view.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                if(newState == PanelState.EXPANDED){
//                    mLayout.setPanelHeight(70*4);
//                } else{
//                    mLayout.setPanelHeight(0);
//                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        Markers = new ArrayList<>();
        singleMarkers = new Marker();
        return view;
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        Maps.naverMap = naverMap;


        //인포뷰생성
        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindowAdapter(Objects.requireNonNull(getActivity())));//클래스 커스텀한거 가져오기

        infoWindow.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                final FranchiseDTO tags = (FranchiseDTO) overlay.getTag();

                assert tags != null;
                Intent intent = new Intent(getActivity(), InfoPopupActivity.class);
                intent.putExtra("id", tags.id);
                intent.putExtra("name", tags.name);
                intent.putExtra("address", tags.address);
                intent.putExtra("category", tags.category);
                intent.putExtra("tel", tags.tel);
                intent.putExtra("latitude", tags.latitude);
                intent.putExtra("longitude", tags.longitude);
                intent.putExtra("reviewCount", 0);
                startActivity(intent);

                return true;
            }
        });

        //지도클릭시 인포뷰 닫기
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                infoWindow.close();
            }
        });

        //실내지도
        naverMap.setIndoorEnabled(true);

        //https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMapOptions.html
        //컨텐츠 패딩
        naverMap.setContentPadding(0, 35 * 3, 0, 20 * 3);

        //UI관련 설정을 담당하는 클래스
        UiSettings uiSettings = naverMap.getUiSettings();
        //틸트 비활성화(지도를 위아래로 기울이는 기능)
        uiSettings.setTiltGesturesEnabled(false);
        //기존의 현재위치버튼 비활성화
        uiSettings.setLocationButtonEnabled(false);
        //기존의 줌버튼 비활성화
        uiSettings.setZoomControlEnabled(false);

        //커스텀 현재위치버튼 활성화
        LocationButtonView locationButtonView = getActivity().findViewById(R.id.map_location);
        locationButtonView.setMap(naverMap);
        //커스텀 줌버튼 활성화
        ZoomControlView zoomControlView = getActivity().findViewById(R.id.map_zoom);
        zoomControlView.setMap(naverMap);

        naverMap.setCameraPosition(new CameraPosition(naverMap.getCameraPosition().target, 15));
        naverMap.setMinZoom(11);

        //위치받아오는소스 활성화
        naverMap.setLocationSource(locationSource);

        //이전위치값들 초기화
        beforeCamera = naverMap.getCameraPosition().target;
        beforeLocation = new Location(NETWORK_PROVIDER);
        beforeLocation.setLatitude(0);
        beforeLocation.setLongitude(0);
        beforezoom = naverMap.getCameraPosition().zoom;

        //현재위치버튼 클릭 이벤트
        naverMap.addOnOptionChangeListener(new NaverMap.OnOptionChangeListener() {
            @Override
            public void onOptionChange() {


                //모드 가져오기
                LocationTrackingMode mode = naverMap.getLocationTrackingMode();

                //face모드 제거
                if (mode == LocationTrackingMode.Face)
                    naverMap.setLocationTrackingMode(LocationTrackingMode.None);

                //나침반 사용여부
                //CameraPosition cameraPosition = naverMap.getCameraPosition();

                //Follow모드일때 나침반 사용
                locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow);
            }
        });

        //카메라위치 변화감지
        //특정 거리 이동시마다 beforeCamera초기화시켜주고 그값과 현제 카메라값 비교해서 마커 최신화
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                //드래그 -1 f, 마커클릭 0 t, 현위치버튼 -3 tf,
                //System.out.println("reasonreason:"+reason);
                if (beforeCamera != null) {
                    if ((reason == -3) && (Distance.getDistance(beforeLocation, naverMap.getCameraPosition().target) == 0)
                            && (Distance.getDistance(beforeCamera, naverMap.getCameraPosition().target) > 5)
                            && (LocationTrackingMode.Follow == naverMap.getLocationTrackingMode())) {
                        beforeCamera = naverMap.getCameraPosition().target;
//                        singleMarkers.setMap(null);


                        if (dataAdapter != null) {
                            dataAdapter.cancel(true);
                            dataAdapter = null;
                        }
                        dataAdapter = new DbCon.DataAdapter(getActivity(), naverMap, Markers);
                        Log.i("DataAdapter", "현위치");
                        if (dataAdapter != null) {
                            dataAdapter.execute(String.valueOf(beforeCamera.latitude), String.valueOf(beforeCamera.longitude));
                        }
                    }

                    if (((reason == -1) && (Distance.getDistance(beforeCamera, naverMap.getCameraPosition().target) > Distance.getClusterDist(naverMap.getCameraPosition().zoom - 1)))//드래그
                            || ((reason == -2) && Math.abs(beforezoom - naverMap.getCameraPosition().zoom) >= 1))
                        /*|| (reason == 0))*/ { //마커클릭
                        beforeCamera = naverMap.getCameraPosition().target;
                        beforezoom = naverMap.getCameraPosition().zoom;
                        singleMarkers.setMap(null);

                        if (dataAdapter != null) {
                            dataAdapter.cancel(true);
                            dataAdapter = null;
                        }
                        dataAdapter = new DbCon.DataAdapter(getActivity(), naverMap, Markers);
                        Log.i("DataAdapter", naverMap.getCameraPosition().zoom + "드래그" + Distance.getClusterDist(naverMap.getCameraPosition().zoom - 1));
                        if (dataAdapter != null) {
                            dataAdapter.execute(String.valueOf(beforeCamera.latitude), String.valueOf(beforeCamera.longitude));
                        }
                    }
                }
//                System.out.println(reason+"::"+naverMap.getCameraPosition().zoom);
            }
        });

        //현재위치 변화감지
        //감도 조정하기
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                if (beforeLocation != null) { //이전 현재위치가 생성되었을때만
                    if (Distance.getDistance(beforeLocation, location) > 5) { //위치변화가 5m 이상일때만
                        //locationSource.getLastLocation() 과 location는 같다
                        beforeLocation = location;
                        if (PlaceList.isCheckedButtonNear) {
                            PlaceList.button_near.performClick();
                        }
                    }
                }
            }
        });

        System.out.println(naverMap.getCameraPosition().zoom);
        //Follow모드로 셋팅해야 시작시 현위치로 카메라이동
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//        naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(37.2157095973366,126.965258516445)));

//        Marker TL = new Marker(naverMap.getCameraPosition().target.offset(500,-500));
//        TL.setCaptionText("TL");
//        TL.setMap(naverMap);
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getActivity(), "현재위치 위도: "+String.valueOf(locationSource.getLastLocation().getLatitude())+" 경도: "+String.valueOf(locationSource.getLastLocation().getLongitude()), Toast.LENGTH_SHORT).show();
//        if (naverMap == null || location == null) {
//            return;
//        }
//
//        LatLng coord = new LatLng(location);
//
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
//        locationOverlay.setVisible(true);
//        locationOverlay.setPosition(coord);
//        locationOverlay.setBearing(location.getBearing());
//
//        naverMap.moveCamera(CameraUpdate.scrollTo(coord));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //위치권한관련 메소드 최소화하고 다시킬때도 작동함
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity(), "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //인포뷰 Override 클래스
    private static class InfoWindowAdapter extends InfoWindow.ViewAdapter {
        @NonNull
        private final Context context;
        private View rootView;
        private TextView name;
        private TextView address;

        private InfoWindowAdapter(@NonNull Context context) {
            this.context = context;
        }


        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {

            if (rootView == null) {
                rootView = View.inflate(context, R.layout.view_custom_info_window, null);
                name = rootView.findViewById(R.id.view_custom_info_window_title);
                address = rootView.findViewById(R.id.view_custom_info_window_category);
            }

            if (infoWindow.getMarker() != null) {
                FranchiseDTO tags = (FranchiseDTO) infoWindow.getMarker().getTag();
                assert tags != null;
                name.setText(tags.name);
                address.setText(tags.address);
            }
            return rootView;
        }
    }

}


