package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportProduct;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.NumUtils;
import com.tiranaporcelain.admin.utils.ViewUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;

public class ReportWorkActivity extends BaseActivity implements
        DateSetListener,
        View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnTouchListener{

    public static final int REQUEST_PRODUCT = 1, REQUEST_PERSON = 2;


    JDF currentDate = new JDF();
    DatePicker datePicker;
    TimePickerDialog timePickerDialog;

    @BindView(R.id.input_date)
    EditText date;

    @BindView(R.id.input_to_time)
    EditText toTime;

    @BindView(R.id.input_from_time)
    EditText fromTime;

    @BindView(R.id.input_working_time)
    EditText workingTime;

    @BindView(R.id.input_extra_time)
    EditText extraTime;

    @BindView(R.id.working_time_price)
    EditText workingTimePrice;

    @BindView(R.id.extra_time_price)
    EditText extraTimePrice;

    @BindView(R.id.product_container)
    LinearLayout productContainer;

    @BindView(R.id.person_name)
    TextView personName;

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


    @OnTouch({
            R.id.input_from_time,
            R.id.input_to_time,
            R.id.input_working_time,
            R.id.input_extra_time
    })
    boolean onTimeTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (getTimePicker().isVisible() || !ViewUtils.isInViewBound(view, motionEvent))
                return true;
            int time = view.getTag() == null ? 0 : (int) view.getTag();
            getTimePicker(view.getId(), time / 60, time % 60).show(getFragmentManager(), "TimePicker");
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


    @Override
    public void onTimeSet(int id, int hourOfDay, int minute) {
        String formattedTime = new StringBuilder()
                .append(LocaleUtils.e2f(NumUtils.intToString(hourOfDay, 2)))
                .append(":")
                .append(LocaleUtils.e2f(NumUtils.intToString(minute, 2)))
                .toString();
        int time = hourOfDay * 60 + minute;
        TextView effectedView = null;
        switch (id) {
            case R.id.input_from_time:
                effectedView = fromTime;
                break;
            case R.id.input_to_time:
                effectedView = toTime;
                break;
            case R.id.input_working_time:
                effectedView = workingTime;
                break;
            case R.id.input_extra_time:
                effectedView = extraTime;
                break;
        }
        if (effectedView != null) {
            effectedView.setText(formattedTime);
            effectedView.setTag(time);
        }
        invalidateTime(id);
    }

    private void invalidateTime(int changedItem) {
        if (fromTime.getTag() == null || toTime.getTag() == null)
            return;
        int mExtraTime = extraTime.getTag() == null ? 0 : (int) extraTime.getTag();
        int mWorkingTime = workingTime.getTag() == null ? 0 : (int) workingTime.getTag();
        int timePeriod = (int) toTime.getTag() - (int) fromTime.getTag();
        if (mWorkingTime == 0 && mExtraTime != 0) {
            mWorkingTime = timePeriod - mExtraTime;
        } else if (mExtraTime == 0 && mWorkingTime != 0) {
            mExtraTime = timePeriod - mWorkingTime;
        } else if (mExtraTime == 0 && mWorkingTime == 0) {
            mWorkingTime = timePeriod;
        }
        workingTime.setTag(mWorkingTime);
        extraTime.setTag(mExtraTime);
        workingTime.setText(formatTime(mWorkingTime) + "  (کاری)");
        extraTime.setText(formatTime(mExtraTime) +  " (اضافه)");
    }

    String formatTime(int time) {
        return new StringBuilder()
                .append(LocaleUtils.e2f(NumUtils.intToString(time / 60, 2)))
                .append(":")
                .append(LocaleUtils.e2f(NumUtils.intToString(time % 60, 2)))
                .toString();
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


    public TimePickerDialog getTimePicker(int id, int hourOfDay, int minute) {
        if (timePickerDialog == null) {
            timePickerDialog = TimePickerDialog.newInstance(
                    this,
                    hourOfDay,
                    minute,
                    true);
        }
        timePickerDialog.setId(id);
        timePickerDialog.setInitialHourOfDay(hourOfDay);
        timePickerDialog.setInitialMinute(minute);
        return timePickerDialog;
    }


    public TimePickerDialog getTimePicker() {
        return getTimePicker(0, 0, 0);
    }


    @OnClick(R.id.add_product)
    void addProductItem() {
        productItem = LayoutInflater.from(this).inflate(R.layout.ac_report_work_product_item,
                productContainer,
                false);
        TransitionManager.beginDelayedTransition(productContainer);
        productContainer.addView(productItem);
        productItem.findViewById(R.id.delete).setOnClickListener(this);
        productItem.findViewById(R.id.input_product_name).setOnTouchListener(this);
    }

    EditText pendingProductName;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (getTimePicker().isVisible() || !ViewUtils.isInViewBound(view, motionEvent))
                return true;
            if (view.getId() == R.id.input_product_name) {
                startActivityForResult(
                        new Intent(this, SelectCategoryActivity.class),
                        REQUEST_PRODUCT
                );
                pendingProductName = (EditText) view;
                return true;
            }
        }
        return false;
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


    @OnClick(R.id.select_person)
    void onRequestPerson() {
        startActivityForResult(
                new Intent(this, SelectCustomerActivity.class),
                REQUEST_PERSON
        );
    }


    @OnClick(R.id.submit)
    void submit() {
        Report report = new Report();
        int workingTime = (int) this.workingTime.getTag();
        int extraTime = (int) this.extraTime.getTag();
        int fromTime = (int) this.fromTime.getTag();
        int toTime = (int) this.toTime.getTag();

        int workingTimePrice = Integer.parseInt(
                this.workingTimePrice.getText().toString()
        );
        int extraTimePrice = Integer.parseInt(
                this.extraTimePrice.getText().toString()
        );

        report.setFromTime(fromTime);
        report.setToTime(toTime);
        report.setDate((long) date.getTag());
        report.setPersonId((int) personName.getTag());
        report.setExtraTime(extraTime);
        report.setWorkingTime(workingTime);
        report.setExtraTimePrice(extraTimePrice);
        report.setWorkingTimePrice(workingTimePrice);
        report.setType(Report.TYPE_WORKING_REPORT);
        DaoManager.session().getReportDao().save(report);
        List<ReportProduct> reportProducts = validateReportProducts(
                getProductList(report.getId().intValue())
        );
        int totalProductPrice = 0;
        for (ReportProduct reportProduct : reportProducts) {
            totalProductPrice += reportProduct.getPrice() * reportProduct.getCount();
        }
        report.setTotalProductPrice(totalProductPrice);
        DaoManager.session().getReportDao().save(report);
        DaoManager.session().getReportProductDao().saveInTx(reportProducts);
        Toasty.success(this, "گزارش ثبت شد").show();
        finish();
    }


    List<ReportProduct> validateReportProducts(ReportProduct[] reportProducts) {
        List<ReportProduct> validatedReportProducts = new ArrayList<>();
        for (ReportProduct reportProduct : reportProducts) {
            if (reportProduct != null && reportProduct.isValid())
                validatedReportProducts.add(reportProduct);
        }
        return validatedReportProducts;
    }


    ReportProduct[] getProductList(int reportId) {
        int productCount = productContainer.getChildCount();
        ReportProduct[] reportProducts = new ReportProduct[productCount];
        for (int i = 0; i < productCount; i++) {
            reportProducts[i] = collectProductFromView(productContainer.getChildAt(i), reportId);
        }
        return reportProducts;
    }


    ReportProduct collectProductFromView(View view, int reportId) {
        EditText countInput = (EditText) view.findViewById(R.id.input_count);
        EditText priceInput = (EditText) view.findViewById(R.id.input_product_price);
        EditText productName = (EditText) view.findViewById(R.id.input_product_name);
        ReportProduct reportProduct = new ReportProduct();
        try {
            reportProduct.setReportId(reportId);
            reportProduct.setCount(Integer.parseInt(countInput.getText().toString()));
            reportProduct.setPrice(Double.valueOf(priceInput.getText().toString()).intValue());
            reportProduct.setProductId((int) productName.getTag());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportProduct;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PRODUCT :
                if (resultCode == RESULT_OK) {
                    Category category = Parcels.unwrap(data.getParcelableExtra("category"));
                    pendingProductName.setText(category.getName());
                    pendingProductName.setTag(category.getId().intValue());
                    pendingProductName.clearFocus();
                }
                break;
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
