package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.adapter.SectionsPagerAdapter;
import com.tiranaporcelain.admin.interfaces.DashboardView;
import com.tiranaporcelain.admin.presenters.DashboardPresenter;
import com.tiranaporcelain.admin.presenters.DashboardPresenterImpl;
import com.tiranaporcelain.admin.utils.ViewAnimator;
import com.tiranaporcelain.admin.views.SimpleDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity implements DashboardView, ViewPager.OnPageChangeListener, View.OnClickListener{


    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    LinearLayout fabMenu;

    int lastPosition = SectionsPagerAdapter.HOME;


    @BindViews({
            R.id.bottombar_home,
            R.id.bottombar_reports,
            R.id.bottombar_products,
            R.id.bottombar_customers,
    })
    List<LinearLayout> bottomBar;

    DashboardPresenter presenter;

    PagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(0);
        presenter = new DashboardPresenterImpl(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        SimpleDrawable simpleDrawable = new SimpleDrawable();
        simpleDrawable.setDurationMillis(300);
        toolbar.setBackground(simpleDrawable);
        initTabs();
    }

    boolean firstFocus = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mViewPager.getCurrentItem() == 3 && !firstFocus) {
            firstFocus = true;
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doForPosition3();
                }
            }, 500);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int curr = mViewPager.getCurrentItem();
        int newCurr = curr >= 1 ? curr - 1 : curr + 1;
        mViewPager.destroyDrawingCache();
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(newCurr, false);
        mViewPager.setCurrentItem(curr, false);
    }

    private void initTabs() {
        changeCurrentTo(mSectionsPagerAdapter.getCount() - 1, true);
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++){
            bottomBar.get(i).setOnClickListener(this);
            bottomBar.get(i).setTag(mSectionsPagerAdapter.getCount() - 1 - i);
            ((TextView) bottomBar.get(i).getChildAt(1)).setText(mSectionsPagerAdapter.getPageTitle(i));
        }
    }


    private void changeCurrentTo(int position, boolean changeViewPager){
        presenter.onPageChanged(position);
        ViewAnimator.defaultScale(
                bottomBar.get(3 - lastPosition).getChildAt(1),
                (float) 1
        );
        lastPosition = position;
        if (changeViewPager)
            mViewPager.setCurrentItem(position, true);
        ViewAnimator.defaultScale(
                bottomBar.get(3 - position).getChildAt(1),
                (float) 1.2
        );
        if (position == 3 && firstFocus) {
            doForPosition3();
        } else {
            doForOther();
        }
        if (position != 2) {
            hideFabMenu();
        }
    }


    @OnClick(R.id.fab_menu_rep_work)
    public void onClickRepWork() {
        startActivity(new Intent(this, ReportWorkActivity.class));
    }


    @OnClick(R.id.fab_menu_rep_dec)
    public void onClickRepDec() {
        startActivity(new Intent(this, ReportDeficitActivity.class));
    }


    @OnClick(R.id.fab_menu_pay_debit)
    public void onClickPayDebit() {

    }


    @Override
    public void hideFabMenu() {
        fabMenu.animate()
                .translationY(70)
                .alpha(0)
                .setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fabMenu.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @Override
    public void showFabMenu() {
        fabMenu.setAlpha(0);
        fabMenu.setTranslationY(70);
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(300)
                .start();
    }

    void doForPosition3() {
        SimpleDrawable simpleDrawable = (SimpleDrawable) toolbar.getBackground();
        simpleDrawable.setBackgroundColor(Color.WHITE);
        simpleDrawable.setRippleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        simpleDrawable.start();
    }

    void doForOther() {
        SimpleDrawable simpleDrawable = (SimpleDrawable) toolbar.getBackground();
        simpleDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        simpleDrawable.setRippleColor(Color.WHITE);
        simpleDrawable.start();
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        presenter.onFabClick();
    }

    @Override
    public void startProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void endProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showFab() {
        fab.animate()
                .scaleY(1)
                .scaleX(1)
                .alpha(1)
                .setDuration(200)
                .start();
    }

    @Override
    public void hideFab() {
        fab.animate()
                .scaleX(0)
                .scaleY(0)
                .alpha(0)
                .setDuration(200)
                .start();
    }


    @Override
    public void showNewCategoryActivity() {
        startActivity(new Intent(this, NewCategoryActivity.class));
    }

    @Override
    public void showNewCustomerActivity() {
        startActivity(new Intent(this, NewCustomerActivity.class));
    }

    @Override
    public void onPageSelected(int position) {
        changeCurrentTo(position, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        changeCurrentTo(position, true);
    }
}
