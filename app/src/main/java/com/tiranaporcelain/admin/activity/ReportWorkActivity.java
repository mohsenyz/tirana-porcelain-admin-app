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
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportProduct;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;

public class ReportWorkActivity extends BaseActivity implements
        DateSetListener, View.OnClickListener{

    JDF currentDate = new JDF();
    DatePicker datePicker;

    @BindView(R.id.input_date)
    EditText date;

    @BindView(R.id.input_working_time)
    EditText workingTime;

    @BindView(R.id.input_extra_time)
    EditText extraTime;

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
        date.setTag(calendar.getTime().getTime());
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
        productItem.findViewById(R.id.input_product_name).setOnClickListener(this);
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


    @OnClick(R.id.submit)
    void submit() {
        Report report = new Report();
        int workingTime = Integer.parseInt(
                this.workingTime.getText().toString()
        );
        // @TODO Implement these
        int fromTime = 0;
        int toTime = 0;
        int extraTime = 0;
        int workingTimePrice = 0;
        int extraTimePrice = 0;

        report.setFromTime(fromTime);
        report.setToTime(toTime);
        report.setDate((long) date.getTag());
        report.setExtraTime(extraTime);
        report.setWorkingTime(workingTime);
        report.setExtraTimePrice(extraTimePrice);
        report.setWorkingTimePrice(workingTimePrice);
        report.setType(Report.TYPE_WORKING_REPORT);
        DaoManager.session().getReportDao().save(report);
        ReportProduct[] reportProducts = validateReportProducts(
                getProductList(report.getId().intValue())
        );
        DaoManager.session().getReportProductDao().saveInTx(reportProducts);
        Toasty.success(this, "گزارش ثبت شد").show();
        finish();
    }


    ReportProduct[] validateReportProducts(ReportProduct[] reportProducts) {
        List<ReportProduct> validatedReportProducts = new ArrayList<>();
        for (ReportProduct reportProduct : reportProducts) {
            if (reportProduct.isValid())
                validatedReportProducts.add(reportProduct);
        }
        return (ReportProduct[]) validatedReportProducts.toArray();
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
        EditText priceInput = (EditText) view.findViewById(R.id.input_price);
        EditText productName = (EditText) view.findViewById(R.id.input_product_name);
        ReportProduct reportProduct = new ReportProduct();
        reportProduct.setReportId(reportId);
        reportProduct.setCount(Integer.parseInt(countInput.getText().toString()));
        reportProduct.setPrice(Double.valueOf(priceInput.getText().toString()).intValue());
        reportProduct.setProductId((int) productName.getTag());
        return reportProduct;
    }
}
