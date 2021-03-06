package com.example.smrp.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smrp.R;
import com.example.smrp.RetrofitHelper;
import com.example.smrp.RetrofitService;
import com.example.smrp.medicine.ListViewItem;
import com.example.smrp.reponse_medicine2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmDetailActivity extends AppCompatActivity {
    Context context;
    ImageView medicineImage;//약 사진
    ImageView iv_back; //뒤로가기 이미지뷰
    ImageView ic_dot;
    Button Btn_set;
    TextView medicineName,medicineEntpName,medicineChart,medicineClassName,medicineEtcOtcName,medicineEffect,medicineUsage;
    String itemSeq ,time;// intent용 변수
    private String str_image, str_name, str_seq,str_eq;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        context=this;

        SharedPreferences loginInfromation = getSharedPreferences("setting",0);
        user_id = loginInfromation.getString("id","");

        //Init
        Btn_set = findViewById(R.id.btn_set);
        medicineImage=findViewById(R.id.iv_medicine);
        iv_back=findViewById(R.id.iv_back);
        ic_dot = findViewById(R.id.ic_dot);

        medicineName=findViewById(R.id.tv_medicine_name) ;    //약이름
        medicineEntpName=findViewById(R.id.tv_entpName);//약 제조사
        medicineChart=findViewById(R.id.tv_chart);//약성상
        medicineClassName=findViewById(R.id.tv_className);//약분류
        medicineEtcOtcName=findViewById(R.id.tv_etcOtcName);//약구분
        medicineEffect=findViewById(R.id.tv_effect);//약효능효과
        medicineUsage=findViewById(R.id.tv_usage);//약용법용량

        //itemSeq 받는 과정
        Intent intent =getIntent();
        itemSeq =intent.getStringExtra("itemSeq");
        time = intent.getStringExtra("time");


        RetrofitService networkService= RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<reponse_medicine2> call = networkService.findmedicine(itemSeq);
        call.enqueue(new Callback<reponse_medicine2>() {
            @Override
            public void onResponse(Call<reponse_medicine2> call, Response<reponse_medicine2> response) {
                reponse_medicine2 reponse_medicine2 =response.body();
                //Image 등록
                Glide.with(context).load(reponse_medicine2.getItemImage()).override(1200,700).fitCenter().into(medicineImage);//이미지 등록
                medicineName.setText(reponse_medicine2.getItemName());
                medicineChart.setText(reponse_medicine2.getChart());
                medicineEntpName.setText(reponse_medicine2.getEntpName());
                medicineClassName.setText(reponse_medicine2.getClassName());
                medicineEtcOtcName.setText(reponse_medicine2.getEtcOtcName());
                medicineEffect.setText(reponse_medicine2.getEffect());
                medicineUsage.setText(reponse_medicine2.getUsage());

                str_image = reponse_medicine2.getItemImage();
                str_name = reponse_medicine2.getItemName();
                str_seq = reponse_medicine2.getItemSeq();
            }

            @Override
            public void onFailure(Call<reponse_medicine2> call, Throwable t) {


            }
        });





        //뒤로가기 버튼
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //설정하기 버튼
        Btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//추가 하기 버튼을 눌렀을때 서버에게 현재 자기가 등록 한 약이 무엇이다라는 것을 알려준다.

                ArrayList<ListViewItem>list = new ArrayList<>();
                list.add(new ListViewItem(str_image,str_name,str_seq,time,str_eq)); //ListViewItem 클래스의 성질을 가지고 있는 ArrayList 객체에 정보(약 이미지url, 약 이름, 약 식별번호
                // 약 생성 일자) 추가

                Intent intent1= new Intent(getApplicationContext(),AlarmSetActivity.class);
                intent1.putExtra("list",list);
                startActivity(intent1);

            }
        });

        ic_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // dialog를 띄울 Activity에서 구현
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                bottomSheetDialog.init(user_id,itemSeq);
                bottomSheetDialog.show(getSupportFragmentManager(),"bottomSheet");
    }
});

    }
}
