package com.example.capstone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

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
    ImageButton addbtn;



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

        mList.add(new MoneyDTO("2020-20-20 20:20", "aaaa", 1000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(0).money));

        mList.add(new MoneyDTO("2020-20-20 20:20", "bbbb", 2000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(1).money));

        mList.add(new MoneyDTO("2020-20-20 20:20", "cccc", 3000));
        money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(2).money));


        mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃

//        input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Date date = new Date(System.currentTimeMillis());
//                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//                String formatDate = sdfNow.format(date);
//
//                MoneyDTO moneyDTO = new MoneyDTO(formatDate,String.valueOf(editText.getText()),Integer.parseInt(String.valueOf(editMoney.getText())));
//                //moneyDTO DB로 보내고
//                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
////DB에서 불러올부분 밑에는 보여주기용 임시데이터
//                mList.clear();
//                mList.add(new MoneyDTO("2020-20-20 20:20", "aaaa", 1000));
//                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(0).money));
//
//                mList.add(new MoneyDTO("2020-20-20 20:20", "bbbb", 2000));
//                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(1).money));
//
//                mList.add(new MoneyDTO("2020-20-20 20:20", "cccc", 3000));
//                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - mList.get(2).money));
//
//                mList.add(moneyDTO);
//                money.setText(String.valueOf(Integer.parseInt(String.valueOf(money.getText())) - moneyDTO.money));
//
//
//                mAdapter.notifyDataSetChanged(); //얘가 리사이클러뷰 아이템들 업뎃
//            }
//        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.testlayout, null);
                builder.setView(view);


                final TextInputEditText etUsername = alertLayout.findViewById(R.id.tiet_username);
                final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Login");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = etUsername.getText().toString();
                        String pass = etPassword.getText().toString();
                        Toast.makeText(getContext(), "Username: " + user + " Password: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();








//                startActivity(new Intent(getActivity().getApplicationContext(),MoneyPopupActivity.class));
//                new MoneyPopupActivity();

//                final EditText edittext = new EditText(getActivity().getApplicationContext());
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
//                builder.setTitle("AlertDialog Title");
//                builder.setMessage("AlertDialog Content");
//                builder.setView(edittext);
//                builder.setPositiveButton("입력",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getActivity().getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
//                            }
//                        });
//                builder.setNegativeButton("취소",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                builder.show();

                /////////////////////////////////////////////
            }

        });
        return view;
    }
}
