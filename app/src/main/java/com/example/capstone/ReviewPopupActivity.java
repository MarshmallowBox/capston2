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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

    RecyclerView mRecyclerView = null;
    ReviewRecyclerViewAdapter mAdapter = null;
    ArrayList<ReviewDTO> mList = new ArrayList<ReviewDTO>();


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

            mRecyclerView = findViewById(R.id.review_popup_recyclerview);
            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
            mAdapter = new ReviewRecyclerViewAdapter(mList);
            mRecyclerView.setAdapter(mAdapter);

            // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String formatDate = sdfNow.format(date);

            System.out.println("리뷰목록가져오기");
//            ArrayList<ReviewDTO> reviewDTOS = new ArrayList<>();
            mList.add(new ReviewDTO(1, 1, 1,"1", formatDate,3.5, "1111111111"));
            mList.add(new ReviewDTO(2, 2, 2,"2", formatDate, 3.5, "2222222222"));
            mList.add(new ReviewDTO(3, 3, 3,"3", formatDate, 3.5, "3333333333"));
            mList.add(new ReviewDTO(4, 4, 4,"4", formatDate, 3.5, "4444444444"));
            mList.add(new ReviewDTO(5, 5, 5,"5", formatDate, 3.5, "5555555555"));
            mList.add(new ReviewDTO(6, 6, 6,"6", formatDate, 3.5, "6666666666"));

            mAdapter.notifyDataSetChanged();
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

            ((TextView) findViewById(R.id.review_popup_title)).setText(name + " 리뷰쓰기");
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
                    //st_id: id, me_id: ?, score: star.getRating(), reviewTXT: text.getText(), date: formatDate
                    Toast.makeText(ReviewPopupActivity.this, "st_id:"+ id+ "me_id: ?"+ "score:"+ star.getRating()+ "reviewTXT:"+ text.getText()+ "date:"+ formatDate, Toast.LENGTH_SHORT).show();

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
//        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼
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
}