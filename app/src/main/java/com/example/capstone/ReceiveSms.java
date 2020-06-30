package com.example.capstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class ReceiveSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Sms Received!", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();  //여러가지 타입을 저장하는 Map클래스
            SmsMessage[] msgs = null;
            String msg_from;
            int usemoneyindex = -1, remainmoneyindex = -1, cardindex = -1;
            if (bundle != null) {
                try {
                    Object[] puds = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[puds.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) puds[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String MsgBody = msgs[i].getMessageBody();
                        MsgBody = MsgBody.replace("\n", " ");
                        MsgBody = MsgBody.replace("\r", " "); //엔터 제거
                        MsgBody = MsgBody.replace(",", "");
                        MsgBody = MsgBody.replace("원", "");
                        String[] data = MsgBody.split(" ");  //스페이스 기준으로 각각 data배열에 저장

                        Date date = new Date();        //컴퓨터 시스템의 시각을 date에 가져온다.
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm:ss:S");


                        for (int j = 0; j < data.length; j++) {  //문자파싱
                            System.out.println(j + "번째 배열 : " + data[j]);
                            if (data[j].indexOf("이용액") != -1) {
                                usemoneyindex = j + 1;
                            }
                            if (data[j].indexOf("잔액") != -1) {
                                remainmoneyindex = j + 1;
                            }
                            if (data[j].indexOf("카드") != -1) {
                                cardindex = j;
                            }
                        }
                        if (!(cardindex == -1 || usemoneyindex == -1 || remainmoneyindex == -1)) {

                            System.out.println("년 : " + (date.getYear() + 1900));//date의 연도을 가져옴
                            System.out.println("월 : " + (date.getMonth() + 1));//date의 월을 가져온다
                            System.out.println("일 : " + date.getDate());    //date의 일을 가져온다.
                            //System.out.println("요일 : " + date.getDay());	//date의 요일을 가져온다.
                            System.out.println("시 : " + date.getHours());    //date의 시간을 가져온다.
                            System.out.println("분 : " + date.getMinutes());    //date의 분을 가져온다.
                            //System.out.println("초 : " + date.getSeconds());	//date의 초를 가져온다.

                            System.out.println("이용카드 : " + data[cardindex]);
                            System.out.println("이용액 : " + data[usemoneyindex]);
                            System.out.println("남은금액 : " + data[remainmoneyindex]);
                            String showdate = (date.getYear() + 1900+"-"+(date.getMonth() + 1)+"-"+date.getDate());
                            String showtime = (date.getHours()+":"+date.getMinutes());
                            String where1 = data[cardindex];
                            String price1 = data[usemoneyindex];
                            DbCon.Money Money2 = new DbCon.Money(Objects.requireNonNull(Money.container2).getContext(),Money.mRecyclerView);
                            Money2.execute(String.valueOf(DbCon.Members.get(0).member_id),"2",showdate,showtime,where1,price1);//2번째 인자가 1이면 리스트 가져오기, 2이면 집어넣기기
                        }
                        //Toast.makeText(context, "From : " + msg_from + ", Body : " + MsgBody, Toast.LENGTH_SHORT).show();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
