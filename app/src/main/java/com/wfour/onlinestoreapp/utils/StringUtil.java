package com.wfour.onlinestoreapp.utils;

import android.content.Context;
import android.util.Log;

import com.wfour.onlinestoreapp.globals.Constants;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Suusoft on 12/06/2016.
 */

public class StringUtil {

    public static String convertNumberToString(float number, int numberAfterDecimal) {
        return String.format(Locale.US, "%,.0" + numberAfterDecimal + "f", number);
    }

    public static String convertNumberToString(float number) {
        return String.format(Locale.US, "%.0f", number);
    }

    public static String convertNumberToString(double number, int numberAfterDecimal) {
        return String.format(Locale.US, "%,.0" + numberAfterDecimal + "f", number);
    }

    public static String convertNumberToString(Context context, double number, int numberAfterDecimal) {
        if (number < 0) {
            number = 0 - number;
            return "-" +  String.format(Locale.US, "%,.0" + numberAfterDecimal + "f", number);
        } else
            return String.format(Locale.US, "%,.0" + numberAfterDecimal + "f", number);
    }

    public static float convertStringToNumber(String strNumber) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);

        if (strNumber.contains(",")) {
            strNumber = strNumber.replace(",", "");
        }

        try {
            Number number = format.parse(strNumber);
            return number.floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double convertStringToNumberDouble(String strNumber) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        if (strNumber.isEmpty()) {
            return 0;
        }

        if (strNumber.contains(",")) {
            strNumber = strNumber.replace(",", "");
        }

        try {
            Number number = format.parse(strNumber);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Check format of email
     *
     * @param target is a email need checking
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Check field not empty and 6 word
     *
     * @param input text input
     */
    public static boolean isGoodField(String input) {
        if (input == null || input.isEmpty() || input.length() < 6)
            return false;
        return true;
    }

    /**
     * Check field not empty
     *
     * @param input text input
     */
    public static boolean isEmpty(String input) {
        if (input == null || input.isEmpty())
            return true;
        return false;
    }

    /**
     * get action of url request
     *
     * @param url
     */
    public static String getAction(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * Password is minimum 8 characters and 1 Number and not contain special character
     * exp: trang123
     */
    public static boolean isValidatePassword(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        return m.find();
    }

    /**
     * get text html in tag Bold
     *
     * @param text
     */
    public static String convertToTextHtmlBold(Context context,String text, int color) {
        return "<b><font color=" + color  + ">" + text + "</font></b>";
//        return "<b><font color=\"#c5c5c5\">" + text + "</font></b>";
    }

    /**
     * get string between pair character
     *
     * @param str
     */
    public static String getStringBetweenPairCharacters(String str) {
        String result= "";
        try {
            result = str.substring(str.indexOf(Constants.STR_TAG_OPEN) + 1, str.indexOf(Constants.STR_TAG_CLOSE));
        }catch (Exception e){
            result = "";
        }
        return result;
    }

    /**
     * get string before character
     *
     * @param str
     */
    public static String getStringBeforeCharacter(String str) {
        String result= "";
        if (str.contains(Constants.STR_TAG_OPEN) && str.contains(Constants.STR_TAG_CLOSE)) {
            try {
                result = str.substring( 0, str.indexOf(Constants.STR_TAG_OPEN)) + ": \n";
            }catch (Exception e){
                result = str;
            }
        }else result = str;
        Log.e("StringUtil","getStringBeforeCharacter " + result);
        return result;
    }
}
