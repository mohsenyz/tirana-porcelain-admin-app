package com.tiranaporcelain.admin.fragment.export_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.SelectCustomerActivity;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.InfoView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ProductListView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ReaddedListView;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.InfoPresenter;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.InfoPresenterImpl;
import com.tiranaporcelain.admin.utils.LocaleUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by mphj on 11/17/17.
 */

public class InfoFragment extends Fragment implements
        InfoView{

    public static final int REQUEST_CUSTOMER = 1;

    @BindView(R.id.totalOff)
    TextView totalOff;

    @BindView(R.id.totalPrice)
    TextView totalPrice;

    @BindView(R.id.input_off)
    EditText off;

    @BindView(R.id.input_tax)
    EditText tax;

    @BindView(R.id.input_description)
    EditText description;

    @BindView(R.id.input_customer)
    EditText customerEditText;

    InfoPresenter presenter;

    ProductListView productListView;
    ReaddedListView readdedListView;
    Customer customer;

    int totalCustomerPrice = 0;

    public void setProductListView(ProductListView view) {
        this.productListView = view;
    }

    public void setReaddedListView(ReaddedListView view) {
        this.readdedListView = view;
    }

    public static InfoFragment newInstance(){
        return new InfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new InfoPresenterImpl(this);
        presenter.configWith(productListView);
        presenter.configWith(readdedListView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_product_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public int getOff() {
        try{
            return Integer.parseInt(off.getText().toString());
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getTax() {
        try{
            return Integer.parseInt(tax.getText().toString());
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public void setTotalPriceWithOff(double totalPriceWithOff) {
        totalCustomerPrice = (int) totalPriceWithOff;
        totalPrice.setText(LocaleUtils.englishNumberToArabic("" + totalPriceWithOff) + " تومان");
    }

    @Override
    public void setTotalOffPrice(double totalOffPrice) {
        totalOff.setText(LocaleUtils.englishNumberToArabic("" + totalOffPrice) + " تومان");
    }

    @Override
    public String getFactorDescription() {
        return description.getText().toString();
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @OnTextChanged({R.id.input_off, R.id.input_tax})
    public void onTextChanged() {
        presenter.load();
    }


    @OnFocusChange(R.id.input_customer)
    void onRequestCustomer(View view, boolean hasFocus) {
        if (hasFocus)
            startActivityForResult(new Intent(getActivity(), SelectCustomerActivity.class), REQUEST_CUSTOMER);
    }

    @Override
    public int getTotalCustomerPrice() {
        presenter.load();
        return totalCustomerPrice;
    }

    @Override
    public void onResume() {
        super.onResume();
        onTextChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOMER) {
            if (resultCode == Activity.RESULT_OK) {
                customer = Parcels.unwrap(data.getParcelableExtra("customer"));
                customerEditText.setText(customer.getName() + " (" + LocaleUtils.englishNumberToArabic(customer.getPhone()) + ")");
                customerEditText.clearFocus();
            }
        }
    }
}