package com.wfour.onlinestoreapp.retrofit;

import com.wfour.onlinestoreapp.objects.PopularProductsObj;
import com.wfour.onlinestoreapp.retrofit.respone.BaseRespone;
import com.wfour.onlinestoreapp.retrofit.respone.RecommendedProductResponse;
import com.wfour.onlinestoreapp.retrofit.respone.ResponeUser;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

    @POST("user/update-profile")
    @Multipart
    Call<ResponeUser> updateProfile(@Part("id") RequestBody id,
                                    @Part("name") RequestBody name,
                                    @Part("address") RequestBody address,
                                    @Part MultipartBody.Part file,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email

    );

    @POST("user/update-profile")
    @Multipart
    Call<ResponeUser> updateProfile(@Part("id") RequestBody id,
                                    @Part("name") RequestBody name,
                                    @Part("address") RequestBody address,
                                    @Part("avatar") RequestBody avatar,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email
    );

    @GET("user/update-profile")
    Call<ResponeUser> updateProfile(@Query("id") String id,
                                    @Query("name") String name,
                                    @Query("address") String address,
                                    @Query("avatar") String avatar,
                                    @Query("phone") String phone,
                                    @Query("email") String email
    );

    @POST("device/index?&type=1&status=1")
    Call<BaseRespone> sendGcmID(
            @Query("gcm_id") String gcm
    );

    @Headers({"Content-Type:application/json"})
    @GET("ecommerce/product-list")
    Call<RecommendedProductResponse> getRecommendedProducts(@Query("is_recomended") String is_recomended);


    @POST("ecommerce/product-list")
    Call<ResponseBody> getPopular(@Body Map<String, String> body);

    @Headers({"Content-Type:application/json"})
    @GET("ecommerce/product-list")
    Call<RecommendedProductResponse> getPopularProductsList(@Query("is_popular") String is_recomended);
}
