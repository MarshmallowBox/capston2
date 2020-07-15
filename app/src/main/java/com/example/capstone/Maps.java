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

import java.util.ArrayList;
import java.util.Objects;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Maps extends Fragment implements OnMapReadyCallback, LocationListener// Fragment 클래스를 상속받아야한다
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public static NaverMap naverMap;
    public static InfoWindow infoWindow;
    public static Location beforeLocationPoint;
    public static LatLng beforeCameraPoint;
    public static double beforeZoomLevel;
    public static SlidingUpPanelLayout slidingUpPanel;
    public static Marker singleMarker;
    static ArrayList<Marker> Markers;
    public DbCon.MarkerAdapter markerAdapter;
    private MapView mapView;
    private FusedLocationSource locationSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_maps, container, false);

        //현재위치 받아오기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //mapView 활성화
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //슬라이딩업패널
        slidingUpPanel = view.findViewById(R.id.sliding_layout);
        slidingUpPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Called when a sliding pane's position changes.
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                Called when a sliding panel state changes.

//                if(newState == PanelState.EXPANDED){
//                    mLayout.setPanelHeight(70*4);
//                } else{
//                    mLayout.setPanelHeight(0);
//                }

            }
        });
        slidingUpPanel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Provides an on click for the portion of the main view that is dimmed.
                slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //클러스터링한 마커들
        Markers = new ArrayList<>();
        //싱글마커
        singleMarker = new Marker();

        return view;
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        Maps.naverMap = naverMap;

        //실내지도
        naverMap.setIndoorEnabled(true);

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

        //줌레벨 설정
        naverMap.setCameraPosition(new CameraPosition(naverMap.getCameraPosition().target, 15));
        naverMap.setMinZoom(11);

        //위치받아오는소스 활성화
        naverMap.setLocationSource(locationSource);

        //이전위치값들 초기화
        beforeCameraPoint = naverMap.getCameraPosition().target;
        beforeLocationPoint = new Location(NETWORK_PROVIDER);
        beforeLocationPoint.setLatitude(0);
        beforeLocationPoint.setLongitude(0);
        beforeZoomLevel = naverMap.getCameraPosition().zoom;
        //인포뷰생성
        infoWindow = new InfoWindow();

        //커스텀인포뷰클래스 가져오기
        infoWindow.setAdapter(new InfoWindowAdapter(Objects.requireNonNull(getActivity())));
        //커스텀인포뷰클래스 클릭시 인포팝업띄우기
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


        //현재위치버튼 클릭 이벤트
        naverMap.addOnOptionChangeListener(new NaverMap.OnOptionChangeListener() {
            @Override
            public void onOptionChange() {
                //모드 가져오기
                LocationTrackingMode mode = naverMap.getLocationTrackingMode();
                //face모드 제거
                if (mode == LocationTrackingMode.Face)
                    naverMap.setLocationTrackingMode(LocationTrackingMode.None);

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
                if (beforeCameraPoint != null) {
                    if ((reason == -3) && (Distance.getDistance(beforeLocationPoint, naverMap.getCameraPosition().target) == 0)
                            && (Distance.getDistance(beforeCameraPoint, naverMap.getCameraPosition().target) > 5)
                            && (LocationTrackingMode.Follow == naverMap.getLocationTrackingMode())) {
                        beforeCameraPoint = naverMap.getCameraPosition().target;
//                        singleMarkers.setMap(null);


                        if (markerAdapter != null) {
                            markerAdapter.cancel(true);
                            markerAdapter = null;
                        }
                        markerAdapter = new DbCon.MarkerAdapter(getActivity(), naverMap, Markers);
                        Log.i("DataAdapter", "현위치");
                        if (markerAdapter != null) {
                            markerAdapter.execute(String.valueOf(beforeCameraPoint.latitude), String.valueOf(beforeCameraPoint.longitude),String.valueOf(MainActivity.user_city.getText()));
                        }
                    }

                    if (((reason == -1) && (Distance.getDistance(beforeCameraPoint, naverMap.getCameraPosition().target) > Distance.getClusterDist(naverMap.getCameraPosition().zoom - 1)))//드래그
                            || ((reason == -2) && Math.abs(beforeZoomLevel - naverMap.getCameraPosition().zoom) >= 1))
                        /*|| (reason == 0))*/ { //마커클릭
                        beforeCameraPoint = naverMap.getCameraPosition().target;
                        beforeZoomLevel = naverMap.getCameraPosition().zoom;
                        singleMarker.setMap(null);

                        if (markerAdapter != null) {
                            markerAdapter.cancel(true);
                            markerAdapter = null;
                        }
                        markerAdapter = new DbCon.MarkerAdapter(getActivity(), naverMap, Markers);
                        Log.i("DataAdapter", naverMap.getCameraPosition().zoom + "드래그" + Distance.getClusterDist(naverMap.getCameraPosition().zoom - 1));
                        if (markerAdapter != null) {
                            markerAdapter.execute(String.valueOf(beforeCameraPoint.latitude), String.valueOf(beforeCameraPoint.longitude),String.valueOf(MainActivity.user_city.getText()));
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
                if (beforeLocationPoint != null) { //이전 현재위치가 생성되었을때만
                    if (Distance.getDistance(beforeLocationPoint, location) > 5) { //위치변화가 5m 이상일때만
                        //locationSource.getLastLocation() 과 location는 같다
                        beforeLocationPoint = location;
                        if (PlaceList.isCheckedButtonNear) {
                            PlaceList.button_near.performClick();
                        }
                    }
                }
            }
        });


        //Follow모드로 셋팅해야 시작시 현위치로 카메라이동
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}


