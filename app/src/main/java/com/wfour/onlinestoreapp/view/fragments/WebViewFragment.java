package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Constants;

/**
 * Created by Suusoft on 06/03/2017.
 */

public class WebViewFragment extends com.wfour.onlinestoreapp.base.BaseFragment {

    private WebView webView;
    private String url;
    String title;

    public static WebViewFragment newInstance(Bundle args) {


        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        title = bundle.getString(Constants.KEY_TITLE);

        url = bundle.getString(Constants.KEY_URL);
    }

    @Override
    protected void initView(View view) {
        webView = (WebView) view.findViewById(R.id.web_view);
        setUpWebView(webView);
    }

    @Override
    protected void getData() {

    }

    private void setUpWebView(WebView webView) {
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }
}
