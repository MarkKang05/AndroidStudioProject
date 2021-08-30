package com.example.grocery;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class AddAct extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_icon;
    private EditText et_name, et_item, et_price, et_location, et_star;
    private Context context;
    private Button btn_cancel, btn_complete;
    private String name, item, price, location, star;
    private Intent intent;
    private DatabaseReference mDatabaseReferences;
    private Uri pictureUri[];
    private TextView tv_possible, tv_impossible;
    private boolean possibleChecked, impossibleChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add);

        context = getApplicationContext();

        et_name = findViewById(R.id.et_name);
        et_item = findViewById(R.id.et_item);
        et_price = findViewById(R.id.et_price);
        et_star = findViewById(R.id.et_star);
        et_location = findViewById(R.id.et_location);

        iv_icon = findViewById(R.id.iv_icon);
        iv_icon.setOnClickListener(this);

        tv_impossible = findViewById(R.id.tv_impossible);
        tv_impossible.setOnClickListener(this);
        tv_possible = findViewById(R.id.tv_possible);
        tv_possible.setOnClickListener(this);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_complete = findViewById(R.id.btn_complete);
        btn_cancel.setOnClickListener(this);
        btn_complete.setOnClickListener(this);

        possibleChecked = false;
        impossibleChecked = false;

        mDatabaseReferences = FirebaseDatabase.getInstance().getReference("grocery");
        pictureUri = new Uri[1];
    }

    @Override
    public void onClick(View view) {

        if (view.equals(btn_complete)) {

            // 이미지 선택 안했을경우
            if (pictureUri[0] == null) {
                Toast.makeText(context, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_name.getText().toString() == null) {
                Toast.makeText(context, "가게이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_item.getText().toString() == null) {
                Toast.makeText(context, "품목을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_price.getText().toString() == null) {
                Toast.makeText(context, "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_star.getText().toString() == null) {
                Toast.makeText(context, "별점을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_location.getText().toString() == null) {
                Toast.makeText(context, "위치를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            else if (possibleChecked == false && impossibleChecked == false)
            {
                    Toast.makeText(context, "퀵 가능 여부를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }



            name = et_name.getText().toString();
            item = et_item.getText().toString();
            price = et_price.getText().toString();
            star = et_star.getText().toString();
            location = et_location.getText().toString();

            ValueInfo valueInfo = new ValueInfo();
            valueInfo.setStoreName(name);
            valueInfo.setItemName(item);
            valueInfo.setPrice(price);
            valueInfo.setStar(star);
            valueInfo.setLocation(location);
            if (possibleChecked) {
                valueInfo.setQuick("가능");
            } else {
                valueInfo.setQuick("불가능");
            }


                mDatabaseReferences.child("list").push().setValue(valueInfo);


                final ProgressDialog progressDialog = new ProgressDialog(AddAct.this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();

                /** 파이어베이스 Storage에 사진저장 **/
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl("gs://grocery-f6b3f.appspot.com").child(name).putFile(pictureUri[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                        Toast.makeText(context, "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddAct.this, MainAct.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                });


        } else if (view.equals(btn_cancel)) {

            Intent intent = new Intent(AddAct.this, MainAct.class);
            startActivity(intent);
            finish();
        } else if (view.equals(iv_icon)) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            //사진을 여러개 선택할수 있도록 한다
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        } else if (view.equals(tv_possible)) {
            // 이미 눌린상태
            if (possibleChecked) {
                possibleChecked = false;
                tv_possible.setBackground(getResources().getDrawable(R.drawable.border_black_square));
                tv_possible.setTextColor(Color.BLACK);
            }

            // 안눌린상태
            else {
                possibleChecked = true;
                tv_possible.setBackground(getResources().getDrawable(R.color.black));
                tv_possible.setTextColor(Color.WHITE);

                if (impossibleChecked) {
                    tv_impossible.setBackground(getResources().getDrawable(R.drawable.border_black_square));
                    tv_impossible.setTextColor(Color.BLACK);

                    impossibleChecked = false;
                }
            }

        } else if (view.equals(tv_impossible)) {
            // 이미 눌린상태
            if (impossibleChecked) {
                impossibleChecked = false;
                tv_impossible.setBackground(getResources().getDrawable(R.drawable.border_black_square));
                tv_possible.setTextColor(Color.BLACK);
            }

            // 안눌린상태
            else {
                impossibleChecked = true;
                tv_impossible.setBackground(getResources().getDrawable(R.color.black));
                tv_impossible.setTextColor(Color.WHITE);

                if (possibleChecked) {
                    tv_possible.setBackground(getResources().getDrawable(R.drawable.border_black_square));
                    tv_possible.setTextColor(Color.BLACK);
                    possibleChecked = false;
                }
            }
        }
    }


    /**
     * startActivityForResult 메소드가 실행될때 실행
     **/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    //ClipData 또는 Uri를 가져온다
                    pictureUri[0] = data.getData();
                    Uri uri = data.getData();
                    ClipData clipData = data.getClipData();
                    iv_icon.setImageURI(uri);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddAct.this, MainAct.class);
        startActivity(intent);
    }
}
