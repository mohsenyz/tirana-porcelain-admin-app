package com.tiranaporcelain.admin.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.tiranaporcelain.admin.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ReportWorkActivity extends BaseActivity implements
        DateSetListener, View.OnClickListener{

    JDF currentDate = new JDF();
    DatePicker datePicker;

    @BindView(R.id.input_date)
    EditText date;

    @BindView(R.id.product_container)
    LinearLayout productContainer;

    View productItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_work);
        ButterKnife.bind(this);
        init();
        addProductItem();
    }


    void init() {
        date.setText(currentDate.getIranianDate());
    }


    @OnTouch(R.id.input_date)
    boolean onDateTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            if (!rect.contains(view.getLeft() + (int) motionEvent.getX(),
                    view.getTop() + (int) motionEvent.getY()))
                return true;
            if (getDatePicker().isVisible())
                return true;
            getDatePicker().show(getSupportFragmentManager(), "DatePicker");
        }
        return true;
    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        currentDate = new JDF(calendar);
        date.setText(currentDate.getIranianDate());
        date.clearFocus();
    }


    public synchronized DatePicker getDatePicker() {
        if (datePicker == null) {
            datePicker = new DatePicker.Builder()
                    .future(true)
                    .theme(R.style.DatePickerDialog)
                    .date(currentDate)
                    .build(this);
        }
        return datePicker;
    }


    @OnClick(R.id.add_product)
    void addProductItem() {
        productItem = LayoutInflater.from(this).inflate(R.layout.ac_report_work_product_item,
                productContainer,
                false);
        TransitionManager.beginDelayedTransition(productContainer);
        productContainer.addView(productItem);
        productItem.findViewById(R.id.delete).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                TransitionManager.beginDelayedTransition(productContainer);
                productContainer.removeView((View) v.getParent().getParent().getParent());
                break;
        }
    }
}
