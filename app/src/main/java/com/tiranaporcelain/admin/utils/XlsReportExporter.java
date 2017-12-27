package com.tiranaporcelain.admin.utils;

import android.os.Environment;

import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.CustomerDao;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.ProductPrice;
import com.tiranaporcelain.admin.models.db.ProductPriceDao;
import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.models.db.TransactionDao;
import com.tiranaporcelain.admin.models.db.TransactionProduct;
import com.tiranaporcelain.admin.models.db.TransactionProductDao;
import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.models.db.TransactionReaddedDao;
import com.tiranaporcelain.admin.presenters.fragment.ReportListPresenterImpl;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by mphj on 11/20/17.
 */


public class XlsReportExporter {

    public static File destinationFolder = Environment.getExternalStoragePublicDirectory("ShafaghAccounting");

    protected Transaction transaction;
    protected List<TransactionProduct> transactionProducts;
    protected List<TransactionReadded> transactionReaddeds;
    protected List<ReportListPresenterImpl.ReportV1Model> reportV1Models;

    public XlsReportExporter(Transaction transaction,
                             List<TransactionProduct> transactionProductList,
                             List<TransactionReadded> transactionReaddedList) {
        this.transaction = transaction;
        this.transactionProducts = transactionProductList;
        this.transactionReaddeds = transactionReaddedList;
    }

    public XlsReportExporter(List<ReportListPresenterImpl.ReportV1Model> list) {
        this.reportV1Models = list;
    }

    public static XlsReportExporter byId(int id) {
        TransactionDao transactionDao = DaoManager.session().getTransactionDao();
        TransactionProductDao transactionProductDao = DaoManager.session().getTransactionProductDao();
        TransactionReaddedDao transactionReaddedDao = DaoManager.session().getTransactionReaddedDao();

        Transaction transaction = transactionDao.load((long) id);
        List<TransactionProduct> transactionProducts = transactionProductDao.queryBuilder()
                .where(TransactionProductDao.Properties.TransactionId.eq(id))
                .list();
        List<TransactionReadded> transactionReaddeds = transactionReaddedDao.queryBuilder()
                .where(TransactionReaddedDao.Properties.TransactionId.eq(id))
                .list();
        return new XlsReportExporter(transaction, transactionProducts, transactionReaddeds);
    }


    public static XlsReportExporter byReportV1(List<ReportListPresenterImpl.ReportV1Model> list) {
        return new XlsReportExporter(list);
    }


