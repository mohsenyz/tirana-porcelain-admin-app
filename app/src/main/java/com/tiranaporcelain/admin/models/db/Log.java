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
public class Log {

    @Id(autoincrement = true)
    public Long id;
    public String command;
    public String body;
    public boolean done;

    @Generated(hash = 740668731)
    public Log(Long id, String command, String body, boolean done) {
        this.id = id;
        this.command = command;
        this.body = body;
        this.done = done;
    }

    @Generated(hash = 1364647056)
    public Log() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean getDone() {
        return this.done;
    }

}
