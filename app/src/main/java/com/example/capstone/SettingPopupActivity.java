package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static com.example.capstone.MainActivity.mapsFrag;


public class SettingPopupActivity extends Activity {
    Spinner citySpinner = null;
    String mode = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting_popup);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        Log.d("modemodemode", "mode: " + mode);


        final String[] city = getResources().getStringArray(R.array.cityArray);
        citySpinner = findViewById(R.id.city_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, city);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        citySpinner.setAdapter(adapter);
        for (int i = 0; i < city.length; i++) {
            if (String.valueOf(MainActivity.user_city.getText()).equals(city[i])) {
                citySpinner.setSelection(i);

            }
        }
        System.out.println("!!!!!!" + MainActivity.user_city.getText() + "!!!!!!");
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SettingPopupActivity.this, city[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        final TextView q = findViewById(R.id.info_popup_title);
        if (mode != null && (mode.equals("new") || mode.equals("notmember"))) {

            q.setText("초기 지역 설정");
        }
        Button save = findViewById(R.id.setting_popup_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citySpinner.getSelectedItemId() == 0) {
                    Toast.makeText(SettingPopupActivity.this, "지역을 선택하지 않았습니다.\n지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (mode != null && mode.equals("new")) {


//                        String.valueOf(citySpinner.getSelectedItem()) -> city값
                        //DB ADD
                        //MainActivity.Member
                        if (MainActivity.Member != null) {
                            MainActivity.Member.cancel(true);
                            MainActivity.Member = null;
                        }
                        MainActivity.Member = new DbCon.MemberAdapter();
                        MainActivity.Member.execute(MainActivity.strEmail, MainActivity.strNickname, String.valueOf(citySpinner.getSelectedItem()), "1");//로그인은 했는데 처음이라 시가 저장 안되어있을경우 function이 1이다
                        System.out.println("어쩌라구");
                        MainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));
                        Log.e("88 fucking city",citySpinner.getSelectedItem()+"\nMainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));\n"+MainActivity.user_city.getText());
                        MainActivity.textView.setText(MainActivity.strNickname);
                        MainActivity.textView1.setText(MainActivity.strEmail);
                        MainActivity.drawerLayout.refreshDrawableState();
                    } else {
                        MainActivity.Member = new DbCon.MemberAdapter();
                        MainActivity.Member.execute(MainActivity.strEmail, MainActivity.strNickname, String.valueOf(citySpinner.getSelectedItem()), "2");//로그인은 했는데 처음이라 시가 저장 안되어있을경우 function이 1이다
                        System.out.println("어쩌라공");
                        MainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));
                        Log.e("97 fucking city",citySpinner.getSelectedItem()+"\nMainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));\n"+MainActivity.user_city.getText());
                        MainActivity.textView.setText(MainActivity.strNickname);
                        MainActivity.textView1.setText(MainActivity.strEmail);
                        MainActivity.drawerLayout.refreshDrawableState();
                        Log.e("야발","1"+String.valueOf(citySpinner.getSelectedItem()));
                        Log.e("야발","2"+MainActivity.user_city.getText());


                    }
                    MainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));
                    Log.e("107 fucking city",citySpinner.getSelectedItem()+"\nMainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));\n"+MainActivity.user_city.getText());

                    Log.e("야발","3"+String.valueOf(citySpinner.getSelectedItem()));
                    Log.e("야발","4"+MainActivity.user_city.getText());

                    DbCon.MarkerAdapter MarkerAdapter = null;
                    if (MainActivity.bottomNavigationView.getSelectedItemId() == R.id.mapmode) {
                        if (MarkerAdapter != null) {
                            MarkerAdapter.cancel(true);
                            MarkerAdapter = null;
                        }
                        MarkerAdapter = new DbCon.MarkerAdapter(mapsFrag.getActivity(), Maps.naverMap, Maps.Markers);
                        Log.i("MarkerAdapter", "야발 현위치");
                        if (MarkerAdapter != null) {
                            MarkerAdapter.execute(String.valueOf(Maps.beforeCameraPoint.latitude), String.valueOf(Maps.beforeCameraPoint.longitude), String.valueOf(MainActivity.user_city.getText()));
                        }
                    } else {
                        if (PlaceList.isCheckedButtonNear) {
                            PlaceList.button_near.performClick();
                        } else {
                            PlaceList.button_camera.performClick();
                        }
                    }
                    if (Maps.slidingUpPanel != null &&
                            (Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || Maps.slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                        Maps.slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }


//                Toast.makeText(SettingPopupActivity.this, String.valueOf(citySpinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭시
        /*if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            finish();
            return true;
        }*/
//        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼

        if (citySpinner.getSelectedItemId() == 0) {
//            Toast.makeText(SettingPopupActivity.this, "지역을 선택하지 않았습니다.\n지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingPopupActivity.this);
            builder.setTitle("알림");
            builder.setMessage("지역을 선택하지 않고 닫겠습니까?");
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
        } else if (!(String.valueOf(citySpinner.getSelectedItem()).equals(String.valueOf(MainActivity.user_city.getText())))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingPopupActivity.this);
            builder.setTitle("알림");
            builder.setMessage("변경한 지역을 선택하지 않고 닫겠습니까?");
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
        } else {
            finish();
        }
        Log.d("CITY", String.valueOf(citySpinner.getSelectedItem()));
        Log.d("CITY", String.valueOf(MainActivity.user_city.getText()));
    }
}