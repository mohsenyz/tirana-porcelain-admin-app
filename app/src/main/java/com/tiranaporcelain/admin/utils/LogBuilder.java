package com.tiranaporcelain.admin.utils;

import com.tiranaporcelain.admin.models.db.Log;

/**
 * Created by mphj on 11/7/2017.
 */

public class LogBuilder{
    String className;
    int id;
    String body;
    String methodName;
    public static LogBuilder update(Class cl){
        LogBuilder builder = new LogBuilder();
        builder.className = cl.getSimpleName();
        builder.methodName = "update";
        return builder;
    }

    public static LogBuilder create(Class cl){
        LogBuilder builder = new LogBuilder();
        builder.className = cl.getSimpleName();
        builder.methodName = "create";
        return builder;
    }

    public LogBuilder id(int id){
        this.id = id;
        return this;
    }

    public LogBuilder object(String json){
        body = json;
        return this;
    }

    public Log build(){
        Log log = new Log();
        log.setDone(false);
        log.setBody(body);
        log.setCommand(
                methodName +
                        "$" +
                        className.toLowerCase() +
                        "$" +
                        id
        );
        return log;
    }
}