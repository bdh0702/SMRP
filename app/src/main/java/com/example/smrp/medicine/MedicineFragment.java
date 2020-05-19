package com.example.smrp.medicine;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smrp.MainActivity;
import com.example.smrp.R;
import com.example.smrp.home.HomeFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MedicineFragment extends Fragment {
    // 배너 ViewPager
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private ListView Lst_medicine; // 등록한 약 목록(아직 구현x)
    private TextView Txt_empty; // 등록한 약이 없을 시 text메세지로 알려줌
    private ImageView Img_ic_plus; // +아이콘
    private MainActivity home;
    private final long DELAY_MS = 1000; // 자동 슬라이드를 위한 변수
    private final long PERIOD_MS = 3000; // 자동 슬라이드를 위한 변수
    private int currentPage = 0; // 자동 슬라이드를 위한 변수(현재 페이지)
    private Timer timer; // 자동 슬라이드를 위한 변수

    private int[] images= {R.drawable.slide1, R.drawable.slide2,R.drawable.slide3}; // ViewPagerAdapter에  보낼 이미지. 이걸로 이미지 슬라이드 띄어줌
    private MedicineViewModel medicineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        medicineViewModel =
                ViewModelProviders.of(this).get(MedicineViewModel.class);

        home = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.medicine_fragment, container, false);
        CircleIndicator indicator = v.findViewById(R.id.indicator); // 인디케이터

        viewPager =  v.findViewById(R.id.banner);
        adapter = new ViewPagerAdapter(getActivity(),images);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager); // 인디케이터 뷰에 추가

        //
        Lst_medicine = v.findViewById(R.id.Lst_medicine);
        Txt_empty = v.findViewById(R.id.Txt_empty);
        Lst_medicine.setEmptyView(Txt_empty);
        Img_ic_plus = v.findViewById(R.id.Img_ic_plus);
        //

        /* 클릭 이벤트들 */
        //1. +아이콘 클릭시
        Img_ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupFragment p = new PopupFragment(); // DialogFragment(약촬영, 약봉투, 처방전 팝업창을 위한 프레그먼트)
                p.show(getActivity().getSupportFragmentManager(),"popup"); //팝업 창 띄우기

            }
        });

        /* 자동 슬라이드*/
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 3) { // 현재페이지가 맨 끝일 시 맨 처음 페이지로 돌아간다.
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);




        //서버에게 사용자 ID를 보낸후  등록된 약들을 받아서 Adapter에 등록한다.

        ArrayList<ListViewItem> items = new ArrayList<ListViewItem>();
        items.add(new ListViewItem("https://lh3.googleusercontent.com/proxy/z5m61I7Jz2jDC56-WPtNa2ddl2zFUSasdcyTfqN8migJLE6xOwzbt7AsJv2wWo0B81jFvX0x4UlQKSDe6HZKKu4e7ByOnfTBZf-P9fim6zQ","hh"));
        items.add(new ListViewItem("https://lh3.googleusercontent.com/proxy/z5m61I7Jz2jDC56-WPtNa2ddl2zFUSasdcyTfqN8migJLE6xOwzbt7AsJv2wWo0B81jFvX0x4UlQKSDe6HZKKu4e7ByOnfTBZf-P9fim6zQ","hh"));
        Lst_medicine.setAdapter(new ListViewAdapter(items,getActivity()));
        return v;


    }


}
// 약 리스트(커스텀 ) - 클랫
