package com.example.capstone;

import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;

public class ReviewDTO {

    public int ID;
    public int FranchiseID;
    public int userID;
    public double score;
    public String text;

    public ReviewDTO(int ID, int FranchiseID, int userID, double score, String text) {
          this.ID=ID;
          this.FranchiseID=FranchiseID;
          this.userID=userID;
          this.score=score;
          this.text=text;
    }
}
