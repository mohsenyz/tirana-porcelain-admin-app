package com.tiranaporcelain.admin.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.NewCategoryView;
import com.tiranaporcelain.admin.presenters.NewCategoryPresenter;
import com.tiranaporcelain.admin.presenters.NewCategoryPresenterImpl;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCategoryActivity extends BaseActivity implements NewCategoryView {

    @BindView(R.id.input_storage)
    EditText storage;

    @BindString(R.string.err_invalid_storage_name)
    String errStorageName;

    NewCategoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_storage);
        ButterKnife.bind(this);
        presenter = new NewCategoryPresenterImpl(this);
    }

    @Override
    public void invalidStorageName() {
        storage.setError(errStorageName);
    }


    @OnClick(R.id.submit)
    void onSubmit(){
        presenter.newStorage(storage.getText().toString());
    }


    @Override
    public void finishActivity() {
        finish();
    }
}
