package com.tiranaporcelain.admin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.tiranaporcelain.admin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetCountActivity extends BaseActivity {

    @BindView(R.id.input_count)
    EditText inputCount;

    int defaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_count);
        ButterKnife.bind(this);
        Intent data = getIntent();
        if (data != null) {
            defaultValue = data.getIntExtra("count", 0);
        }
        inputCount.setText("" + defaultValue);
    }

    @OnClick(R.id.submit)
    void onSubmit(){
        Intent i = new Intent();
        i.putExtra("count", Integer.parseInt(inputCount.getText().toString()));
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
