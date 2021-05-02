package com.wfour.onlinestoreapp.paypal;

import org.json.JSONObject;

/**
 * Created by Suusoft on 4/14/2016.
 */
public class PaypalParser {

    private static final String PAYPAY_RESONDE = "response";
    private static final String PAYPAL_RESPONDE_TYPE = "response_type";

    public static String getTransactionFromPaypal(JSONObject json) {
        try {
            JSONObject proof_of_payment = json
                    .getJSONObject(PAYPAY_RESONDE);

            return proof_of_payment.getString("state")
                    + proof_of_payment.getString("id");
        } catch (Exception e) {

        }
        return "";
    }
}
