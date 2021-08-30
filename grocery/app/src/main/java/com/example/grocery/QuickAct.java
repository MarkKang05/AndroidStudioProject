package com.example.grocery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuickAct extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private DatabaseReference mDatabaseReferences;
    private ListAdapter listAdapter;
    private ArrayList<ValueInfo> list;
    private RecyclerView recyclerView;
    private TextView tv_quick, tv_home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_quick);

        context = getApplicationContext();
        mDatabaseReferences = FirebaseDatabase.getInstance().getReference("grocery");

        list = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(QuickAct.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(manager);

        tv_quick = findViewById(R.id.tv_quick);
        tv_quick.setOnClickListener(this);
        tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);

        mDatabaseReferences.child("list").orderByChild("quick").equalTo("가능").addValueEventListener(new ValueEventListener() {
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
        case R.id.tv_home:

            Intent intent = new Intent(QuickAct.this, MainAct.class);
            startActivity(intent);
            finish();
            break;

    }
    }
}
