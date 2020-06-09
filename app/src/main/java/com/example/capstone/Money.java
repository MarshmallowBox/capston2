package com.example.capstone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Money extends Fragment // Fragment 클래스를 상속받아야한다
{
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_money, container, false);

        items = new ArrayList<String>();


                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);


        listView = view.findViewById(R.id.money_listView);
        listView.setAdapter(adapter);



        final TextView money = view.findViewById(R.id.textView2);
        final EditText editText1= view.findViewById(R.id.editText1);
        final EditText editText2= view.findViewById(R.id.editText2);
        Button button= view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();

                if (!text1.isEmpty() && !text2.isEmpty()){
                    items.add(text1+"\t"+text2+"원");
                    money.setText(String.valueOf( Integer.parseInt(money.getText().toString())-Integer.parseInt(editText2.getText().toString()) ));
                    editText1.setText("");
                    editText2.setText("");
                    adapter.notifyDataSetChanged();
                }

            }
        });





        return view;
    }
}
