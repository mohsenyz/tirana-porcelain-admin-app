package com.tiranaporcelain.admin.presenters.fragment;

import android.content.Intent;
import android.net.Uri;

import com.tiranaporcelain.admin.AccountryApplication;
import com.tiranaporcelain.admin.interfaces.fragment.ReportListView;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.ProductPrice;
import com.tiranaporcelain.admin.models.db.ProductPriceDao;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.models.db.TransactionDao;
import com.tiranaporcelain.admin.models.db.TransactionProduct;
import com.tiranaporcelain.admin.models.db.TransactionProductDao;
import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.XlsReportExporter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mphj on 11/20/17.
 */

public class ReportListPresenterImpl implements ReportListPresenter {

    ReportListView view;

    public ReportListPresenterImpl(ReportListView view) {
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public static class ReportV1Model {
        public Product product;
        public long price;
        public long customerPrice;
        public long count;
    }

    @Override
    public void exportV1(final long fromDate, final long toDate) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                Map<Long, ReportV1Model> productMap = new HashMap<>();
                TransactionDao transactionDao = DaoManager.session().getTransactionDao();
                TransactionProductDao transactionProductDao = DaoManager.session().getTransactionProductDao();
                ProductDao productDao = DaoManager.session().getProductDao();
                ProductPriceDao productPriceDao = DaoManager.session().getProductPriceDao();

                List<Transaction> transactionList = transactionDao.queryBuilder()
                        .where(TransactionDao.Properties.Type.eq(Transaction.TYPE_OUTGOING))
                        .where(TransactionDao.Properties.CreatedAt.between(fromDate, toDate))
                        .listLazy();

                for (Transaction transaction : transactionList) {
                    List<TransactionProduct> transactionProducts
                            = transactionProductDao.queryBuilder()
                            .where(TransactionProductDao.Properties.TransactionId.eq(transaction.getId()))
                            .list();
                    for (TransactionProduct transactionProduct : transactionProducts) {
                        Product product = productDao.load((long)transactionProduct.getProductId());
                        ProductPrice productPrice = productPriceDao.queryBuilder()
                                .where(ProductPriceDao.Properties.ProductId.eq(transactionProduct.getProductId()))
                                .orderDesc(ProductPriceDao.Properties.CreatedAt)
                                .unique();
                        if (productMap.containsKey(product.getId())) {
                            ReportV1Model reportV1Model = productMap.get(product.getId());
                            reportV1Model.count += transactionProduct.getCount();
                            reportV1Model.customerPrice += transactionProduct.getCount() * productPrice.getCustomerPrice() * (100 - transaction.getOff()) / 100;
                            reportV1Model.price += transactionProduct.getCount() * productPrice.getPrice();
                        } else {
                            ReportV1Model reportV1Model = new ReportV1Model();
                            reportV1Model.product = product;
                            reportV1Model.count += transactionProduct.getCount();
                            reportV1Model.customerPrice += transactionProduct.getCount() * productPrice.getCustomerPrice() * (100 - transaction.getOff()) / 100;
                            reportV1Model.price += transactionProduct.getCount() * productPrice.getPrice();
                            productMap.put(product.getId(), reportV1Model);
                        }
                    }
                }
                XlsReportExporter xlsReportExporter = XlsReportExporter.byReportV1(
                        new ArrayList<ReportV1Model>(productMap.values())
                );
                xlsReportExporter.exportReportV1("report_v1_" + fromDate + "_" + toDate + ".xls");
                e.onNext(XlsReportExporter.destinationFolder + File.separator + "report_v1_" + fromDate + "_" + toDate + ".xls");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        try{

                            File file = new File(s);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent = Intent.createChooser(target, "Open pdf");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AccountryApplication.context().startActivity(intent);
                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    @Override
    public void loadList() {
        TransactionDao transactionDao = DaoManager.session().getTransactionDao();
        view.setAdapter(
                transactionDao.queryBuilder()
                    .where(TransactionDao.Properties.Type.eq(Transaction.TYPE_OUTGOING))
                    .listLazy()
        );
    }

    @Override
    public void loadList(long fromDate, long toDate) {
        TransactionDao transactionDao = DaoManager.session().getTransactionDao();
        view.setAdapter(
                transactionDao.queryBuilder()
                        .where(TransactionDao.Properties.Type.eq(Transaction.TYPE_OUTGOING))
                        .where(TransactionDao.Properties.CreatedAt.between(fromDate, toDate))
                        .listLazy()
        );
    }
}
