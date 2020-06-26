package com.example.capstone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class Money extends Fragment // Fragment 클래스를 상속받아야한다
{
    RecyclerView mRecyclerView = null;
    MoneyRecyclerViewAdapter mAdapter = null;
    ArrayList<MoneyDTO> mList = new ArrayList<MoneyDTO>();
    static TextView money = null;
    EditText datetv,timetv,editText = null;
    EditText editMoney = null;
    Button input = null, month_day,time_day;
    ImageButton addbtn;
    String showdate, showtime;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_money, container, false);
        money = view.findViewById(R.id.money);
        //editText = view.findViewById(R.id.money_input_text);
        //editMoney = view.findViewById(R.id.money_input_money);
        //input = view.findViewById(R.id.money_input);
        //addbtn = view.findViewById(R.id.addButton);
        addbtn = (ImageButton)view.findViewById(R.id.addButton);//오른쪽 아래 '+' 버튼

        //addbtn.setOnClickListener();
        mRecyclerView = view.findViewById(R.id.money_recyclerview);
        mAdapter = new MoneyRecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //DB에서 불러올부분 밑에는 보여주기용 임시데이터

//        mList.add(new MoneyDTO("2020-12-20","20:20", "aaaa", 1000));
////        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(0).money));
//
//        mList.add(new MoneyDTO("2020-12-20","20:20", "bbbb", 2000));
//
//
//        mList.add(new MoneyDTO("2019-12-20","11:11", "cccc", 3000));


        mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.money_popup, null);
                builder.setView(view);

                // money_popup 텍스트필드 이름생성
                final TextInputEditText usingid = alertLayout.findViewById(R.id.usingpopuptext);
                final TextInputEditText priceid = alertLayout.findViewById(R.id.pricepopuptext);
                final TextInputEditText dateid = alertLayout.findViewById(R.id.datepopuptext);
                final TextInputEditText timeid = alertLayout.findViewById(R.id.timepopuptext);
                datetv = alertLayout.findViewById(R.id.datepopuptext);
                timetv = alertLayout.findViewById(R.id.timepopuptext);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("지출내역");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);


                // 현재 날짜
                Calendar now;
                now = Calendar.getInstance();
                final int currentyear = now.get(Calendar.YEAR);
                final int currentmonth = now.get(Calendar.MONTH);
                final int currentdate = now.get(Calendar.DATE);
                final int currenthour = now.get(Calendar.HOUR);
                final int currentminute = now.get(Calendar.MINUTE);

                // 달력 다이얼로그 생성
                month_day = alertLayout.findViewById(R.id.monthbtn);
                dateid.setText(currentyear + "년 " + (currentmonth+1) + "월 " + currentdate +"일");  // 초기값 오늘날짜 만들기
                String plus0month, plus0day, plus0hour, plus0minute;
                if( currentmonth <= 9 ) {
                    plus0month = ("0" + (currentmonth+1));
                } else {
                    plus0month = ("" + (currentmonth+1));
                }

                if ( currentdate <=9 ) {
                    plus0day = ("0" + currentdate);
                } else {
                    plus0day = (""+currentdate);
                }
                showdate = (currentyear+"-"+(plus0month)+"-"+plus0day);
                month_day.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, currentyear, currentmonth, currentdate);
                        dialog.show();
                    }
                });

                //시간 다이얼로그 생성
                time_day = alertLayout.findViewById(R.id.timebtn);
                timeid.setText(currenthour + "시 " + currentminute + "분");
                if ( currenthour <= 9){
                    plus0hour = ("0" + currenthour);
                } else {
                    plus0hour = ("" + currenthour);
                }
                if ( currentminute <= 9 ){
                    plus0minute = ("0" + currentminute);
                } else {
                    plus0minute = ("" + currentminute);
                }
                showtime = (plus0hour+":"+plus0minute);
                time_day.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener2, currenthour, currentminute, false);
                        dialog.show();
                    }
                });

                //취소, 확인버튼
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String where1 = usingid.getText().toString();
                        String price1 = priceid.getText().toString();
                        String date1 = dateid.getText().toString();
                        String time1 = timeid.getText().toString();
                        if ( where1.isEmpty() == true || price1.isEmpty() == true || date1.isEmpty() == true || time1.isEmpty() == true ) {  //한칸이라도 비어있다면
                            Toast.makeText(getContext(), "전체 칸을 다 입력해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            MoneyDTO moneyDTO = new MoneyDTO(showdate,showtime, where1, Integer.parseInt(price1));
                            //moneyDTO DB로 보내고
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            //리스트에 저장
                            mList.add(moneyDTO);
                            money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - moneyDTO.money));

                            mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃

                            Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = alert.create();

                dialog.show();
            }
        });
        return view;
    }

    //날짜선택란
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            datetv.setText(year + "년 " + (++monthOfYear) + "월 " + dayOfMonth +"일");
            String month,day;
            if (monthOfYear<=9) {month = ("0"+monthOfYear);}
            else {month = (""+monthOfYear);}
            if (dayOfMonth<=9) {day = ("0"+dayOfMonth);}
            else {day = (""+dayOfMonth);}
            showdate = (year+"-"+month+"-"+day);
            System.out.println("showdateinPicker : "+showdate);
        }
    };

    //시간선택란
    private TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            Toast.makeText(getContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            timetv.setText(hourOfDay + "시 " + minute + "분");
            String time,minutes;
            if (hourOfDay<=9) {time = ("0"+hourOfDay);}
            else {time = (""+hourOfDay);}
            if (minute<=9) {minutes = ("0"+minute);}
            else {minutes = (""+minute);}
            showtime = (time+":"+minutes);
            System.out.println("showtimeinPicker : "+showtime);
        }
    };
    public static void changeleftmoney(){
        money.setText(String.valueOf(MoneyRecyclerViewAdapter.leftovermoney));
    }

}
