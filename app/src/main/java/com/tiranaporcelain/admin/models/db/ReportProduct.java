package com.tiranaporcelain.admin.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.parceler.Parcel;

/**
 * Created by mphj on 1/7/18.
 */

@Entity
@Parcel
public class ReportProduct {

    @Id(autoincrement = true)
    public Long id;
    public int productId;
    public int price;
    public int count;
    public int reportId;

    public ReportProduct(int productId, int price, int count, int reportId) {
        this.productId = productId;
        this.price = price;
        this.count = count;
        this.reportId = reportId;
    }

    public ReportProduct() {
    }

    @Generated(hash = 1047774072)
    public ReportProduct(Long id, int productId, int price, int count, int reportId) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.count = count;
        this.reportId = reportId;
    }
    public int getProductId() {
        return this.productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getReportId() {
        return this.reportId;
    }
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isValid() {
        return id != 0 && productId != 0 && price != 0 && count != 0 && reportId != 0;
    }
}
