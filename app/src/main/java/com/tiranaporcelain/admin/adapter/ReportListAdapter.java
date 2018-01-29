package com.tiranaporcelain.admin.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.components.JDF;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.Report;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.LocaleUtils;
import com.tiranaporcelain.admin.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 1/29/18.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder>{
    List<Report> list;
    FragmentActivity fragmentActivity;
    OnObjectItemClick<Report> onObjectItemClick;

    public ReportListAdapter(List<Report> list, FragmentActivity fragmentActivity){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
    }

    public ReportListAdapter(List<Report> list, FragmentActivity fragmentActivity, OnObjectItemClick<Report> click){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.onObjectItemClick = click;
    }

    @Override
    public ReportListAdapter.ReportViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_list_item, viewGroup, false);
        return new ReportListAdapter.ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportListAdapter.ReportViewHolder viewHolder, int i) {
        final Report report = list.get(i);
        if (report.getType() == Report.TYPE_WORKING_REPORT) {
            int price = report.getTotalProductPrice();
            price += Math.round(TimeUtils.minAsHour(report.getExtraTime()) * report.getExtraTimePrice());
            price += Math.round(TimeUtils.minAsHour(report.getWorkingTime()) * report.getWorkingTimePrice());
            viewHolder.container.setBackgroundResource(R.drawable.border_shape_green);
            viewHolder.title.setText("حقوق به مبلغ " + LocaleUtils.e2f(String.valueOf(price)) + " تومان");
        } else {
            viewHolder.container.setBackgroundResource(R.drawable.border_shape_red);
            String title = "";
            switch (report.getTransactionType()) {
                case Report.TRANSACTION_DEBT:
                    title = "بدهی";
                    break;
                case Report.TRANSACTION_HELP:
                    title = "مساعده";
                    break;
                case Report.TRANSACTION_INSURANCE:
                    title = "بیمه";
                    break;
                case Report.TRANSACTION_OTHERS:
                    title = "کسری";
                    break;
                case Report.TRANSACTION_SALARY:
                    title = "تسویه حساب";
            }
            title += " به مبلغ " + LocaleUtils.e2f(String.valueOf(report.getTransactionPrice())) + " تومان";
            viewHolder.title.setText(title);
        }
        Customer customer = DaoManager.session().getCustomerDao().load((long) report.getPersonId());
        viewHolder.subtitle.setText(customer.getName() + " ( " + LocaleUtils.e2f(customer.getPhone()) + " )");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(report.getDate()));
        viewHolder.button.setText(LocaleUtils.e2f(new JDF(calendar).getIranianDate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.subtitle)
        public TextView subtitle;

        @BindView(R.id.button)
        public TextView button;

        @BindView(R.id.container)
        public RelativeLayout container;

        public ReportViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
