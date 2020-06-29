package com.example.capstone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class MoneyRecyclerViewAdapter extends RecyclerView.Adapter<MoneyRecyclerViewAdapter.ViewHolder> {

    public static ArrayList<MoneyDTO> moneyDTOS = null;
    private Context context;
    MoneyRecyclerViewAdapter(ArrayList<MoneyDTO> reviews) {
        moneyDTOS =reviews;
    }
    TextView datetv, hourtv;
    String showdate, showtime, showid;
    static int leftovermoney;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView date, text, money, hour;
        Button month_btn,time_btn;

        public ViewHolder(View view) {
            super(view);
            this.date = view.findViewById(R.id.money_item_date);
            this.text = view.findViewById(R.id.money_item_text);
            this.money = view.findViewById(R.id.money_item_money);
            this.hour = view.findViewById(R.id.money_item_hour);
            view.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        // 4. 컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:  // 5. 편집 항목을 선택시
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        // 다이얼로그를 보여주기 위해 money_popup.xml 파일을 사용합니다.
                        View view = LayoutInflater.from(context)
                                .inflate(R.layout.money_popup, null, false);
                        builder.setView(view);
    //                    final EditText editTextusing = (EditText) view.findViewById(R.id.usingtext);
    //                    final EditText editTextprice = (EditText) view.findViewById(R.id.pricetext);
    //                    final EditText editTextdate = (EditText) view.findViewById(R.id.datetext);
    //                    final EditText editTexttime = (EditText) view.findViewById(R.id.timetext);
                        final TextInputEditText usingid = view.findViewById(R.id.usingpopuptext);
                        final TextInputEditText priceid = view.findViewById(R.id.pricepopuptext);
                        final TextInputEditText dateid = view.findViewById(R.id.datepopuptext);
                        final TextInputEditText timeid = view.findViewById(R.id.timepopuptext);

                        datetv = view.findViewById(R.id.datepopuptext);
                        hourtv = view.findViewById(R.id.timepopuptext);
                        // 6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.
                        usingid.setText(moneyDTOS.get(getAdapterPosition()).getText());
                        priceid.setText(Integer.toString(moneyDTOS.get(getAdapterPosition()).getMoney()));
                        dateid.setText(moneyDTOS.get(getAdapterPosition()).getDate());
                        timeid.setText(moneyDTOS.get(getAdapterPosition()).getHour());

                        String where1 = usingid.getText().toString();
                        String price1 = priceid.getText().toString();
                        final String date1 = dateid.getText().toString();
                        final String time1 = timeid.getText().toString();

                        //실제 데이터에 들어가는 값
                        showdate = (moneyDTOS.get(getAdapterPosition()).getDate());
                        showtime = (moneyDTOS.get(getAdapterPosition()).getHour());
                        showid = (moneyDTOS.get(getAdapterPosition()).getId());

                        //시간, 날짜 선택
                        month_btn = view.findViewById(R.id.monthbtn);
                        time_btn = view.findViewById(R.id.timebtn);
                        month_btn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog dialog = new DatePickerDialog(context, listener,getcal(date1,0), ((getcal(date1,1))-1), getcal(date1,2));
                                dialog.show();
                            }
                        });
                        time_btn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                TimePickerDialog dialog = new TimePickerDialog(context, listener2,gettime(time1,0), gettime(time1,1), false);
                                dialog.show();
                            }
                        });


                        // done버튼 누르면
                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strusing = usingid.getText().toString();
                                int strprice = Integer.parseInt(priceid.getText().toString());
                                String strdate = dateid.getText().toString();
                                String strhour = timeid.getText().toString();
                                DbCon.Money Money = new DbCon.Money();
                                Money.execute(showid,"3",showdate,showtime,strusing,priceid.getText().toString());


                                MoneyDTO dict = new MoneyDTO(showid,showdate, showtime, strusing, strprice );


                                // 8. ListArray에 있는 데이터를 변경하고
                                moneyDTOS.set(getAdapterPosition(), dict);


                                // 9. 어댑터에서 RecyclerView에 반영하도록 합니다.
                                notifyItemChanged(getAdapterPosition());
                                dialog.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();




                        break;


                    case 1002:
                        showid = (moneyDTOS.get(getAdapterPosition()).getId());
                        DbCon.Money DeleteMoney = new DbCon.Money();
                        DeleteMoney.execute(showid,"4",showdate,showtime,"strusing","priceid.getText().toString()");
                        moneyDTOS.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), moneyDTOS.size());
                        leftovermoney();
                        Money.changeleftmoney();



                        break;
                }
                return true;
            }
        };
    }


    @NonNull
    @Override
    public MoneyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.money_item, parent, false) ;
        MoneyRecyclerViewAdapter.ViewHolder vh = new MoneyRecyclerViewAdapter.ViewHolder(view) ;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
            }
        });


        return vh ;
    }


    @Override
    public void onBindViewHolder(@NonNull MoneyRecyclerViewAdapter.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분
        MoneyDTO item = moneyDTOS.get(position) ;
        System.out.println(position+"번째 아이템 저장");
        holder.date.setText(moneyDTOS.get(position).date);
        holder.hour.setText(moneyDTOS.get(position).hour);
        holder.text.setText(moneyDTOS.get(position).text);
        holder.money.setText(String.valueOf(moneyDTOS.get(position).money));
        leftovermoney();
        Money.changeleftmoney();

    }

    @Override
    public int getItemCount() {
        //카운터
        return moneyDTOS.size();
    }


    //날짜선택란
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(context, year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            datetv.setText(year + "년 " + (++monthOfYear) + "월 " + dayOfMonth +"일");
            String month,day;
            if (monthOfYear<=9) {month = ("0"+monthOfYear);}
            else {month = (""+monthOfYear);}
            if (dayOfMonth<=9) {day = ("0"+dayOfMonth);}
            else {day = (""+dayOfMonth);}
            showdate = (year+"-"+month+"-"+day);
        }
    };

    //시간선택란
    private TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            Toast.makeText(context, hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            hourtv.setText(hourOfDay + "시 " + minute + "분");
            String time,minutes;
            if (hourOfDay<=9) {time = ("0"+hourOfDay);}
            else {time = (""+hourOfDay);}
            if (minute<=9) {minutes = ("0"+minute);}
            else {minutes = (""+minute);}
            showtime = (time+":"+minutes);
        }
    };

    // 0:년  1:월  2:일
    public int getcal(String data, int a){
        StringTokenizer st = new StringTokenizer(data,"-");
        int ret[] = new int[4];
        for(int i = 0 ; i < 3 ; i++ ){
            ret[i]=Integer.parseInt(st.nextToken());
        }
        switch(a){
            case 0: return ret[0];
            case 1: return ret[1];
            case 2: return ret[2];
            default : return 0;
        }
    };

    // 0:시 1:분
    public int gettime(String data, int a){
        StringTokenizer st = new StringTokenizer(data,":");
        int ret[] = new int[4];
        for(int i = 0 ; i < 2 ; i++ ){
            ret[i]=Integer.parseInt(st.nextToken());
        }
        switch(a){
            case 0: return ret[0];
            case 1: return ret[1];
            default : return 0;
        }
    };

    //처음잔액 여기다가 넣어주면 됨
    public static void leftovermoney(){
        int orimoney= MainActivity.originmoney;
        int usingmoney = 0;
        for(int i = 0 ; i < moneyDTOS.size() ; i++){
            usingmoney += moneyDTOS.get(i).getMoney();
        }
        leftovermoney = orimoney-usingmoney;
        System.out.print("남은금액 : " + leftovermoney);
    };

}


