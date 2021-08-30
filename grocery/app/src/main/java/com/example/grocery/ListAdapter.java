package com.example.grocery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ValueInfo> list;
    private StorageReference storageReference;
    private ArrayList<String> uriList;


    public ListAdapter (Context context , ArrayList list)
    {
        this.context = context;
        this.list = list;
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://grocery-f6b3f.appspot.com");
        uriList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_main, parent, false);

        ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        holder.tv_store.setText(list.get(position).getStoreName());
        holder.tv_item.setText(list.get(position).getItemName());
        holder.tv_price.setText(list.get(position).getPrice());

        if(storageReference!=null)
        {
            //참조객체로 부터 이미지의 다운로드 URL을 얻어오기

            // acaBossMixed 예시 = 마포학원반영강 (학원과 원장이름 이어붙인것 )

            String name = list.get(position).getStoreName();

            if(name != null) {
                storageReference.child(name).getDownloadUrl().addOnSuccessListener(uri -> {

                 list.get(position).setUri(uri.toString());

                    //다운로드 URL이 파라미터로 전달되어 옴.
                    Glide.with(context).load(uri).into(holder.iv_icon);
                });
            }

            else
            {

            }
        }

        if(list.get(position).getStar() != null) {
            switch (list.get(position).getStar()) {
                case "1":
                    holder.iv_star_1.setVisibility(View.VISIBLE);
                    break;

                case "2":
                    holder.iv_star_1.setVisibility(View.VISIBLE);
                    holder.iv_star_2.setVisibility(View.VISIBLE);
                    break;

                case "3":
                    holder.iv_star_1.setVisibility(View.VISIBLE);
                    holder.iv_star_2.setVisibility(View.VISIBLE);
                    holder.iv_star_3.setVisibility(View.VISIBLE);
                    break;

                case "4":
                    holder.iv_star_1.setVisibility(View.VISIBLE);
                    holder.iv_star_2.setVisibility(View.VISIBLE);
                    holder.iv_star_3.setVisibility(View.VISIBLE);
                    holder.iv_star_4.setVisibility(View.VISIBLE);
                    break;

                case "5":
                    holder.iv_star_1.setVisibility(View.VISIBLE);
                    holder.iv_star_2.setVisibility(View.VISIBLE);
                    holder.iv_star_3.setVisibility(View.VISIBLE);
                    holder.iv_star_4.setVisibility(View.VISIBLE);
                    holder.iv_star_5.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_store, tv_item, tv_price;
        private ImageView iv_star_1,iv_star_2,iv_star_3,iv_star_4,iv_star_5,iv_icon;
        private ConstraintLayout const_list;

        public ViewHolder (View view)
        {
            super(view);

            const_list = view.findViewById(R.id.const_list);

            tv_store = view.findViewById(R.id.tv_store);
            tv_item = view.findViewById(R.id.tv_item);
            tv_price = view.findViewById(R.id.tv_price);

            iv_icon = view.findViewById(R.id.iv_icon);
            iv_star_1 = view.findViewById(R.id.iv_star_1);
            iv_star_2 = view.findViewById(R.id.iv_star_2);
            iv_star_3 = view.findViewById(R.id.iv_star_3);
            iv_star_4 = view.findViewById(R.id.iv_star_4);
            iv_star_5 = view.findViewById(R.id.iv_star_5);

            const_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    Intent intent = new Intent(context, DetailAct.class);
                    intent.putExtra("name", list.get(position).getStoreName());
                    intent.putExtra("item", list.get(position).getStoreName());
                    intent.putExtra("price", list.get(position).getPrice());
                    intent.putExtra("star", list.get(position).getStar());
                    intent.putExtra("location", list.get(position).getLocation());
                    intent.putExtra("uri", list.get(position).getUri());
                    intent.putExtra("quick", list.get(position).getQuick());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });


        }
    }
}
