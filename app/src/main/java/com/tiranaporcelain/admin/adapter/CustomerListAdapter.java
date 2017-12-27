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

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.dialog.CustomerSettingDialog;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.utils.LocaleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 10/20/2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder> {

    List<Customer> list;

    FragmentActivity fragmentActivity;

    OnObjectItemClick<Customer> onObjectItemClick;

    public CustomerListAdapter(List<Customer> list, FragmentActivity fragmentActivity){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
    }

    public CustomerListAdapter(List<Customer> list, FragmentActivity fragmentActivity, OnObjectItemClick<Customer> click){
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.onObjectItemClick = click;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.customer_list_item, viewGroup, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder viewHolder, int i) {
        final Customer customer = list.get(i);
        if (customer.getServerId() != 0)
            viewHolder.loading.setVisibility(View.GONE);
        viewHolder.subText.setText(Html.fromHtml(
                viewHolder.text.getResources().getString(R.string.html_phone_eq).replace("xxx", LocaleUtils.englishNumberToArabic(customer.getPhone()))));
        viewHolder.text.setText(customer.getName());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onObjectItemClick != null){
                    onObjectItemClick.onClick(v, customer);
                    return;
                }
                BottomSheetDialogFragment bottomSheetDialogFragment = CustomerSettingDialog.create(customer);
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text)
        public TextView text;

        @BindView(R.id.subText)
        public TextView subText;

        @BindView(R.id.loading)
        public ImageView loading;

        @BindView(R.id.container)
        CardView container;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
