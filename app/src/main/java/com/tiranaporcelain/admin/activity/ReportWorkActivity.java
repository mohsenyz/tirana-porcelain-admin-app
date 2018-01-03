package com.tiranaporcelain.admin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.tiranaporcelain.admin.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

public class ReportWorkActivity extends BaseActivity implements DateSetListener {

    JDF currentDate = new JDF();

    @BindView(R.id.input_date)
    EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_work);
        ButterKnife.bind(this);
        init();
    }


    void init() {
        date.setText(currentDate.getIranianDate());
    }

    @OnFocusChange(R.id.input_date)
    void onDateFocus(boolean hasFocus) {
        if (!hasFocus)
            return;
        DatePicker datePicker = new DatePicker.Builder()
                .future(true)
                .theme(R.style.DatePickerDialog)
                .date(currentDate)
                .build(this);
        datePicker.show(getSupportFragmentManager(), "DatePicker");
    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        currentDate = new JDF(calendar);
        date.setText(currentDate.getIranianDate());
    }
}
