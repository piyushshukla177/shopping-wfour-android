package com.wfour.onlinestoreapp.globals;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.MySharedPreferences;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.CartManager;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.ContactObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 12/01/2015.
 */
public class GlobalFunctions {

    private static final String GCM_TOKEN = "gcmToken";
    private static final String USER_OBJ = "userObj";

    public static Drawable buildCounterDrawable(Context context, int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.badge_icon_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static void getCountCart(ArrayList<CartObj> cartList, int count) {
        cartList = CartManager.getInstance().getArray();
        if (cartList.size() == 0) {
            DataStoreManager.clearCountCart();
            count = 0;
            DataStoreManager.saveCountCart(count);
        } else {
            int temp = 0;
            for (int i = 0; i < cartList.size(); i++) {
                temp += cartList.get(i).getNumber();
            }
            count = temp;
            DataStoreManager.saveCountCart(count);
        }
    }

    public static void showConfirmationDialog(Context context, String msg, String positive, String negative,
                                              boolean isCancelable, final IConfirmation iConfirmation) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_confirmation);

        TextViewRegular lblMsg = dialog.findViewById(R.id.lbl_msg);
        TextViewBold lblNegative = dialog.findViewById(R.id.lbl_negative);
        TextViewBold lblPositive = dialog.findViewById(R.id.lbl_positive);

        lblMsg.setText(msg);
        lblNegative.setText(negative);
        lblPositive.setText(positive);

        lblNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                iConfirmation.onNegative();
            }
        });

        lblPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                iConfirmation.onPositive();
            }
        });

        dialog.setCancelable(isCancelable);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void enableStrictMode() {
        // Allow strict mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void startActivityLTR(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityLTR(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public static void startActivityRTL(Activity act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void startActivityRTL(Activity act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void startActivityWithoutAnimation(Context act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
    }

    public static void startActivityCart(Context act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
    }

    public static void startActivityWithoutAnimation(Context act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

    public static void startActivityWithoutAnimation1(Context act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

    public static void startActivityForResult(Activity act, Class<?> clz, int reqCode, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        act.startActivityForResult(intent, reqCode);
    }

    public static void startActivityForResult(Activity act, Class<?> clz, int reqCode) {
        Intent intent = new Intent(act, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivityForResult(intent, reqCode);
    }

    public static void startActivityForResult(Fragment act, Class<?> clz, int reqCode, Bundle bundle) {
        Intent intent = new Intent(act.getContext(), clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivityForResult(intent, reqCode);
    }

    /**
     * Close activity with right-to-left animation
     *
     * @param act
     */
    public static void finishActivity(Activity act) {
        act.finish();
        act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Force close keyboard of the given editText
     *
     * @param activity
     */
    public static void closeKeyboard(AppCompatActivity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void showKeyboard(Context ctx, EditText editText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * @param email
     * @return True if the given email is valid
     */
    public static boolean validateEmail(String email) {
        return !email.trim().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * @param act
     * @return Width of screen by pixel
     */
    public static int getScreenWidthAsPixel(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * @param act
     * @return Width of screen by inch
     */
    public static double getScreenWidthAsInch(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return Math.sqrt(x + y);
    }

    /**
     * Send an email to the (one)given email, you can customize to send to multiple
     *
     * @param ctx
     * @param email
     */
    public static void sendEmail(Context ctx, String email) {
        if (!email.isEmpty() && !email.equals("null")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                ctx.startActivity(Intent.createChooser(i, ctx.getString(R.string.send_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ctx, ctx.getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.no_email), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If you want to call this method on api 23 or higher then you need to check permission in runtime
     *
     * @param act
     * @param phoneNumber
     */
    public static void call(Activity act, String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.equals("") && !phoneNumber.equalsIgnoreCase("null")) {
            String number = "tel:" + phoneNumber;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            act.startActivity(callIntent);
        } else {
            Toast.makeText(act, act.getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generating random string
     *
     * @param length Length of code
     * @return Random string
     */
    public static String generateCode(int length) {
        String str = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
        String code = "";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * str.length());
            code += str.charAt(index);
        }

        return code;
    }

    public static class updateBalance { /* Additional fields if needed */

    }


    public static void saveFCMToken(Context context, String token) {
        MySharedPreferences.getInstance(context).putStringValue(GCM_TOKEN, token);
    }

    public static String getFCMToken(Context context) {
        return MySharedPreferences.getInstance(context).getStringValue(GCM_TOKEN);
    }

    /**
     * Check permissions in runtime
     *
     * @param activity
     * @param permissions
     * @param reqCode
     * @param notification
     * @return
     */
    public static boolean isGranted(Activity activity, String[] permissions, int reqCode, String notification) {
        boolean granted = true;

        if (isMarshmallow()) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];

                granted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    if (notification != null && notification.length() > 0) {
                        Toast.makeText(activity, notification, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

            // Ask permissions
            if (!granted) {
                ActivityCompat.requestPermissions(activity, permissions, reqCode);
            }
        }

        return granted;
    }

    /**
     * Check location is granted or not in run time
     *
     * @param activity
     * @param reqCode
     * @param notification [optional] can be null/empty if you don't want to notify user
     * @return true if location is granted
     */
    public static boolean locationIsGranted(Activity activity, int reqCode, String notification) {
        boolean granted = true;

        if (isMarshmallow()) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];

                granted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (granted) {
                    break;
                }
            }

            // Ask permissions
            if (!granted) {
                if (notification != null && notification.length() > 0) {
                    Toast.makeText(activity, notification, Toast.LENGTH_SHORT).show();
                }

                ActivityCompat.requestPermissions(activity, permissions, reqCode);
            }
        }

        return granted;
    }

    /**
     * Check CALL_PHONE is granted or not in run time
     *
     * @param activity
     * @param reqCode
     * @param notification [optional] can be null/empty if you don't want to notify user
     * @return true if CALL_PHONE is granted
     */
    public static boolean callPhoneIsGranted(Activity activity, int reqCode, String notification) {
        boolean granted = true;

        if (isMarshmallow()) {
            String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
            String permission = permissions[0];

            granted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                if (notification != null && notification.length() > 0) {
                    Toast.makeText(activity, notification, Toast.LENGTH_SHORT).show();
                }
            }

            // Ask permissions
            if (!granted) {
                ActivityCompat.requestPermissions(activity, permissions, reqCode);
            }
        }

        return granted;
    }

    public static boolean isMarshmallow() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M;
    }

    public static void saveUserInfo(Context context, UserObj userObj) {
        String info = new Gson().toJson(userObj);

        MySharedPreferences.getInstance(context).putStringValue(USER_OBJ, info);
    }

    /*public static UserObj getUserInfo(Context context) {
        String strUser = MySharedPreferences.getInstance(context).getStringValue(USER_OBJ);
        return new Gson().fromJson(strUser, UserObj.class);
    }*/

    public static boolean isForeground(RecentChatObj obj) {
        return (DataStoreManager.getCurrentChat() != null
                && DataStoreManager.getCurrentChat().getQbUser().getId().equals(obj.getQbUser().getId()));
    }

    public static boolean isTheFirstMessage(TransportDealObj transportDealObj) {
        TransportDealObj obj = DataStoreManager.getTransport(transportDealObj.getDriverId());
        if (obj != null) {
            return !obj.getDriverId().equals(transportDealObj.getDriverId())
                    || obj.getLatLngPickup().latitude != transportDealObj.getLatLngPickup().latitude
                    || obj.getLatLngPickup().longitude != transportDealObj.getLatLngPickup().longitude
                    || obj.getLatLngDestination().latitude != transportDealObj.getLatLngDestination().latitude
                    || obj.getLatLngDestination().longitude != transportDealObj.getLatLngDestination().longitude;
        }

        return true;
    }

    public static boolean isFriend(String userId) {
        boolean isFriend = false;
        ArrayList<ContactObj> contacts = DataStoreManager.getContactsList();
        if (contacts != null && contacts.size() > 0) {
            for (int i = 0; i < contacts.size(); i++) {
                ContactObj contactObj = contacts.get(i);
                if (userId.equals(contactObj.getFriendId())) {
                    isFriend = true;
                    break;
                }
            }
        }

        return isFriend;
    }
}
