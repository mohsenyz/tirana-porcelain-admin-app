package com.tiranaporcelain.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.GetCountActivity;
import com.tiranaporcelain.admin.adapter.SimpleListAdapter;
import com.tiranaporcelain.admin.interfaces.dialog.ProductSettingView;
import com.tiranaporcelain.admin.models.SimpleListModel;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.presenters.dialog.ProductSettingPresenter;
import com.tiranaporcelain.admin.presenters.dialog.ProductSettingPresenterImpl;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by mphj on 10/23/2017.
 */

public class ProductSettingDialog extends BottomSheetDialogFragment implements ProductSettingView {


    ProductSettingPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    SimpleListAdapter simpleListAdapter;

    Product product;

    public static final int INCREASE_PRODUCT = 1;

    public static ProductSettingDialog create(Product product){
        ProductSettingDialog dialog = new ProductSettingDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("product", Parcels.wrap(Product.class, product));
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.bs_dialog_simple_setting, null);
        getDialog().setContentView(contentView);
        ButterKnife.bind(this, contentView);
        setupRecyclerView();
        if (getArguments() != null) {
            product = Parcels.unwrap(getArguments().getParcelable("product"));
        }
        presenter = new ProductSettingPresenterImpl(this);
        presenter.loadList();
        return dialog;
    }

    public void setupRecyclerView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setAdapter(List<SimpleListModel> list) {
        simpleListAdapter = new SimpleListAdapter(list);
        recyclerView.setAdapter(simpleListAdapter);
    }

    @Override
    public void increase() {
        startActivityForResult(new Intent(getActivity(), GetCountActivity.class), INCREASE_PRODUCT);
    }

    @Override
    public void increasedSuccessfully() {
        Toasty.success(getActivity(), "تعداد محصول با موفقیت افزایش یافت").show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INCREASE_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.increaseProduct(product, data.getIntExtra("count", 0));
            }
        }
    }
}
