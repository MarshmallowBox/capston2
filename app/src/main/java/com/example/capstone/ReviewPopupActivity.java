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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        if(mode.equals("addReview_popup_open")){
            setContentView(R.layout.activity_review_add_popup);

            ((TextView) findViewById(R.id.review_popup_title)).setText(name + " 리뷰쓰기");
            final EditText text = findViewById(R.id.review_add_popup_text);
            Button submit = findViewById(R.id.review_add_popup_submit);
            final RatingBar star = findViewById(R.id.review_add_popup_star);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ReviewPopupActivity.this, "평점: "+star.getRating()+"내용: " + text.getText(), Toast.LENGTH_SHORT).show();

                    Intent addReviewIntent = new Intent(ReviewPopupActivity.this, InfoPopupActivity.class);
                    addReviewIntent.putExtra("id",id);
                    addReviewIntent.putExtra("name",name);
                    addReviewIntent.putExtra("address",address);
                    addReviewIntent.putExtra("category",category);
                    addReviewIntent.putExtra("tel",tel);
                    addReviewIntent.putExtra("latitude",latitude);
                    addReviewIntent.putExtra("longitude",longitude);
                    addReviewIntent.putExtra("reviewCount",0);
                    startActivity(addReviewIntent);

                    //액티비티(팝업) 닫기
                    finish();
                }
            });
        }

        if(mode.equals("Review_popup_open")){
            setContentView(R.layout.activity_review_popup);

            ArrayList<ReviewDTO> reviewDTOS = new ArrayList<>();
            reviewDTOS.add(new ReviewDTO(1,1,1,3.5,"111111111"));
            reviewDTOS.add(new ReviewDTO(2,2,2,3.5,"222222222"));
            reviewDTOS.add(new ReviewDTO(3,3,3,3.5,"333333333"));


            // Inflater View 만들기
            View view = (View) getLayoutInflater().inflate(R.layout.activity_review_popup, null);

            // Inflate된 View에서 Resource(ViewGroup) 얻어 오기~!
            LinearLayout bg = (LinearLayout) view.findViewById(R.id.review_popup);


            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.review_popup_recyclerview);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReviewPopupActivity.this); //리스트뷰를 띄워준다
            ReviewRecyclerViewAdapter myRecyclerViewAdapter = new ReviewRecyclerViewAdapter(reviewDTOS);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myRecyclerViewAdapter);
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
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼
        Intent addReviewIntent = new Intent(ReviewPopupActivity.this, InfoPopupActivity.class);
        addReviewIntent.putExtra("id",id);
        addReviewIntent.putExtra("name",name);
        addReviewIntent.putExtra("address",address);
        addReviewIntent.putExtra("category",category);
        addReviewIntent.putExtra("tel",tel);
        addReviewIntent.putExtra("latitude",latitude);
        addReviewIntent.putExtra("longitude",longitude);
        addReviewIntent.putExtra("reviewCount",0);
        startActivity(addReviewIntent);

        //액티비티(팝업) 닫기
        finish();
    }
}