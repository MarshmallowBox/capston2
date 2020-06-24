package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MoneyRecyclerViewAdapter extends RecyclerView.Adapter<MoneyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MoneyDTO> moneyDTOS = null;

    MoneyRecyclerViewAdapter(ArrayList<MoneyDTO> reviews) {
        moneyDTOS =reviews;
    }


    @NonNull
    @Override
    public MoneyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.money_item, parent, false) ;
        MoneyRecyclerViewAdapter.ViewHolder vh = new MoneyRecyclerViewAdapter.ViewHolder(view) ;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent2 = new Intent(parent.getContext(), HelpPopupActivity.class);
                parent.getContext().startActivity(intent2);
            }
        });


        return vh ;
    }


    @Override
    public void onBindViewHolder(@NonNull MoneyRecyclerViewAdapter.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분
        MoneyDTO item = moneyDTOS.get(position) ;
        holder.date.setText(moneyDTOS.get(position).date);
        holder.text.setText(moneyDTOS.get(position).text);
        holder.money.setText(String.valueOf(moneyDTOS.get(position).money));

    }

    @Override
    public int getItemCount() {
        //카운터
        return moneyDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView text;
        TextView money;

        Button delete;
        ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.money_item_date);
            text = view.findViewById(R.id.money_item_text);
            money = view.findViewById(R.id.money_item_money);
        }

    }
}

