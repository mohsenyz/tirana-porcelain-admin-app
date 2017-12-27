package com.tiranaporcelain.admin.utils;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by mphj on 11/20/17.
 */

public class PdfReportExporter {

    public static File destinationFolder = Environment.getExternalStoragePublicDirectory("ShafaghAccounting");

    protected Transaction transaction;
    protected List<TransactionProduct> transactionProducts;
    protected List<TransactionReadded> transactionReaddeds;

    public PdfReportExporter(Transaction transaction,
                          List<TransactionProduct> transactionProductList,
                          List<TransactionReadded> transactionReaddedList) {
        this.transaction = transaction;
        this.transactionProducts = transactionProductList;
        this.transactionReaddeds = transactionReaddedList;
    }

    public static PdfReportExporter byId(int id) {
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
        return new PdfReportExporter(transaction, transactionProducts, transactionReaddeds);
    }


    public void export(String fileName) throws Exception{
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File outputFile = new File(destinationFolder + File.separator + fileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        PdfUtils.DefaultFooter event = new PdfUtils.DefaultFooter("حسابداری شفق", "صفحه ی");
        pdfWriter.setPageEvent(event);
        document.open();

        String pageTitle = "فاکتور شماره ی ";
        pageTitle += LocaleUtils.englishNumberToArabic("" + transaction.getId());

        Paragraph pBody = new Paragraph();

        Paragraph pTitle = new Paragraph(PdfUtils.asRtl(pageTitle), PdfUtils.getDefaultFont(15));
        pTitle.setAlignment(Element.ALIGN_CENTER);

        pBody.add(pTitle);
        PdfUtils.addEmptyLine(pBody, 1);

        CustomerDao customerDao = DaoManager.session().getCustomerDao();
        Customer customer = customerDao.load((long)transaction.getCustomerId());
        String customerTitle = "مشتری : " + customer.getName() + " (" + customer.getPhone() + ")";
        Paragraph pCustomer = new Paragraph(new Phrase(PdfUtils.asRtl(customerTitle),
                PdfUtils.getDefaultFont()));
        pCustomer.setAlignment(Element.ALIGN_RIGHT);
        pCustomer.setExtraParagraphSpace(5);
        pBody.add(pCustomer);

        PdfUtils.addEmptyLine(pBody, 1);

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
        PdfPTable t1 = PdfUtils.getDefaultTable(t1ColumnsSize, t1Headers, t1Body);
        pBody.add(t1);


        PdfUtils.addEmptyLine(pBody, 1);

        if (transactionReaddeds.size() != 0) {
            Paragraph pReadded = new Paragraph(new Phrase(
                    PdfUtils.asRtl("اضافات و کسورات"),
                    PdfUtils.getDefaultFont()
            ));
            pReadded.setAlignment(Element.ALIGN_CENTER);
            pBody.add(pReadded);

            PdfUtils.addEmptyLine(pBody, 1);
        }


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
        PdfPTable t2 = PdfUtils.getDefaultTable(t2ColumnsSize, t2Headers, t2Body);
        if(transactionReaddeds.size() != 0)
            pBody.add(t2);


        Paragraph pFinally = new Paragraph(PdfUtils.asRtl("جمع بندی"), PdfUtils.getDefaultFont(15));
        pFinally.setAlignment(Element.ALIGN_CENTER);
        pBody.add(pFinally);

        PdfUtils.addEmptyLine(pBody, 1);

        float[] t3ColumnsSize = {2, 6};
        String paymentType =
                (transaction.getPaymentType() == Transaction.PAYMENT_CHECK)
                ? "چک" : "نقد";
        String[][] t3Body = {
                {"تخفیف", LocaleUtils.e2f(String.valueOf((int)transaction.getOff())) + " درصد"},
                {"مالیات", LocaleUtils.e2f(String.valueOf((int)transaction.getTax())) + " درصد"},
                {"توضیحات", transaction.getDescription()},
                {"نوع پرداخت", paymentType},
                {"مبلغ قابل پرداخت", LocaleUtils.e2f(String.valueOf(transaction.getCustomerPrice()))}
        };
        PdfPTable t3 = PdfUtils.getDefaultTable(t3ColumnsSize, new String[]{}, t3Body);
        pBody.add(t3);

        document.add(pBody);
        document.close();
    }
}
