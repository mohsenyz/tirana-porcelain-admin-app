package com.tiranaporcelain.admin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;
import com.tiranaporcelain.admin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeReaderActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.zxing)
    ZXingScannerView scannerView;

    String serial = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
        scannerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                scannerView.toggleFlash();
                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        submit.setText(rawResult.getText());
        serial = rawResult.getText();
        scannerView.resumeCameraPreview(this);
    }

    @OnClick(R.id.submit)
    void onSubmit(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("serial", serial);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
