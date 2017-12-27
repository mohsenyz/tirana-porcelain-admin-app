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

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.activity.AlertActivity;
import com.tiranaporcelain.admin.activity.NewTransactionReaddedActivity;
import com.tiranaporcelain.admin.adapter.TransactionReaddedListAdapter;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ReaddedListView;
import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.ReaddedListPresenter;
import com.tiranaporcelain.admin.presenters.fragment.export_activity.ReaddedListPresenterImpl;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mphj on 11/10/2017.
 */

public class ReaddedListFragment extends Fragment implements
        ReaddedListView, OnObjectItemClick<TransactionReadded> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    ReaddedListPresenter presenter;

    TransactionReadded pendingReadded;

    public static final int SELECT_READDED = 2, REMOVE_READDED = 3;

    public static ReaddedListFragment newInstance() {
        return new ReaddedListFragment();
    }

    public static ProductListFragment newInstance(boolean isForSelect) {
        ProductListFragment exportActivityProductListFragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("select", isForSelect);
        exportActivityProductListFragment.setArguments(bundle);
        return exportActivityProductListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ReaddedListPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern_layout_1, container, false);
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

    public void setupRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void loadList(List<TransactionReadded> list) {
        TransactionReaddedListAdapter transactionReaddedListAdapter = new TransactionReaddedListAdapter(list, getActivity(), this);
        recyclerView.setAdapter(transactionReaddedListAdapter);
    }

    @Override
    public void addNew(TransactionReadded transactionReadded) {
        presenter.addNew(transactionReadded);
    }

    @Override
    public List<TransactionReadded> getList() {
        return presenter.getList();
    }

    @Override
    public void onClick(View v, TransactionReadded object) {
        pendingReadded = object;
        startActivityForResult(new Intent(getActivity(), AlertActivity.class), REMOVE_READDED);
    }


    @OnClick(R.id.fab)
    void onFabClick() {
        startActivityForResult(new Intent(getActivity(), NewTransactionReaddedActivity.class), SELECT_READDED);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_READDED) {
            if (resultCode == Activity.RESULT_OK) {
                TransactionReadded transactionReadded = Parcels.unwrap(data.getParcelableExtra("readded"));
                addNew(transactionReadded);
            }
        }

        if (requestCode == REMOVE_READDED && pendingReadded != null) {
            if (resultCode == AlertActivity.SUBMIT) {
                presenter.delete(pendingReadded);
            }
        }
    }
}