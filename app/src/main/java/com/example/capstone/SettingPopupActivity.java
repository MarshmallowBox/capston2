package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingPopupActivity extends Activity {
    Spinner citySpinner = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting_popup);

        final String[] city = getResources().getStringArray(R.array.cityArray);
        citySpinner = findViewById(R.id.city_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, city);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        citySpinner.setAdapter(adapter);
        for(int i=0;i<city.length;i++){
            if(String.valueOf(MainActivity.user_city.getText()).equals(city[i])){
                citySpinner.setSelection(i);
            }
        }
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SettingPopupActivity.this, city[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button save = findViewById(R.id.setting_popup_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citySpinner.getSelectedItemId() == 0) {
                    Toast.makeText(SettingPopupActivity.this, "지역을 선택하지 않았습니다.\n지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    MainActivity.user_city.setText(String.valueOf(citySpinner.getSelectedItem()));
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
            Toast.makeText(SettingPopupActivity.this, "지역을 선택하지 않았습니다.\n지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if (!(String.valueOf(citySpinner.getSelectedItem()).equals(String.valueOf(MainActivity.user_city.getText())))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingPopupActivity.this);
            builder.setTitle("알림");
            builder.setMessage("변경한 지역을 저장하지 않고 닫겠습니까?");
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