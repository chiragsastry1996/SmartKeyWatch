package com.volvo.smartkeywatch.MainMenu;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.volvo.smartkeywatch.R;
import com.volvo.smartkeywatch.Utils.CenterZoomLayoutManager;
import com.volvo.smartkeywatch.Utils.LinePagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends WearableActivity {
    @BindView(R.id.main_menu_activity) RelativeLayout relativeLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.back) ImageView backbutton;
    List<MainMenu> mainMenuList;
    MainMenuAdapter mainMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);

        mainMenuList = new ArrayList<>();

        backbutton.setVisibility(View.VISIBLE);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final RecyclerView.LayoutManager mLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        mainMenuList.add(new MainMenu("ABCD"));
        mainMenuAdapter = new MainMenuAdapter(MainMenuActivity.this, mainMenuList);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        recyclerView.setAdapter(mainMenuAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    backbutton.setVisibility(View.GONE);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = ((CenterZoomLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
                if(firstVisibleItem == 0) {
                    backbutton.setVisibility(View.VISIBLE);
                }
                /* Log.e ("VisibleItem", String.valueOf(firstVisibleItem));*/

            }
        });

    }

}
