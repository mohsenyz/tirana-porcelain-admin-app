package com.tiranaporcelain.admin.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.json.JSONObject;
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
    public long createdAt;
    public int serverId;

    @Generated(hash = 1814894556)
    public Customer(Long id, String name, String phone, long createdAt,
            int serverId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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

    public static String toJson(Customer customer){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", customer.getName());
            jsonObject.put("phone", customer.getPhone());
            jsonObject.put("createdAt", customer.getCreatedAt());
            jsonObject.put("serverId", customer.getServerId());
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
