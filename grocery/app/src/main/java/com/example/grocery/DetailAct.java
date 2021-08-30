package com.example.grocery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailAct extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Intent intent;
    private TextView tv_name, tv_item, tv_price, tv_location, tv_quick;
    private String name, item, price, location,star, uri, quick;
    private ImageView iv_icon,iv_star_1,iv_star_2,iv_star_3,iv_star_4,iv_star_5;
    private Button btn_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_detail);
        context = getApplicationContext();
        intent = getIntent();
        name = intent.getStringExtra("name");
        item = intent.getStringExtra("item");
        price = intent.getStringExtra("price");
        star = intent.getStringExtra("star");
        location = intent.getStringExtra("location");
        uri = intent.getStringExtra("uri");
        quick = intent.getStringExtra("quick");

        btn_confirm = findViewById(R.id.btn_complete);
        btn_confirm.setOnClickListener(this);

        iv_star_1 = findViewById(R.id.iv_star_1);
        iv_star_2 = findViewById(R.id.iv_star_2);
        iv_star_3 = findViewById(R.id.iv_star_3);
        iv_star_4 = findViewById(R.id.iv_star_4);
        iv_star_5 = findViewById(R.id.iv_star_5);

        tv_name = findViewById(R.id.tv_name1);
        tv_item = findViewById(R.id.tv_item1);
        tv_price = findViewById(R.id.tv_price1);
        tv_location = findViewById(R.id.tv_location1);
        tv_quick = findViewById(R.id.tv_quick1);

        tv_name.setText(name);
        tv_item.setText(item);
        tv_price.setText(price);
        tv_location.setText(location);
        tv_quick.setText(quick);

        iv_icon = findViewById(R.id.iv_icon);

        Uri ur = Uri.parse(uri);
        Glide.with(context).load(ur).into(iv_icon);


        switch (star) {
            case "1":
                iv_star_1.setVisibility(View.VISIBLE);
                break;

            case "2":
                iv_star_1.setVisibility(View.VISIBLE);
                iv_star_2.setVisibility(View.VISIBLE);
                break;

            case "3":
                iv_star_1.setVisibility(View.VISIBLE);
                iv_star_2.setVisibility(View.VISIBLE);
                iv_star_3.setVisibility(View.VISIBLE);
                break;

            case "4":
                iv_star_1.setVisibility(View.VISIBLE);
                iv_star_2.setVisibility(View.VISIBLE);
                iv_star_3.setVisibility(View.VISIBLE);
                iv_star_4.setVisibility(View.VISIBLE);
                break;

            case "5":
                iv_star_1.setVisibility(View.VISIBLE);
                iv_star_2.setVisibility(View.VISIBLE);
                iv_star_3.setVisibility(View.VISIBLE);
                iv_star_4.setVisibility(View.VISIBLE);
                iv_star_5.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onClick(View view) {

        if(view.equals(btn_confirm))
        {
            finish();
        }
    }
}
