package com.example.capstone;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.Objects;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Maps extends Fragment implements OnMapReadyCallback, LocationListener// Fragment 클래스를 상속받아야한다
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public static NaverMap naverMap;
    public static InfoWindow infoWindow;
    private Location beforeLocation;
    private LatLng beforeCamera;
    private MapView mapView;
    private FusedLocationSource locationSource;
    static LatLng makerlatlng = new LatLng(0,0);
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

        return view;
    }

    //리스트에 리사이클러뷰어뎁터와 연동해서
    //마커 어뎁터생성자를 통해 마커 시별로 불러와 생성후
    //주변찾기나 화면에 띄우는거는 좌표값넘겨서
    //marker.setMap(naverMap); 또는 marker.setMap(null);로 표시여부 판별
    //표시여부는 좌표와 거리간 관계로 구별
    //중복마커는 클러스터링 알고리즘 적용해보고 이용하기
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;


        //인포뷰생성
        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindowAdapter(Objects.requireNonNull(getActivity())));//클래스 커스텀한거 가져오기

        infoWindow.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                //여기를 좀더 다듬기기
                //태그를 통해 객체전달받기
                final FranchiseDTO tags = (FranchiseDTO) overlay.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle(tags.id + "\t" + tags.name);
                builder.setMessage(tags.category + "\n" + tags.tel + "\n" + tags.address + "\n길찾기(예), 전화(아니요)");

                builder.setPositiveButton("길찾기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                                builder2.setPositiveButton("T-Map",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String url = "tmap://route?goalx=" + tags.longitude + "&goaly=" + tags.latitude + "&goalname=" + tags.name + "";
                                                try {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    startActivity(intent);
                                                } catch (Exception e) {
                                                    url = "market://details?id=com.skt.tmap.ku";
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    startActivity(intent);
                                                    e.getStackTrace();
                                                }
                                            }
                                        });
                                builder2.setNegativeButton("Kakao Map",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String url = "kakaomap://route?sp=" + beforeLocation.getLatitude() + "," + beforeLocation.getLongitude() + "&ep=" + tags.latitude + "," + tags.longitude + "&by=CAR";
                                                try {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    startActivity(intent);
                                                } catch (Exception e) {
                                                    url = "market://details?id=net.daum.android.map";
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    startActivity(intent);
                                                    e.getStackTrace();
                                                }
                                            }
                                        });
                                builder2.show();
                            }
                        });
                builder.setNegativeButton("전화",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (tags.tel.toString().equals("")) {
                                    Toast.makeText(getActivity(), "번호가 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tags.tel.toString()));
                                    startActivity(intent);
                                }
                            }
                        });

                builder.show();
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

        //위치받아오는소스 활성화
        naverMap.setLocationSource(locationSource);

        //이전위치값들 초기화
        beforeCamera = naverMap.getCameraPosition().target;
        beforeLocation = new Location(NETWORK_PROVIDER);
        beforeLocation.setLatitude(0);
        beforeLocation.setLongitude(0);

//        //마커 생성
//        for (final FranchiseDTO franchise : DbClass.Franchises) {
//        final Marker marker = new Marker();
//        marker.setPosition(new LatLng(beforeCamera.latitude, beforeCamera.longitude));//위경도
//
////        marker.setIcon(OverlayImage.fromResource(R.drawable.appicon64)); //아이콘 직접 지정
//        marker.setIcon(MarkerIcons.YELLOW);//기본제공 마커
//        //특정 줌 레벨에서만 캡션이 나타나도록 지정
//        marker.setCaptionMinZoom(12);
////        marker.setCaptionMaxZoom(16);
//        //마커 크기지정 아마 3:4비율인듯
//        marker.setWidth(90);
//        marker.setHeight(120);
//
//        marker.setSubCaptionColor(Color.BLUE); //서브캡션 색상
//        marker.setSubCaptionTextSize(10); //서브캡션 크기
//        marker.setHideCollidedCaptions(true);//마커곂칠때 캡션숨기기
//        marker.setOnClickListener(new OnClickListener() {
//            @Override
//            public boolean onClick(@NonNull Overlay overlay) {
//                //클릭시 카메라 이동
//                naverMap.moveCamera(CameraUpdate.scrollTo(marker.getPosition()).animate(CameraAnimation.Easing));
//                //infoWindow에 franchises값 태그로 전달
//                makerlatlng = marker.getPosition();
//                //인포뷰 활성화
////                            infoWindow.open(marker);
//                return true;
//            }
//        });
//        marker.setMap(naverMap); //지도에 추가, null이면 안보임

