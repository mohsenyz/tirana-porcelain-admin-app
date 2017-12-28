package com.tiranaporcelain.admin.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.parceler.Parcel;

/**
 * Created by mphj on 10/20/2017.
 */

@Parcel
@Entity
public class Customer {

    @Id(autoincrement = true)
    public Long id;
    public String name;
    public String phone;
    public String fixedPhone;
    public String address;
    public String description;
    public long createdAt;
    public int serverId;

    @Generated(hash = 1393280652)
    public Customer(Long id, String name, String phone, String fixedPhone,
            String address, String description, long createdAt, int serverId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.fixedPhone = fixedPhone;
        this.address = address;
        this.description = description;
        this.createdAt = createdAt;
        this.serverId = serverId;
    }

    @Generated(hash = 60841032)
    public Customer() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
