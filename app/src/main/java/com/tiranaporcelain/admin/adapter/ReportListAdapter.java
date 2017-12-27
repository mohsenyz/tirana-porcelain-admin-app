package com.tiranaporcelain.admin.adapter;

import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.components.JDF;
import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.dialog.ReportSettingDialog;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.utils.LocaleUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 11/20/17.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    List<Transaction> list;

    FragmentActivity fragmentActivity;

    OnObjectItemClick<Transaction> onObjectItemClick;

    public ReportListAdapter(List<Transaction> list, FragmentActivity fragmentActivity){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
    }

    public ReportListAdapter(List<Transaction> list, FragmentActivity fragmentActivity, OnObjectItemClick<Transaction> click){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.onObjectItemClick = click;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.customer_list_item, viewGroup, false);
        return new ReportViewHolder(itemView);
    }

    private String dateToString(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        JDF jdf = new JDF(calendar);
        return jdf.getIranianDate();
    }


    @Override
    public void onBindViewHolder(ReportViewHolder viewHolder, int i) {
        final Transaction transaction = list.get(i);
        if (transaction.getServerId() != 0)
            viewHolder.loading.setVisibility(View.GONE);
        viewHolder.subText.setText(Html.fromHtml(
                viewHolder.text.getResources().getString(R.string.html_date_eq).replace("xxx", LocaleUtils.e2f(dateToString(transaction.getCreatedAt() * 1000l)))));
        viewHolder.text.setText("فاکتور " + transaction.getId() + " (" + LocaleUtils.e2f("" + transaction.getCustomerPrice()) + " تومان" +")");
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onObjectItemClick != null){
                    onObjectItemClick.onClick(v, transaction);
                    return;
                }
                BottomSheetDialogFragment bottomSheetDialogFragment = ReportSettingDialog.create(transaction);
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text)
        public TextView text;

        @BindView(R.id.subText)
        public TextView subText;

        @BindView(R.id.loading)
        public ImageView loading;

        @BindView(R.id.container)
        CardView container;

        public ReportViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
