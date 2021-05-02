package com.wfour.onlinestoreapp.network.multipart;

/**
 * Created by Suusoft on 1/20/2017.
 */

public class MultiPartParam {
    public String contentType;
    public String value;

    /**
     * Initialize a multipart request param with the value and content type
     *
     * @param contentType The content type of the param
     * @param value       The value of the param
     */
    public MultiPartParam(String contentType, String value) {
        this.contentType = contentType;
        this.value = value;
    }
}
