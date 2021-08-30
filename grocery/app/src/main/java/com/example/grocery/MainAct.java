package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAct extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private DatabaseReference mDatabaseReferences;
    private ListAdapter listAdapter;
    private ArrayList<ValueInfo> list;
    private RecyclerView recyclerView;
    private ImageView iv_plus, iv_search;
    private EditText et_search;
    private TextView tv_quick, tv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        context = getApplicationContext();
        mDatabaseReferences = FirebaseDatabase.getInstance().getReference("grocery");

        list = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(MainAct.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(manager);

        tv_quick = findViewById(R.id.tv_quick);
        tv_quick.setOnClickListener(this);

        iv_plus = findViewById(R.id.iv_add);
        iv_plus.setOnClickListener(this);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        et_search = findViewById(R.id.et_top);

        tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);


        dataLoad();
    }

    public  void dataLoad()
    {
        mDatabaseReferences.child("list").orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.getChildrenCount() != 0)
                    {
                        ValueInfo valueInfo = dataSnapshot.getValue(ValueInfo.class);
                        list.add(valueInfo);
                    }
                }

                listAdapter = new ListAdapter(context,list);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_add:

                Intent intent = new Intent(MainAct.this, AddAct.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_search:
              String searchWord = et_search.getText().toString();
              if(searchWord != null)
              {
                  mDatabaseReferences.child("list").orderByChild("itemName").equalTo(searchWord).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {

                          list.clear();
                          for (DataSnapshot dataSnapshot : snapshot.getChildren())
                          {
                              if (dataSnapshot.getChildrenCount() != 0)
                              {
                                  ValueInfo valueInfo = dataSnapshot.getValue(ValueInfo.class);
                                  list.add(valueInfo);
                              }
                          }

                          listAdapter = new ListAdapter(context,list);
                          recyclerView.setAdapter(listAdapter);
                          listAdapter.notifyDataSetChanged();

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });
              }

              else
              {
                  Toast.makeText(context, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
              }

                break;

            case R.id.tv_quick:

                Intent intent1 = new Intent(MainAct.this, QuickAct.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.tv_home:

//                finish();
//                startActivity(getIntent());

                dataLoad();
                break;
        }
    }
}