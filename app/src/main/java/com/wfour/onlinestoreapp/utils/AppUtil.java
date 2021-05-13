package com.wfour.onlinestoreapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.TransportDealObj;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Suusoft on 1/8/2016.
 */
public class AppUtil {

    private static final String TAG = "AppUtil";

    ///////////////////////////////////////////////////////////////////////////
    // Start activity
    ///////////////////////////////////////////////////////////////////////////
    public static void startActivity(Context act, Class<?> clz) {
        Intent intent = new Intent(act, clz);
        act.startActivity(intent);
    }

    public static void startActivity(Context act, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(act, clz);
        intent.putExtras(bundle);
        act.startActivity(intent);
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

    public static boolean checkApi(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

    // Request runtime permissions
    public static boolean checkPermission(Context ctx, String permission) {
        if (checkApi(Build.VERSION_CODES.M)) {
            return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    /**
     * get IMEI of device
     *
     * @param context
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static void getFacebookKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Show hide keyboard
     */
    public static void showKeyboard(Context ctx, EditText editText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * This method allow closing keyboard when users click out-side(out of edittext)
     *
     * @param act  context
     * @param view root view
     */
    public static void closeKeyboardWhenTouchOutside(final Activity act, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                closeKeyboardWhenTouchOutside(act, innerView);
            }
        }
    }

    /**
     * Show toast notify.
     *
     * @param message can hard text or from string file
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
    }

    /**
     * Get color
     */
    public static int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static int getScreenWidth(Activity act) {
        /* getWidth() is deprecated */
        // Display display = act.getWindowManager().getDefaultDisplay();
        // return display.getWidth();

        DisplayMetrics dm = new DisplayMetrics();
        if(act!=null)
        {
            act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }

        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

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
     * Add shortcut to home
     *
     * @param context Context
     * @param tClass  class is called(should be first class of app)
     */
    public static void addShortcut(Context context, Class tClass) {
        //on Home screen
        Intent shortcutIntent = new Intent(context, tClass);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    public static class RunnerLoadBitmap extends AsyncTask<Uri, Void, Bitmap> {
        private ImageView imageView;
        private Context context;

        public RunnerLoadBitmap setImageView(ImageView imageView) {
            this.imageView = imageView;
            this.context = imageView.getContext();
            return this;
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            Bitmap bitmap = null;
            try {
                Uri uri = uris[0];
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
//                bitmap = BitmapFactory.decodeStream(inputStream);
                try {
                    bitmap = ImageUtil.getCorrectOrientationImage(context, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            if (bitmap != null) {
                return ImageUtil.decodeBitmapFromBitmap(bitmap, 300, 300);
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static void setImageFromUri(ImageView imageBitmap, Uri uri) {
        new RunnerLoadBitmap().setImageView(imageBitmap).execute(uri);
    }

    public static final int PICK_IMAGE_REQUEST_CODE = 72;

    public static void pickImage(Fragment context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);
    }

    public static void pickImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static Bitmap getBitMapFromImageView(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        return bitmap;
    }

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * very simple get data from drawable. you can get drawable from imageview.
     *
     * @param drawable data
     * @return byte array
     */

    @NonNull
    public  static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void showFileChooser(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            fragment.startActivityForResult(
                    Intent.createChooser(intent, fragment.getString(R.string.select_file_to_upload)),
                    requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(fragment.getContext(), fragment.getString(R.string.pls_install_file_manager), Toast.LENGTH_SHORT).show();
        }
    }

    public static void captureImage(Activity activity, int requestCode) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, requestCode);
    }

    public static void captureImage(Fragment fragment, int requestCode) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(cameraIntent, requestCode);
    }

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
                Toast.makeText(ctx, ctx.getString(R.string.msg_no_email_client), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.msg_no_email), Toast.LENGTH_SHORT).show();
        }
    }

    public static int getCurentPositionCountryCode(Context context) {
        String CountryID = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentCountryCode(Context context) {
        String countryID = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            countryID = manager.getSimCountryIso().toUpperCase();

        } catch (Exception e) {
            countryID = "+";
        }
        return countryID;
    }

    public static String formatCurrency(String str) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(new BigDecimal(str));
    }

    public static String formatCurrency(double input) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(input);
    }

    public static String formatCurrency(float input) {
        DecimalFormat format = new DecimalFormat("###,###,###.0");
        return format.format(input);
    }

    public static String formatCurrency(int input) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(input);
    }

    public static void linkToWeb(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void share(Activity activity, String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.share)));
    }

    public static void shareTrip(Activity activity, TransportDealObj transportDealObj) {
        String content = activity.getString(R.string.from) + ": " + transportDealObj.getPickup()
                + "\n" + activity.getString(R.string.to) + ": " + transportDealObj.getDestination()
                + "\n" + activity.getString(R.string.driver) + ": " + transportDealObj.getDriverName()
                + "\n" + activity.getString(R.string.phone_number) + ": " + transportDealObj.getDriverPhone()
                + "\n" + activity.getString(R.string.time) + ": " + DateTimeUtil.convertTimeStampToDate(transportDealObj.getTime(), "HH:mm EEE, dd/MM/yyyy");
        AppUtil.share(activity, content);
    }

    public static void fillAddress(Context context, final EditText tv, final Place place) {
        String address = place.getAddress().toString();
        if (address.contains("\n")) {
            address = address.replace("\n", ", ");
        }
        tv.setText(address);
    }

    public static void setSoftInputMode(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public static void logSizeMultiScreen(Activity activity){
        int w = AppUtil.getScreenWidth(activity);
        int h = AppUtil.getScreenHeight(activity);

        Log.e("screen", "width/height = " + AppUtil.convertPixelsToDp(activity, w)
                + "/" + AppUtil.convertPixelsToDp(activity, h)
                + " dp :::: w/h = " + w + "/" + h + "px --- dpi = "  + (w*160)/AppUtil.convertPixelsToDp(activity, w));
    }

    public static int getWidthDp(Activity activity){
        return convertPixelsToDp(activity, getScreenWidth(activity));
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }




}
