package com.tiranaporcelain.admin.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mphj on 10/15/2017.
 */

public class LoginModel {

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private long createdAt;

    @SerializedName("token")
    private String token;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private int status = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSucceed(){
        return this.status == 200;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
