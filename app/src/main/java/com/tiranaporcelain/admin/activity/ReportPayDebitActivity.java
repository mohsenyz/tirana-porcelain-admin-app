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
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportDao;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.TimeUtils;
import com.tiranaporcelain.admin.utils.ViewUtils;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                    invalidatePerson(customer.getId().intValue());
                }
                break;
        }
    }


    @OnClick(R.id.submit)
    void submit() {
        long date = (long) this.date.getTag();
        int personId = (int) this.personName.getTag();
        int price = Integer.parseInt(
                this.price.getText().toString()
        );
        String description = this.description.getText().toString();
        if (description.trim().isEmpty())
            description = "تسویه حساب تاریخ " + this.date.getText().toString();

        Report report = new Report();
        report.setDate(date);
        report.setPersonId(personId);
        report.setTransactionType(Report.TRANSACTION_SALARY);
        report.setTransactionPrice(price);
        report.setDescription(description);
        report.setType(Report.TYPE_TRANSACTION);
        DaoManager.session().getReportDao().save(report);
        Toasty.success(this, "گزارش ثبت شد").show();
        finish();
    }


    void invalidatePerson(final int personId) {
        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        ReportDao reportDao = DaoManager.session().getReportDao();
                        List<Report> list = reportDao.queryBuilder()
                                .where(ReportDao.Properties.PersonId.eq(personId))
                                .list();
                        int price = 0;
                        for (Report report : list) {
                            if (report.getType() == Report.TYPE_WORKING_REPORT) {
                                price += report.getTotalProductPrice();
                                price += Math.round(TimeUtils.minAsHour(report.getExtraTime()) * report.getExtraTimePrice());
                                price += Math.round(TimeUtils.minAsHour(report.getWorkingTime()) * report.getWorkingTimePrice());
                            } else if (report.getType() == Report.TYPE_TRANSACTION) {
                                price -= report.getTransactionPrice();
                            }
                        }
                        e.onNext(price);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        int mPrice = (int) o;
                        price.setText(LocaleUtils.e2f(String.valueOf(mPrice)));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        price.clearFocus();
                    }
                });
    }
}
