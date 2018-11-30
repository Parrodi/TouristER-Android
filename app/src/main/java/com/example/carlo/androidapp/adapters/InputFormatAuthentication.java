package com.example.carlo.androidapp.adapters;

import android.text.TextUtils;
import android.util.Patterns;

public class InputFormatAuthentication {

    public static boolean isEmailValid(String email) {
        return (!email.equals("")  && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean passwordsMatch(String psw1, String pswd2) {
        return psw1.equals(pswd2) && !psw1.equals("") && !pswd2.equals("") && psw1.length()>=6;
    }

    public static boolean isNameValid(String name){
        if(name.length() == 0){
            return false;
        }
        int size = 0;
        if(name.length() > 200)
            return false;
        while(name.length() > size){
            if(name.charAt(size) == 32){
                if(size == 0){return false;}
                if(name.charAt(size+1) == 32){
                    return false;
                }else size++;
            }else if(name.charAt(size) > 64 && name.charAt(size) < 123){
                size++;
            }else return false;
        }
        return true;
    }

    public static boolean isNumberValid(String phone){
        if(phone.length() > 9 && phone.length() < 15 && Patterns.PHONE.matcher(phone).matches() && !phone.equals("")){
            return true;
        }else
        return false;
    }
}
