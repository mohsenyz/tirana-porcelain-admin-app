package com.tiranaporcelain.admin.presenters.dialog;

import android.view.View;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.dialog.ProductSettingView;
import com.tiranaporcelain.admin.models.SimpleListModel;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.models.db.TransactionDao;
import com.tiranaporcelain.admin.models.db.TransactionProduct;
import com.tiranaporcelain.admin.models.db.TransactionProductDao;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphj on 10/23/2017.
 */

public class ProductSettingPresenterImpl implements ProductSettingPresenter {

    ProductSettingView view;

    public ProductSettingPresenterImpl(ProductSettingView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList() {
        List<SimpleListModel> list = new ArrayList<>();
        SimpleListModel model = new SimpleListModel("ویرایش", R.drawable.ic_gray_edit);
        list.add(model);
        model = new SimpleListModel("نمودار تغییر قیمت", R.drawable.ic_gray_line_chart);
        list.add(model);
        model = new SimpleListModel("خروجی گرفتن از بارکد", R.drawable.ic_gray_texture);
        list.add(model);
        model = new SimpleListModel("افزایش تعداد", R.drawable.ic_uppward, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.increase();
            }
        });
        list.add(model);
        view.setAdapter(list);
    }


    @Override
    public void increaseProduct(Product product, int count) {
        if (count <= 0)
            return;

        ProductDao productDao = DaoManager.session().getProductDao();
        TransactionProductDao transactionProductDao = DaoManager.session().getTransactionProductDao();
        TransactionDao transactionDao = DaoManager.session().getTransactionDao();

        product.setCount(product.getCount() + count);
        productDao.save(product);

        Transaction transaction = new Transaction();
        transaction.setCreatedAt(System.currentTimeMillis() / 1000L);
        transaction.setType(Transaction.TYPE_INCOMING);
        transactionDao.save(transaction);

        TransactionProduct transactionProduct = new TransactionProduct();
        transactionProduct.setTransactionId(transaction.getId().intValue());
        transactionProduct.setProductId(product.getId().intValue());
        transactionProduct.setCount(count);

        transactionProductDao.save(transactionProduct);
        view.increasedSuccessfully();
    }
}
