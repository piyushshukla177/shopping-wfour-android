package com.wfour.onlinestoreapp.quickblox;

import android.content.Context;

import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.quickblox.content.SharedPrefsHelper;

public class SharedPreferencesUtil {
    private static final String QB_USER_ID = "qb_user_id";
    private static final String QB_USER_LOGIN = "qb_user_login";
    private static final String QB_USER_PASSWORD = "qb_user_password";
    private static final String QB_USER_FULL_NAME = "qb_user_full_name";

    public static void saveQbUser(Context context, QBUser qbUser) {
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();
        helper.save(QB_USER_ID, qbUser.getId());
        helper.save(QB_USER_LOGIN, qbUser.getLogin());
        helper.save(QB_USER_PASSWORD, context.getResources().getString(R.string.QB_DEFAULT_PASSWORD));
        helper.save(QB_USER_FULL_NAME, qbUser.getFullName());
    }

    public static void removeQbUser() {
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();
        helper.delete(QB_USER_ID);
        helper.delete(QB_USER_LOGIN);
        helper.delete(QB_USER_PASSWORD);
        helper.delete(QB_USER_FULL_NAME);
    }

    public static boolean hasQbUser() {
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();
        return helper.has(QB_USER_LOGIN) && helper.has(QB_USER_PASSWORD);
}

    public static QBUser getQbUser() {
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();

        if (hasQbUser()) {
            Integer id = helper.get(QB_USER_ID);
            String login = helper.get(QB_USER_LOGIN);
            String password = helper.get(QB_USER_PASSWORD);
            String fullName = helper.get(QB_USER_FULL_NAME);

            QBUser user = new QBUser(login, password);
            user.setId(id);
//            user.setId(80813);
            user.setFullName(fullName);
            return user;
        } else {
            return null;
        }
    }

}
