package com.wfour.onlinestoreapp.interfaces;

import com.wfour.onlinestoreapp.objects.TransportDealObj;

/**
 * Created by Suusoft on 12/05/2016.
 */

public interface ITransportDeal {

    void onCancel(TransportDealObj obj);

    void onTracking(TransportDealObj obj);

    void onFinish(TransportDealObj obj);
}
