package com.tiranaporcelain.admin.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Transient;


/**
 * Created by mphj on 10/20/2017.
 */
@Parcel
@Entity
public class Product {

    @Id(autoincrement = true)
    public Long id;
    public String name;
    public long createdAt;
    public String token;
    public int serverId;
    public int categoryId;
    public int count;

    @org.greenrobot.greendao.annotation.Transient
    public transient int pendingCount;

    @Transient
    @org.greenrobot.greendao.annotation.Transient
    private ProductPrice currentProductPrice;

    @Generated(hash = 991032820)
    public Product(Long id, String name, long createdAt, String token, int serverId,
            int categoryId, int count) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.token = token;
        this.serverId = serverId;
        this.categoryId = categoryId;
        this.count = count;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }

    public ProductPrice getCurrentProductPrice() {
        return currentProductPrice;
    }

    public void setCurrentProductPrice(ProductPrice currentProductPrice) {
        this.currentProductPrice = currentProductPrice;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public static String toJson(Product product) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", product.name);
            jsonObject.put("token", product.token);
            jsonObject.put("createdAt", product.name);
            jsonObject.put("serverId", product.serverId);
            jsonObject.put("categoryId", product.categoryId);
            jsonObject.put("count", product.count);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }
}
