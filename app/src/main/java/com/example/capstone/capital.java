package com.example.capstone;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class capital extends AppCompatActivity {
    ListView listView;
    String mTitle[] = {"Facebook", "Instagram"};
    String mDescription[] = {"Facebook Description", "Instagram Description"};

    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital);
        listView = findViewById(R.id.listView);
        //View view = inflater.inflate(R.layout.activity_capital, container, false);


        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Toast.makeText(capital.this, "Facebook Description", Toast.LENGTH_SHORT).show();
                }
                if (position == 1){
                    Toast.makeText(capital.this, "Facebook Description", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rDescription[];

        MyAdapter(Context c, String title[], String description[]) {
            super(c, R.layout.activity_capital_row, R.id.MainTitle, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.activity_capital_row, parent, false);
            //ImageView images = row.findViewById(R.id.)
            TextView mainTitle = row.findViewById(R.id.MainTitle);
            TextView subTitle = row.findViewById(R.id.SubTitle);

            mainTitle.setText(rTitle[position]);
            subTitle.setText(rDescription[position]);

            return row;
        }
    }
}