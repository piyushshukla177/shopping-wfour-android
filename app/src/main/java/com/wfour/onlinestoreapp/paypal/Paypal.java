package com.wfour.onlinestoreapp.paypal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by Suusoft on 4/14/2016.
 */
public class Paypal implements IPaypal {
    private static final String TAG = "Paypal class";
    public static final int PAYPAL_REQUEST_CODE = 1005;

    protected static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PAYPAL_ENVIRONMENT)
            .clientId(PAYPAL_CLIENT_APP_ID)
            .acceptCreditCards(false);

    /**
     *
     * Using this when listener startActivityForResult() in Activity.
     * @param value is price of product.
     * @param context current activity
     *
     */
    public static void requestPaypalPayment(Activity context, double value) {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
                PAYPAL_CURENCY, PAYPAl_CONTENT_TEXT, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        context.startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    /**
     * Using this when listener startActivityForResult() in Fragment.
     * @param value is price of product.
     * @param fragment response to startActivityForResult() in Fragment
     *
     */
    public static void requestPaypalPayment(Fragment fragment, double value) {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
                PAYPAL_CURENCY, PAYPAl_CONTENT_TEXT, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(fragment.getContext(), PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        fragment.startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    /**
    *
    * Start paypal service. Called in onCreate()
    *
    */
    public static void startPaypalService(Context context) {
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        context.startService(intent);
    }

    /**
     *
     * Call this in onActivityResult when call { requestPaypalPayment()}
     * @param data from onActivityResult
     *
     */
    public static boolean isConfirm(Intent data){
        if (data != null) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i(TAG, confirm.toJSONObject().toString(4));
                    Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                    /**
                     *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                     * or consent completion.
                     * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                     * for more details.
                     *
                     * For sample mobile backend interactions, see
                     * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                     */

                } catch (JSONException e) {
                    Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                }
                return true;
            }
        }
        return false;
    }

}