    public void export(String fileName) throws Exception{
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File outputFile = new File(destinationFolder + File.separator + fileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        Workbook wb = new HSSFWorkbook();
        CellStyle cHeader = wb.createCellStyle();
        cHeader.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Sheet productSheet = wb.createSheet("محصولات");

        CustomerDao customerDao = DaoManager.session().getCustomerDao();
        Customer customer = customerDao.load((long)transaction.getCustomerId());
        String customerTitle = customer.getName() + " (" + customer.getPhone() + ")";


        float[] t1ColumnsSize = new float[]{1, 6, 3, 2, 3};
        String[] t1Headers = new String[] {"شماره", "نام", "قیمت واحد", "تعداد", "قیمت کل"};
        String[][] t1Body = new String[transactionProducts.size()][5];
        for (int i = 0; i < transactionProducts.size(); i++) {
            TransactionProduct transactionProduct = transactionProducts.get(i);
            ProductDao productDao = DaoManager.session().getProductDao();
            ProductPriceDao productPriceDao = DaoManager.session().getProductPriceDao();
            ProductPrice productPrice = productPriceDao.queryBuilder()
                    .where(ProductPriceDao.Properties.ProductId.eq(transactionProduct.getProductId()))
                    .orderDesc(ProductPriceDao.Properties.CreatedAt)
                    .unique();
            Product product = productDao.load((long)transactionProduct.getProductId());
            String[] row = {LocaleUtils.e2f(String.valueOf(i + 1)),
                    product.getName(),
                    LocaleUtils.e2f(String.valueOf((int) productPrice.getCustomerPrice())),
                    LocaleUtils.e2f(String.valueOf(transactionProduct.getCount())),
                    LocaleUtils.e2f(String.valueOf((int)(transactionProduct.getCount() * productPrice.getCustomerPrice())))};
            t1Body[i] = row;
        }
        XlsUtils.getDefaultTable(productSheet, t1ColumnsSize, t1Headers, t1Body, cHeader);


        Sheet readdedSheet = wb.createSheet("اضافات و کسورات");
        float[] t2ColumnsSize = new float[]{1, 6, 3, 2};
        String[] t2Headers = new String[] {"شماره", "توضیحات", "قیمت", "نوع"};
        String[][] t2Body = new String[transactionReaddeds.size()][4];
        for (int i = 0; i < transactionReaddeds.size(); i++) {
            TransactionReadded transactionReadded = transactionReaddeds.get(i);
            String type = (transactionReadded.getType() == TransactionReadded.INC)
                    ? "اضافات" : "کسورات";
            String[] row = {LocaleUtils.e2f(String.valueOf(i + 1)),
                    transactionReadded.getDescription(),
                    LocaleUtils.e2f(String.valueOf((int)transactionReadded.getPrice())),
                    LocaleUtils.e2f(type)};
            t2Body[i] = row;
        }
        XlsUtils.getDefaultTable(readdedSheet, t2ColumnsSize, t2Headers, t2Body, cHeader);

        Sheet finallySheet = wb.createSheet("جمع بندی");

        float[] t3ColumnsSize = {2, 6};
        String paymentType =
                (transaction.getPaymentType() == Transaction.PAYMENT_CHECK)
                        ? "چک" : "نقد";
        String[][] t3Body = {
                {"تخفیف", LocaleUtils.e2f(String.valueOf((int)transaction.getOff())) + " درصد"},
                {"مالیات", LocaleUtils.e2f(String.valueOf((int)transaction.getTax())) + " درصد"},
                {"توضیحات", transaction.getDescription()},
                {"مشتری", customerTitle},
                {"نوع پرداخت", paymentType},
                {"مبلغ قابل پرداخت", LocaleUtils.e2f(String.valueOf(transaction.getCustomerPrice()))}
        };

        XlsUtils.getDefaultTable(finallySheet, t3ColumnsSize, new String[]{"", ""}, t3Body, cHeader);
        FileOutputStream os = new FileOutputStream(outputFile);
        wb.write(os);
        os.close();
    }


    public void exportReportV1(String fileName) throws Exception{
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File outputFile = new File(destinationFolder + File.separator + fileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        Workbook wb = new HSSFWorkbook();
        CellStyle cHeader = wb.createCellStyle();
        cHeader.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Sheet productSheet = wb.createSheet("محصولات");
        float[] t1ColumnsSize = new float[]{1, 6, 6, 3, 4, 4};
        String[] t1Headers = new String[] {"شماره", "نام", "شناسه", "تعداد فروش", "قیمت کل فروش", "قیمت کل خرید"};
        String[][] t1Body = new String[reportV1Models.size()][6];
        for (int i = 0; i < reportV1Models.size(); i++) {
            t1Body[i] = new String[] {
                    LocaleUtils.e2f(String.valueOf(i + 1)),
                    reportV1Models.get(i).product.getName(),
                    reportV1Models.get(i).product.getToken(),
                    LocaleUtils.e2f(String.valueOf((int)reportV1Models.get(i).count)),
                    LocaleUtils.e2f(String.valueOf((int)reportV1Models.get(i).customerPrice)),
                    LocaleUtils.e2f(String.valueOf((int)reportV1Models.get(i).price))
            };
        }

        XlsUtils.getDefaultTable(productSheet, t1ColumnsSize, t1Headers, t1Body, cHeader);
        FileOutputStream os = new FileOutputStream(outputFile);
        wb.write(os);
        os.close();
    }
}
