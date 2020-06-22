package com.example.capstone;

import com.naver.maps.geometry.LatLng;

public class FranchiseDTO {

    public int id;
    public String name;
    public String address;
    public String category;
    public String tel;
    public double latitude;
    public double longitude;

    public FranchiseDTO(int id, String name, String address, String category, String tel, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LatLng getLatLng(){
        return new LatLng(latitude,longitude);
    }
}
