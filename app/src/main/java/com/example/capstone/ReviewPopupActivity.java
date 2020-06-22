package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class ReviewPopupActivity extends Activity {


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review_add_popup);

//        Intent intent = getIntent();
//        final ArrayList<String> data = new ArrayList<>(Objects.requireNonNull(intent.getStringArrayListExtra("data")));
//        String mode = data.get(0);
//        if (mode.equals("addReview_popup_open")) {
//            setContentView(R.layout.activity_review_add_popup);
//            int id = Integer.parseInt(data.get(1));
//            String name = data.get(2);
//            ((TextView) findViewById(R.id.review_add_popup_title)).setText(name + " 리뷰쓰기");
//            final EditText disc = findViewById(R.id.review_add_popup_disc);
//            Button submit = findViewById(R.id.review_add_popup_submit);
//
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(ReviewPopupActivity.this, "" + disc.getText(), Toast.LENGTH_SHORT).show();
////                        ArrayList<String> result = new ArrayList<>();
////                        result.add("addReview_popup_close");
////                        result.add(data.get(0)); //id
////                        result.add(data.get(1)); //name
////                        Intent intent = new Intent();
////                        intent.putStringArrayListExtra("result", result);
////                        setResult(RESULT_OK, intent);
//
//                    //액티비티(팝업) 닫기
//                    finish();
//                }
//            });
//        }


    }

    /*//확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭시
        /*if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            finish();
            return true;
        }*/
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼
        finish();
    }
}

