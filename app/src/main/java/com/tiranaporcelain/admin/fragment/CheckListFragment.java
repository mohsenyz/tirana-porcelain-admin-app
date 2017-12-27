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
import com.tiranaporcelain.admin.adapter.CheckListAdapter;
import com.tiranaporcelain.admin.interfaces.fragment.CheckListView;
import com.tiranaporcelain.admin.models.db.Check;
import com.tiranaporcelain.admin.presenters.fragment.CheckListPresenter;
import com.tiranaporcelain.admin.presenters.fragment.CheckListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mphj on 11/20/17.
 */

public class CheckListFragment extends Fragment implements CheckListView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    CheckListAdapter checkListAdapter;

    CheckListPresenter presenter;

    public CheckListFragment(){

    }

    public static CheckListFragment newInstance(){
        return new CheckListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CheckListPresenterImpl(this);
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
    public void setAdapter(List<Check> checks) {
        checkListAdapter = new CheckListAdapter(checks, getActivity());
        recyclerView.setAdapter(checkListAdapter);
    }
}
