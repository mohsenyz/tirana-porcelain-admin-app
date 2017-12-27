package com.tiranaporcelain.admin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.adapter.CustomerListAdapter;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.interfaces.fragment.CustomerListView;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.presenters.fragment.CustomerListPresenter;
import com.tiranaporcelain.admin.presenters.fragment.CustomerListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerListFragment extends Fragment implements CustomerListView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    CustomerListAdapter customerListAdapter;

    CustomerListPresenter presenter;

    OnObjectItemClick<Customer> click;

    public CustomerListFragment(){

    }

    public static CustomerListFragment newInstance(){
        return new CustomerListFragment();
    }

    public static CustomerListFragment newInstance(boolean isForSelect){
        CustomerListFragment customerListFragment = new CustomerListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("select", isForSelect);
        customerListFragment.setArguments(bundle);
        return customerListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CustomerListPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public void setupRecyclerView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
        presenter.onResume();
        presenter.loadList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setAdapter(List<Customer> realmResults) {
        if (getArguments() != null && getArguments().getBoolean("select")){
            customerListAdapter = new CustomerListAdapter(realmResults, getActivity(), click);
        } else {
            customerListAdapter = new CustomerListAdapter(realmResults, getActivity());
        }
        recyclerView.setAdapter(customerListAdapter);
    }

    public void setOnObjectItemClickListener(OnObjectItemClick<Customer> click){
        this.click = click;
    }
}
