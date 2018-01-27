package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.ViewUtils;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ReportPayDebitActivity extends BaseActivity implements DateSetListener {


    public static final int REQUEST_PERSON = 2;


    JDF currentDate = new JDF();
    DatePicker datePicker;

    @BindView(R.id.input_date)
    EditText date;

    @BindView(R.id.person_name)
    TextView personName;

    @BindView(R.id.description_layout)
    CardView descriptionLayout;

    @BindView(R.id.main_container)
    LinearLayout mainContainer;

    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.price)
    EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pay_debit);
        ButterKnife.bind(this);
        init();
    }


    void init() {
        date.setText(currentDate.getIranianDate());
        date.setTag(Calendar.getInstance().getTime().getTime());
    }


    @OnTouch(R.id.input_date)
    boolean onDateTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (getDatePicker().isVisible() || !ViewUtils.isInViewBound(view, motionEvent))
                return true;
            getDatePicker().show(getSupportFragmentManager(), "DatePicker");
        }
        return true;
    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        currentDate = new JDF(calendar);
        date.setText(LocaleUtils.e2f(currentDate.getIranianDate()));
        date.setTag(calendar.getTime().getTime());
        date.clearFocus();
    }

    public DatePicker getDatePicker() {
        if (datePicker == null) {
            datePicker = new DatePicker.Builder()
                    .future(true)
                    .theme(R.style.DatePickerDialog)
                    .date(currentDate)
                    .build(this);
        }
        return datePicker;
    }

    @OnClick(R.id.select_person)
    void onRequestPerson() {
        startActivityForResult(
                new Intent(this, SelectCustomerActivity.class),
                REQUEST_PERSON
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERSON :
                if (resultCode == RESULT_OK) {
                    Customer customer = Parcels.unwrap(data.getParcelableExtra("customer"));
                    personName.setTag(customer.getId().intValue());
                    personName.setText(customer.getName() + " (" + LocaleUtils.e2f(customer.getPhone()) + ")");
                }
                break;
        }
    }

}