//        }

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
                CameraPosition cameraPosition = naverMap.getCameraPosition();

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

                if(beforeCamera != null
                        && (LocationDistance.distance(beforeLocation,naverMap.getCameraPosition().target)==0 && reason == -3)
                        && (LocationDistance.distance(beforeCamera,naverMap.getCameraPosition().target)>5)){
                    System.out.println("현위치버튼 "+animated+reason);
                    Toast.makeText(getActivity(), "현위치버튼 lat: "+String.valueOf(beforeCamera.latitude)+"lon: "+String.valueOf(beforeCamera.longitude), Toast.LENGTH_SHORT).show();

                    DbClass.GetData task= new DbClass.GetData();
                    task.execute(String.valueOf(beforeLocation.getLatitude()),String.valueOf(beforeLocation.getLongitude()),"false");


                    beforeCamera = naverMap.getCameraPosition().target;
                }

                if(beforeCamera != null && (LocationDistance.distance(beforeCamera,naverMap.getCameraPosition().target)>250 && reason == -1)){
                    System.out.println("드래그 "+animated);
                    Toast.makeText(getActivity(), "드래그 lat: "+String.valueOf(beforeCamera.latitude)+"lon: "+String.valueOf(beforeCamera.longitude), Toast.LENGTH_SHORT).show();

//
                    DbClass.GetData task= new DbClass.GetData();
                    task.execute(String.valueOf(beforeCamera.latitude),String.valueOf(beforeCamera.longitude),"false");

                    beforeCamera = naverMap.getCameraPosition().target;


                }

                if(beforeCamera != null && reason == 0 && LocationDistance.distance(makerlatlng,naverMap.getCameraPosition().target)==0) {
                    System.out.println("마커 클릭 "+animated);
                    Toast.makeText(getActivity(), "마커 클릭 lat: "+String.valueOf(beforeCamera.latitude)+"lon: "+String.valueOf(beforeCamera.longitude), Toast.LENGTH_SHORT).show();
//
                    DbClass.GetData task= new DbClass.GetData();
                    task.execute(String.valueOf(beforeCamera.latitude),String.valueOf(beforeCamera.longitude),"false");

                    beforeCamera = naverMap.getCameraPosition().target;
                }

//
//
//
//
            }
        });

        //현재위치 변화감지
        //감도 조정하기
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                if (beforeLocation != null) { //이전 현재위치가 생성되었을때만
                    if (LocationDistance.distance(beforeLocation, location) > 5) { //위치변화가 5m 이상일때만
//
                        DbClass.GetData task= new DbClass.GetData();
                        task.execute(String.valueOf(beforeLocation.getLatitude()),String.valueOf(beforeLocation.getLongitude()),"true");
//
                        beforeLocation = location;
                        //locationSource.getLastLocation() 과 location는 같다
                    }
                }
            }
        });

        //Follow모드로 셋팅해야 시작시 현위치로 카메라이동
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
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
        private ImageView icon;
        private TextView name;
        private TextView address;


        private InfoWindowAdapter(@NonNull Context context) {
            this.context = context;
        }


        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {
            if (rootView == null) {
                rootView = View.inflate(context, R.layout.view_custom_info_window, null);
                icon = rootView.findViewById(R.id.view_custom_info_window_image);
                name = rootView.findViewById(R.id.view_custom_info_window_title);
                address = rootView.findViewById(R.id.view_custom_info_window_desc);
            }

            if (infoWindow.getMarker() != null) {
                //여러개 태그
                icon.setImageResource(R.drawable.appicon64);
                //(String) infoWindow.getMarker().getTag()로 태그값 전달받음
                FranchiseDTO tags = (FranchiseDTO) infoWindow.getMarker().getTag();
                name.setText(tags.name);
                address.setText(tags.address);

                //단독 태그
//                icon.setImageResource(R.drawable.appicon64);
                //(String) infoWindow.getMarker().getTag()로 태그값 전달받음
//                name.setText(infoWindow.getMarker().getTag() + " 번째 가맹점");
//                address.setText(infoWindow.getMarker().getTag() + " 번째 가맹점 주소");
            }

            return rootView;
        }
    }

    //좌표간 거리계산
    public static class LocationDistance {
        //Location, LatLng
        public static double distance(Location location, LatLng latLng) {
            double theta = latLng.longitude - location.getLongitude();
            double dist = Math.sin(deg2rad(latLng.latitude)) * Math.sin(deg2rad(location.getLatitude()))
                    + Math.cos(deg2rad(latLng.latitude)) * Math.cos(deg2rad(location.getLatitude())) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1609.344; //미터단위로 변환

            return (dist);
        }

        //Location, Location
        public static double distance(Location location1, Location location2) {
            double theta = location1.getLongitude() - location2.getLongitude();
            double dist = Math.sin(deg2rad(location1.getLatitude())) * Math.sin(deg2rad(location2.getLatitude()))
                    + Math.cos(deg2rad(location1.getLatitude())) * Math.cos(deg2rad(location2.getLatitude())) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1609.344; //미터단위로 변환

            return (dist);
        }

        //LatLng, LatLng
        public static double distance(LatLng latLng1, LatLng latLng2) {
            double theta = latLng1.longitude - latLng2.longitude;
            double dist = Math.sin(deg2rad(latLng1.latitude)) * Math.sin(deg2rad(latLng2.latitude))
                    + Math.cos(deg2rad(latLng1.latitude)) * Math.cos(deg2rad(latLng2.latitude)) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1609.344; //미터단위로 변환
            return (dist);
        }


        // This function converts decimal degrees to radians
        private static double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        // This function converts radians to decimal degrees
        private static double rad2deg(double rad) {
            return (rad * 180 / Math.PI);
        }

    }
}

