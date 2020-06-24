package com.example.capstone;

public class ReviewDTO {

    public int ID;
    public int FranchiseID;
    public int userID;
   //일단뺌 public String userName;
    public String date;
    public double score;
    public String text;

    public ReviewDTO(int ID, int FranchiseID, int userID, String date, double score, String text) { //유저네임 빠져있음
        this.ID = ID;
        this.FranchiseID = FranchiseID;
        this.userID = userID;
        //일단뺌this.userName = userName;
        this.date = date;
        this.score = score;
        this.text = text;
    }
}
