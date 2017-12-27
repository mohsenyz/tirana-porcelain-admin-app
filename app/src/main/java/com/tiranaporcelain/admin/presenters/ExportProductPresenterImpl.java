package com.tiranaporcelain.admin.presenters;

import android.support.annotation.Nullable;

import com.tiranaporcelain.admin.interfaces.ExportProductView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.InfoView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ProductListView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ReaddedListView;
import com.tiranaporcelain.admin.models.db.Check;
import com.tiranaporcelain.admin.models.db.CheckDao;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.models.db.TransactionDao;
import com.tiranaporcelain.admin.models.db.TransactionProduct;
import com.tiranaporcelain.admin.models.db.TransactionProductDao;
import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphj on 10/24/2017.
 */

public class ExportProductPresenterImpl implements ExportProductPresenter{

    ExportProductView view;

    public ExportProductPresenterImpl(ExportProductView view) {
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void submit(InfoView infoView, ProductListView productListView, ReaddedListView readdedListView, int paymentType, @Nullable Check check) {
        List<Product> products = productListView.getList();
        List<TransactionReadded> transactionReaddeds = readdedListView.getList();
        String description = infoView.getFactorDescription();
        int customerPrice = infoView.getTotalCustomerPrice();
        int tax = infoView.getTax();
        int off = infoView.getOff();
        int customerId = infoView.getCustomer().getId().intValue();

        TransactionDao transactionDao = DaoManager.session().getTransactionDao();
        Transaction transaction = new Transaction();
        transaction.setCreatedAt(System.currentTimeMillis() / 1000l);
        transaction.setCustomerId(customerId);
        transaction.setCustomerPrice(customerPrice);
        transaction.setPrice(calculateProductPrice(products));
        transaction.setDescription(description);
        transaction.setOff(off);
        transaction.setTax(tax);
        transaction.setPaymentType(paymentType);
        transaction.setReaddedPrice(calculateReaddedPrice(transactionReaddeds));
        transaction.setType(Transaction.TYPE_OUTGOING);
        transactionDao.save(transaction);

        ProductDao productDao = DaoManager.session().getProductDao();
        TransactionProductDao transactionProductDao = DaoManager.session().getTransactionProductDao();
        List<TransactionProduct> transactionProducts = new ArrayList<>();
        for (Product product : products) {
            android.util.Log.i("PRODUCT", "" + product.getCount() + "," + product.getPendingCount());
            product.setCount(product.getCount() - product.getPendingCount());
            TransactionProduct transactionProduct = new TransactionProduct();
            transactionProduct.setTransactionId(transaction.getId().intValue());
            transactionProduct.setProductId(product.getId().intValue());
            transactionProduct.setCount(product.getPendingCount());
            transactionProducts.add(transactionProduct);
        }

        transactionProductDao.saveInTx(transactionProducts);
        productDao.saveInTx(products);

        if (check != null) {
            CheckDao checkDao = DaoManager.session().getCheckDao();
            check.setTransactionId(transaction.getId().intValue());
            checkDao.save(check);
        }
        view.transactionSaved(transaction.getId().intValue());
    }


    private int calculateProductPrice(List<Product> products) {
        int price = 0;
        for (Product product : products) {
            price += product.getPendingCount() * product.getCurrentProductPrice().getPrice();
        }
        return price;
    }

    private int calculateReaddedPrice(List<TransactionReadded> transactionReaddeds) {
        int price = 0;
        for (TransactionReadded transactionReadded : transactionReaddeds) {
            price += transactionReadded.getPrice()
                    * (transactionReadded.getType() == TransactionReadded.INC ? 1 : -1);
        }
        return price;
    }
}
