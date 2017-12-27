package com.tiranaporcelain.admin.fragment.export_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.BarcodeReaderActivity;
import com.tiranaporcelain.admin.activity.GetCountActivity;
import com.tiranaporcelain.admin.activity.SelectProductActivity;
import com.tiranaporcelain.admin.adapter.ProductListAdapter;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ProductListView;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.ProductListPresenter;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.ProductListPresenterImpl;
import com.tiranaporcelain.admin.utils.DaoManager;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by mphj on 11/10/2017.
 */

public class ProductListFragment extends Fragment implements
        ProductListView, OnObjectItemClick<Product>{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.barcode)
    FloatingActionButton barcode;

    ProductListPresenter presenter;

    Product pendingProduct;

    public static final int GET_COUNT_REQUEST = 1, SELECT_PRODUCT = 2, SCAN_BARCODE = 3;

    public static ProductListFragment newInstance(){
        return new ProductListFragment();
    }

    public static ProductListFragment newInstance(boolean isForSelect){
        ProductListFragment exportActivityProductListFragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("select", isForSelect);
        exportActivityProductListFragment.setArguments(bundle);
        return exportActivityProductListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProductListPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern_layout_2, container, false);
        ButterKnife.bind(this, view);
        fab.setVisibility(View.VISIBLE);
        setupRecyclerView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.loadList();
    }

    public void setupRecyclerView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void loadList(List<Product> list) {
        ProductListAdapter productListAdapter = new ProductListAdapter(list, getActivity(), this);
        recyclerView.setAdapter(productListAdapter);
    }

    @Override
    public void addProduct(Product product) {
        presenter.addProduct(product);
    }

    @Override
    public void errorInsufficientAmount(Product product) {
        Toasty.error(getActivity(),"متاسفانه موجودی این محصول کمتر از مقدار وارد شده است", Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorSerialNotFound() {
        Toasty.error(getActivity(), "هیچ محصولی با این شناسه یافت نشد", Toast.LENGTH_LONG).show();
    }

    @Override
    public List<Product> getList() {
        return presenter.getList();
    }

    @Override
    public void onClick(View v, Product object) {
        this.pendingProduct = object;
        startActivityForResult(new Intent(getActivity(), GetCountActivity.class), GET_COUNT_REQUEST);
    }


    @OnClick(R.id.fab)
    void onFabClick() {
        startActivityForResult(new Intent(getActivity(), SelectProductActivity.class), SELECT_PRODUCT);
    }

    @OnClick(R.id.barcode)
    void onRequestBarcodeScanner() {
        startActivityForResult(new Intent(getActivity(), BarcodeReaderActivity.class), SCAN_BARCODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_COUNT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int count = data.getIntExtra("count", 0);
                if (this.pendingProduct.getCount() < count) {
                    errorInsufficientAmount(this.pendingProduct);
                    this.pendingProduct = null;
                    return;
                }
                if (count <= 0) {
                    this.pendingProduct = null;
                    return;
                }
                this.pendingProduct.setPendingCount(count);
                addProduct(pendingProduct);
            }
        }

        if (requestCode == SELECT_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                this.pendingProduct = Parcels.unwrap(data.getParcelableExtra("product"));
                onClick(null, this.pendingProduct);
            }
        }

        if (requestCode == SCAN_BARCODE) {
            if (resultCode == Activity.RESULT_OK) {
                String serial = data.getStringExtra("serial");
                ProductDao productDao = DaoManager.session().getProductDao();
                Product product = productDao.queryBuilder()
                        .where(ProductDao.Properties.Token.eq(serial))
                        .limit(1)
                        .unique();
                if (product == null) {
                    errorSerialNotFound();
                    return;
                }
                this.pendingProduct = product;
                onClick(null, this.pendingProduct);
            }
        }
    }
}
