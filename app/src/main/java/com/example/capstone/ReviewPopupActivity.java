package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewPopupActivity extends Activity {

    Intent intent;
    int id;
    String name;
    String address;
    String category;
    String tel;
    double latitude;
    double longitude;
    int reviewCount;
    String mode;
    DbCon.Review review;
    RecyclerView mRecyclerView = null;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        intent = getIntent();
        id = intent.getExtras().getInt("id");
        name = intent.getExtras().getString("name");
        address = intent.getExtras().getString("address");
        category = intent.getExtras().getString("category");
        tel = intent.getExtras().getString("tel");
        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");
        reviewCount = intent.getExtras().getInt("reviewCount");
        mode = intent.getExtras().getString("mode");

        if (mode.equals("Review_popup_open")) {
            setContentView(R.layout.activity_review_popup);
            ((TextView) findViewById(R.id.review_popup_title)).setText(name + " 리뷰보기");

            mRecyclerView = findViewById(R.id.review_popup_recyclerview);

            System.out.println("리뷰목록가져오기");
            System.out.println(id);
            System.out.println(name);

            if (review != null) {
                review.cancel(true);
                review = null;
            }
            review = new DbCon.Review(this,mRecyclerView);
            if (review != null) {
                review.execute(String.valueOf(id),"1","1","1","1","1");
            }


            // store_id, function, member_id, score, reviewTXT, date 순으로 보내는거임 임의로

//            ArrayList<ReviewDTO> reviewDTOS = new ArrayList<>();



            Button close = findViewById(R.id.review_popup_close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addReviewIntent = new Intent(ReviewPopupActivity.this, InfoPopupActivity.class);
                    addReviewIntent.putExtra("id", id);
                    addReviewIntent.putExtra("name", name);
                    addReviewIntent.putExtra("address", address);
                    addReviewIntent.putExtra("category", category);
                    addReviewIntent.putExtra("tel", tel);
                    addReviewIntent.putExtra("latitude", latitude);
                    addReviewIntent.putExtra("longitude", longitude);
                    addReviewIntent.putExtra("reviewCount", 0);
                    startActivity(addReviewIntent);

                    //액티비티(팝업) 닫기
                    finish();
                }
            });

        }
        if (mode.equals("addReview_popup_open")) {
            setContentView(R.layout.activity_review_add_popup);

            ((TextView) findViewById(R.id.review_add_popup_title)).setText(name + " 리뷰쓰기");
            final EditText text = findViewById(R.id.review_add_popup_text);
            Button submit = findViewById(R.id.review_add_popup_submit);
            final RatingBar star = findViewById(R.id.review_add_popup_star);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    String formatDate = sdfNow.format(date);
                    System.out.println("여기서리뷰목록에추가?");
                    //DB에 입력할부분

                    if (review != null) {
                        review.cancel(true);
                        review = null;
                    }
                    review = new DbCon.Review();
                    if (review != null) {
                        //store_id,function,member_id,score,reviewTXT,date 순으로 보내는거임 임의로
                        review.execute(String.valueOf(id),"2","2",String.valueOf(star.getRating()),String.valueOf(text.getText()),formatDate);
                    }




                    //store_id,function,member_id,score,reviewTXT,date 순으로 보내는거임 임의로
                    Intent addReviewIntent = new Intent(ReviewPopupActivity.this, InfoPopupActivity.class);
                    addReviewIntent.putExtra("id", id);
                    addReviewIntent.putExtra("name", name);
                    addReviewIntent.putExtra("address", address);
                    addReviewIntent.putExtra("category", category);
                    addReviewIntent.putExtra("tel", tel);
                    addReviewIntent.putExtra("latitude", latitude);
                    addReviewIntent.putExtra("longitude", longitude);
                    addReviewIntent.putExtra("reviewCount", 0);
                    startActivity(addReviewIntent);

                    //액티비티(팝업) 닫기
                    finish();
                }
            });
        }

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

        if (mode.equals("addReview_popup_open")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewPopupActivity.this);
            builder.setTitle("알림");
            builder.setMessage("리뷰를 저장하지 않고 닫겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent addReviewIntent = new Intent(ReviewPopupActivity.this, InfoPopupActivity.class);
                    addReviewIntent.putExtra("id", id);
                    addReviewIntent.putExtra("name", name);
                    addReviewIntent.putExtra("address", address);
                    addReviewIntent.putExtra("category", category);
                    addReviewIntent.putExtra("tel", tel);
                    addReviewIntent.putExtra("latitude", latitude);
                    addReviewIntent.putExtra("longitude", longitude);
                    addReviewIntent.putExtra("reviewCount", 0);
                    startActivity(addReviewIntent);

                    //액티비티(팝업) 닫기
                    finish();
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }else{
            finish();
        }
    }
}