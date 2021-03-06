package com.example.capstone;

public class MoneyDTO {

    public String id;
    public String date;
    public String hour;
    public String text;
    public int money;


    public MoneyDTO(String id, String date, String hour, String text, int money) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.text = text;
        this.money = money;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setMoney(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


}
