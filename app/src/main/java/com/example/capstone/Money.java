package com.example.capstone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Money extends Fragment // Fragment 클래스를 상속받아야한다
{
    RecyclerView mRecyclerView = null;
    MoneyRecyclerViewAdapter mAdapter = null;
    ArrayList<MoneyDTO> mList = new ArrayList<MoneyDTO>();
    TextView money = null;
    EditText editText = null;
    EditText editMoney = null;
    Button input = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_money, container, false);
        money = view.findViewById(R.id.money);
        editText = view.findViewById(R.id.money_input_text);
        editMoney = view.findViewById(R.id.money_input_money);
        input = view.findViewById(R.id.money_input);

        mRecyclerView = view.findViewById(R.id.money_recyclerview);
        mAdapter = new MoneyRecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //DB에서 불러올부분 밑에는 보여주기용 임시데이터

        mList.add(new MoneyDTO("2020-20-20 20:20", "aaaa", 1000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(0).money));

        mList.add(new MoneyDTO("2020-20-20 20:20", "bbbb", 2000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(1).money));

        mList.add(new MoneyDTO("2020-20-20 20:20", "cccc", 3000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(2).money));


        mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String formatDate = sdfNow.format(date);

                MoneyDTO moneyDTO = new MoneyDTO(formatDate,String.valueOf(editText.getText()),Integer.parseInt(String.valueOf(editMoney.getText())));
                //moneyDTO DB로 보내고
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//DB에서 불러올부분 밑에는 보여주기용 임시데이터
                mList.clear();
                mList.add(new MoneyDTO("2020-20-20 20:20", "aaaa", 1000));
                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(0).money));

                mList.add(new MoneyDTO("2020-20-20 20:20", "bbbb", 2000));
                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(1).money));

                mList.add(new MoneyDTO("2020-20-20 20:20", "cccc", 3000));
                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(2).money));

                mList.add(moneyDTO);
                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - moneyDTO.money));


                mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃
            }
        });

        return view;
    }
}
