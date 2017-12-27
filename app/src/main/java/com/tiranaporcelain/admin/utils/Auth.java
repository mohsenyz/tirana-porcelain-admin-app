package com.tiranaporcelain.admin.utils;

import com.tiranaporcelain.admin.models.LoginModel;
//import com.snappydb.DB;
//import com.snappydb.DBFactory;

/**
 * Created by mphj on 10/15/2017.
 */

public class Auth {
    public static void login(LoginModel loginModel){
        /*try{
            DB snappydb = DBFactory.open(AccountryApplication.context());
            snappydb.put("user", loginModel);
            snappydb.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    public static void logout(){
        /*try{
            DB snappydb = DBFactory.open(AccountryApplication.context());
            snappydb.del("user");
            snappydb.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    public static boolean isLoggedIn(){
        /*try{
            DB snappydb = DBFactory.open(AccountryApplication.context());
            boolean exists = snappydb.exists("user");
            snappydb.close();
            return exists;
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return false;
    }


    public static LoginModel getInfo(){
        /*if (isLoggedIn()){
            try{
                DB snappydb = DBFactory.open(AccountryApplication.context());
                LoginModel loginModel = snappydb.getObject("user", LoginModel.class);
                snappydb.close();
                return loginModel;
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        return null;
    }
}
