package com.tiranaporcelain.admin.presenters;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tiranaporcelain.admin.interfaces.NewProductView;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.ProductPrice;
import com.tiranaporcelain.admin.models.db.ProductPriceDao;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.models.db.TransactionDao;
import com.tiranaporcelain.admin.models.db.TransactionProduct;
import com.tiranaporcelain.admin.models.db.TransactionProductDao;
import com.tiranaporcelain.admin.utils.BarcodeGenerator;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.DoubleValidator;

/**
 * Created by mphj on 10/20/2017.
 */

public class NewProductPresenterImpl implements NewProductPresenter, BarcodeGenerator.OnLoadListener {

    NewProductView view;

    public NewProductPresenterImpl(NewProductView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void createProduct(String name, String token, String price, String customerPrice, Category category, String count) {
        if (category == null) {
            view.invalidCategory();
            return;
        }

        if (!DoubleValidator.isValid(price)){
            view.invalidPrice();
            return;
        }

        if (!DoubleValidator.isValid(customerPrice)){
            view.invalidCustomerPrice();
            return;
        }

        if (TextUtils.isEmpty(name)){
            view.invalidName();
            return;
        }

        if (TextUtils.isEmpty(token)){
            view.invalidSerial();
            return;
        }

        if (!DoubleValidator.isValid(count)) {
            view.invalidCount();
            return;
        }

        long currentTime = Math.round(System.currentTimeMillis() / 1000f);
        Product product = new Product();
        product.setCreatedAt(currentTime);
        product.setName(name);
        product.setToken(token);
        product.setCategoryId(category.getId().intValue());
        product.setCount(Integer.parseInt(count));

        ProductDao productDao = DaoManager.session().getProductDao();
        productDao.save(product);
        int id = product.getId().intValue();

        // We should define price and off too

        ProductPrice productPrice = new ProductPrice();
        productPrice.setCreatedAt(currentTime);
        productPrice.setProductId(id);
        productPrice.setPrice(Double.parseDouble(price));
        productPrice.setCustomerPrice(Double.parseDouble(customerPrice));

        ProductPriceDao productPriceDao = DaoManager.session().getProductPriceDao();
        productPriceDao.save(productPrice);


        TransactionDao transactionDao = DaoManager.session().getTransactionDao();
        Transaction transaction = new Transaction();
        transaction.setCreatedAt(System.currentTimeMillis() / 1000L);
        transaction.setType(Transaction.TYPE_INCOMING);
        transactionDao.save(transaction);

        TransactionProductDao transactionProductDao = DaoManager.session().getTransactionProductDao();

        TransactionProduct transactionProduct = new TransactionProduct();
        transactionProduct.setCount(product.getCount());
        transactionProduct.setProductId(product.getId().intValue());
        transactionProduct.setTransactionId(transaction.getId().intValue());
        transactionProductDao.save(transactionProduct);

        view.finishActivity();
    }

    @Override
    public void generateBarcode(String barcode) {
        if (TextUtils.isEmpty(barcode))
            return;
        BarcodeGenerator.create(barcode, 300, 50, this);
    }

    @Override
    public void generateBarcode() {
        String randomBarcode = BarcodeGenerator.random(16);
        generateBarcode(randomBarcode);
        view.setSerial(randomBarcode);
    }

    @Override
    public void onBitmapLoad(Bitmap bitmap) {
        view.setSerial(bitmap);
    }
}
