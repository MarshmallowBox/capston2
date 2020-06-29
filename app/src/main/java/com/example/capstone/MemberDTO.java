package com.example.capstone;


public class MemberDTO {

    public int member_id;
    public String tel;
    public String name;
    public String nickname;
    public String email;
    public String agerange;
    public String gender;
    public String birthday;
    public String profileimg;
    public String startmoney;
    public int ct_id;

    public MemberDTO(int member_id, String tel, String name, String nickname, String email, String agerange, String gender, String birthday, String profileimg, String startmoney, int ct_id) {
        this.member_id = member_id;
        this.tel = tel;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.agerange = agerange;
        this.gender = gender;
        this.birthday = birthday;
        this.profileimg = profileimg;
        this.startmoney = startmoney;
        this.ct_id = ct_id;

    }
}