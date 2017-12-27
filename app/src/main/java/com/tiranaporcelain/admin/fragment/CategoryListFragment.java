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
import com.tiranaporcelain.admin.adapter.CategoryListAdapter;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.interfaces.fragment.CategoryListView;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.presenters.fragment.CategoryListPresenter;
import com.tiranaporcelain.admin.presenters.fragment.CategoryListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListFragment extends Fragment implements CategoryListView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    CategoryListAdapter categoryListAdapter;

    CategoryListPresenter presenter;

    OnObjectItemClick<Category> click;

    public CategoryListFragment(){

    }

    public static CategoryListFragment newInstance(){
        return new CategoryListFragment();
    }

    public static CategoryListFragment newInstance(boolean isForSelect){
        CategoryListFragment categoryListFragment = new CategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("select", isForSelect);
        categoryListFragment.setArguments(bundle);
        return categoryListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoryListPresenterImpl(this);
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

    public void setOnObjectItemClickListener(OnObjectItemClick<Category> click){
        this.click = click;
    }

    @Override
    public void setAdapter(List<Category> realmResults) {
        if (getArguments() != null && getArguments().getBoolean("select")) {
            categoryListAdapter = new CategoryListAdapter(realmResults, getActivity(), click);
        } else {
            categoryListAdapter = new CategoryListAdapter(realmResults, getActivity());
        }
        recyclerView.setAdapter(categoryListAdapter);
    }
}
