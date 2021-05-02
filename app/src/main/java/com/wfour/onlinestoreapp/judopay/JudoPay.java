package com.wfour.onlinestoreapp.judopay;


/**
 * Created by Suusoft on 26/12/2016.
 */

public class JudoPay {
    private static final String API_TOKEN = "wFiIoRkkUm2Vmh2o";
    private static final String API_SECRET = "fd88a8c9c06e2d9597358feac037af7f8791aab29afaf65e5801dc12fd14a1e6";
    private static final String ID = "100613-058";
    public static final int PAYMENT_REQUEST = 1010;

    public JudoPay() {
    }

//    public void payment(Fragment fragment, String amount) {
//        Intent intent = new Intent(fragment.getActivity(), PaymentActivity.class);
//        intent.putExtra(Judo.JUDO_OPTIONS, new Judo.Builder()
//                .setApiToken(API_TOKEN)
//                .setApiSecret(API_SECRET)
//                .setEnvironment(Judo.SANDBOX)
//                .setJudoId(ID)
//                .setAmount(amount)
//                .setCurrency(Currency.GBP)
//                .setConsumerRef("<YOUR_REFERENCE>")
//                .build());
//
//        fragment.startActivityForResult(intent, PAYMENT_REQUEST);
//    }
//
//    public void payment(Activity activity, String amount) {
//        Intent intent = new Intent(activity, PaymentActivity.class);
//        intent.putExtra(Judo.JUDO_OPTIONS, new Judo.Builder()
//                .setApiToken(API_TOKEN)
//                .setApiSecret(API_SECRET)
//                .setEnvironment(Judo.SANDBOX)
//                .setJudoId(ID)
//                .setAmount(amount)
//                .setCurrency(Currency.GBP)
//                .setConsumerRef("<YOUR_REFERENCE>")
//                .build());
//
//        activity.startActivityForResult(intent, PAYMENT_REQUEST);
//    }

}
