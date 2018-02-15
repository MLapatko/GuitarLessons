package com.example.user.guitarlessons;

/**
 * Created by user on 14.02.2018.
 */

public class AuthValidation {
    public static boolean checkEmail(CharSequence charSequence){
        if (charSequence==null){
            return false;
        }
       return android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }
    public static boolean checkPasswordLength(CharSequence charSequence){
        if (charSequence==null ||charSequence.length()<7){
            return false;
        }
        return true;
    }
}
