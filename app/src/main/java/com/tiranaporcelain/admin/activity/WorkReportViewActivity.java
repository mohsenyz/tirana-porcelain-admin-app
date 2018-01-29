package com.tiranaporcelain.admin.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.components.JDF;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.models.db.ReportProduct;
import com.tiranaporcelain.admin.models.db.ReportProductDao;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.NumUtils;
import com.tiranaporcelain.admin.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkReportViewActivity extends BaseActivity {

    int reportId;
    Report report;
    List<ReportProduct> productList;

    @BindView(R.id.input_date)
    TextView date;

    @BindView(R.id.person_name)
    TextView personName;

    @BindView(R.id.input_to_time)
    TextView toTime;

    @BindView(R.id.input_from_time)
    TextView fromTime;

    @BindView(R.id.input_extra_time)
    TextView extraTime;

    @BindView(R.id.input_working_time)
    TextView workingTime;

    @BindView(R.id.extra_time_price)
    TextView extraTimePrice;

    @BindView(R.id.working_time_price)
    TextView workingTimePrice;

    @BindView(R.id.product_container)
    LinearLayout productContainer;

    @BindView(R.id.total_product_price)
    TextView totalProductPrice;

    @BindView(R.id.total_time_price)
    TextView totalTimePrice;

    @BindView(R.id.total_price)
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_view);
        ButterKnife.bind(this);
        reportId = getIntent().getIntExtra("id", -1);
        report = DaoManager.session().getReportDao().load((long) reportId);
        productList = DaoManager.session().getReportProductDao().queryBuilder()
                .where(ReportProductDao.Properties.ReportId.eq(reportId))
                .list();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(report.getDate()));
        date.setText(new JDF(calendar).getIranianDate());

        Customer customer = DaoManager.session().getCustomerDao().load((long) report.getPersonId());
        personName.setText(customer.getName() + " (" + customer.getPhone() + ")");

        toTime.setText(formatTime(report.getToTime()));
        fromTime.setText(formatTime(report.getFromTime()));
        extraTime.setText(formatTime(report.getExtraTime()) + " اضافه");
        workingTime.setText(formatTime(report.getWorkingTime()) + " کاری");

        extraTimePrice.setText(LocaleUtils.e2f(String.valueOf(report.getExtraTimePrice())) + " تومان هر ساعت اضافه");
        workingTimePrice.setText(LocaleUtils.e2f(String.valueOf(report.getWorkingTimePrice())) + " تومان هر ساعت کاری");

        totalProductPrice.setText(LocaleUtils.e2f(String.valueOf(report.getTotalProductPrice())) + " تومان");
        int price = (int) Math.round(TimeUtils.minAsHour(report.getExtraTime()) * report.getExtraTimePrice());
        price += Math.round(TimeUtils.minAsHour(report.getWorkingTime()) * report.getWorkingTimePrice());
        totalTimePrice.setText(LocaleUtils.e2f(String.valueOf(price)) + " تومان");
        totalPrice.setText(LocaleUtils.e2f(String.valueOf(price + report.getTotalProductPrice())) + " تومان");

        for (ReportProduct product : productList) {
            Category category = DaoManager.session().getCategoryDao().load((long) product.getProductId());
            View view = LayoutInflater.from(this).inflate(R.layout.report_list_item, null, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.container.setBackgroundResource(R.drawable.border_shape_green);
            viewHolder.title.setText(category.getName().trim());
            viewHolder.subtitle.setText("قیمت هر عدد :‌ " + LocaleUtils.e2f(String.valueOf(product.getPrice())) + " تومان");
            viewHolder.button.setText(LocaleUtils.e2f(String.valueOf(product.getCount())) + " عدد");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            productContainer.addView(view, layoutParams);
        }
    }

    String formatTime(int time) {
        return new StringBuilder()
                .append(LocaleUtils.e2f(NumUtils.intToString(time / 60, 2)))
                .append(":")
                .append(LocaleUtils.e2f(NumUtils.intToString(time % 60, 2)))
                .toString();
    }


    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.subtitle)
        TextView subtitle;

        @BindView(R.id.container)
        RelativeLayout container;

        @BindView(R.id.button)
        TextView button;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
