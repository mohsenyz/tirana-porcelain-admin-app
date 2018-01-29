package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.NewCategoryView;
import com.tiranaporcelain.admin.presenters.NewCategoryPresenter;
import com.tiranaporcelain.admin.presenters.NewCategoryPresenterImpl;
import com.tiranaporcelain.admin.utils.DaoManager;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCategoryActivity extends BaseActivity implements NewCategoryView {

    @BindView(R.id.input_storage)
    EditText storage;

    @BindString(R.string.err_invalid_storage_name)
    String errStorageName;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.submit)
    Button submit;

    NewCategoryPresenter presenter;

    int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_storage);
        ButterKnife.bind(this);
        presenter = new NewCategoryPresenterImpl(this);
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("id", -1);
        if (categoryId != -1) {
            title.setText("ویرایش محصول");
            storage.setText(DaoManager.session().getCategoryDao().load((long) categoryId).getName());
            submit.setText("ویرایش");
        }
    }

    @Override
    public void invalidStorageName() {
        storage.setError(errStorageName);
    }


    @OnClick(R.id.submit)
    void onSubmit(){
        if (categoryId == -1) {
            presenter.newStorage(storage.getText().toString());
        } else {
            presenter.updateStorage(storage.getText().toString(), categoryId);
        }
    }


    @Override
    public void finishActivity() {
        finish();
    }
}
