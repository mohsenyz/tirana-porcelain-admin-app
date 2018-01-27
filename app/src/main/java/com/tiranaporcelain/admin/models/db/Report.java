package com.tiranaporcelain.admin.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.parceler.Parcel;

/**
 * Created by mphj on 1/7/18.
 */

@Parcel
@Entity
public class Report {

    public static final transient int TYPE_WORKING_REPORT = 1,
            TYPE_TRANSACTION = 2;

    public static final transient int TRANSACTION_HELP = 1,
            TRANSACTION_INSURANCE = 2,
            TRANSACTION_DEBT = 3,
            TRANSACTION_OTHERS = 4,
            TRANSACTION_SALARY = 5;

    @Id(autoincrement = true)
    public Long id;
    public long date;
    public int fromTime;
    public int toTime;
    public int workingTime;
    public int extraTime;
    public int workingTimePrice;
    public int extraTimePrice;
    public int totalProductPrice;
    public int transactionPrice;
    public int type;
    public int transactionType;
    public String description;
    public int personId;

    @Generated(hash = 391471817)
    public Report(Long id, long date, int fromTime, int toTime, int workingTime,
            int extraTime, int workingTimePrice, int extraTimePrice,
            int totalProductPrice, int transactionPrice, int type,
            int transactionType, String description, int personId) {
        this.id = id;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.workingTime = workingTime;
        this.extraTime = extraTime;
        this.workingTimePrice = workingTimePrice;
        this.extraTimePrice = extraTimePrice;
        this.totalProductPrice = totalProductPrice;
        this.transactionPrice = transactionPrice;
        this.type = type;
        this.transactionType = transactionType;
        this.description = description;
        this.personId = personId;
    }
    @Generated(hash = 1739299007)
    public Report() {
    }

    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public int getFromTime() {
        return this.fromTime;
    }
    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }
    public int getToTime() {
        return this.toTime;
    }
    public void setToTime(int toTime) {
        this.toTime = toTime;
    }
    public int getWorkingTime() {
        return this.workingTime;
    }
    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }
    public int getExtraTime() {
        return this.extraTime;
    }
    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }
    public int getWorkingTimePrice() {
        return this.workingTimePrice;
    }
    public void setWorkingTimePrice(int workingTimePrice) {
        this.workingTimePrice = workingTimePrice;
    }
    public int getExtraTimePrice() {
        return this.extraTimePrice;
    }
    public void setExtraTimePrice(int extraTimePrice) {
        this.extraTimePrice = extraTimePrice;
    }
    public int getTotalProductPrice() {
        return this.totalProductPrice;
    }
    public void setTotalProductPrice(int totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }
    public int getTransactionPrice() {
        return this.transactionPrice;
    }
    public void setTransactionPrice(int transactionPrice) {
        this.transactionPrice = transactionPrice;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getTransactionType() {
        return this.transactionType;
    }
    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
