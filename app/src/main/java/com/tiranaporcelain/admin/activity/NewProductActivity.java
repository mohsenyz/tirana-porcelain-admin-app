package com.tiranaporcelain.admin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.NewProductView;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.presenters.NewProductPresenter;
import com.tiranaporcelain.admin.presenters.NewProductPresenterImpl;

import org.parceler.Parcels;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class NewProductActivity extends BaseActivity implements NewProductView {

    @BindView(R.id.input_name)
    EditText name;

    @BindView(R.id.input_serial)
    EditText serial;

    @BindView(R.id.input_price)
    EditText price;

    @BindView(R.id.input_customer_price)
    EditText customerPrice;

    @BindView(R.id.input_category)
    EditText category;

    @BindView(R.id.input_count)
    EditText count;

    @BindView(R.id.serial_img)
    ImageView serialImage;

    @BindString(R.string.err_input_not_valid)
    String errInputNotValid;

    private Category productCategory;

    final Handler handler = new Handler();

    public static final int BARCODE_READER_REQUEST_CODE = 1, SELECT_CATEGORY_REQUEST_CODE = 2;

    NewProductPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);
        presenter = new NewProductPresenterImpl(this);
    }

    @OnTextChanged(R.id.input_serial)
    void onSerialTextChange() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(typingStatusRunnable, 1000);
    }

    @Override
    public void invalidName() {
        name.setError(errInputNotValid);
    }

    @Override
    public void invalidSerial() {
        serial.setError(errInputNotValid);
    }

    @Override
    public void invalidPrice() {
        price.setError(errInputNotValid);
    }

    @Override
    public void invalidCustomerPrice() {
        customerPrice.setError(errInputNotValid);
    }

    @Override
    public void invalidCategory() {
        category.setError(errInputNotValid);
    }

    @Override
    public void setSerial(Bitmap bitmap) {
        serialImage.setImageBitmap(bitmap);
    }

    @Override
    public void setSerial(String serial) {
        this.serial.setText(serial);
    }

    @Override
    public void invalidCount() {
        count.setError(errInputNotValid);
    }

    @Override
    public void setCategory(Category category) {
        this.productCategory = category;
        this.category.setText(category.getName());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @OnClick(R.id.submit)
    void onSubmit(){
        presenter.createProduct(
                name.getText().toString(),
                serial.getText().toString(),
                price.getText().toString(),
                customerPrice.getText().toString(),
                productCategory,
                count.getText().toString()
        );
    }


    @OnClick(R.id.pick_from_camera)
    void onRequestCamera(){
        startActivityForResult(new Intent(this, BarcodeReaderActivity.class), BARCODE_READER_REQUEST_CODE);
    }

    @OnClick(R.id.input_category)
    void onRequestCategory() {
        startActivityForResult(new Intent(this, SelectCategoryActivity.class), SELECT_CATEGORY_REQUEST_CODE);
    }

    @OnClick(R.id.pick_random)
    void onRequestRandom(){
        presenter.generateBarcode();
    }

    final Runnable typingStatusRunnable = new Runnable() {
        @Override
        public void run() {
            presenter.generateBarcode(serial.getText().toString());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String serial = data.getStringExtra("serial");
                presenter.generateBarcode(serial);
                setSerial(serial);
            }
        }

        if (requestCode == SELECT_CATEGORY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Category category = Parcels.unwrap(data.getParcelableExtra("category"));
                setCategory(category);
            }
        }
    }
}
