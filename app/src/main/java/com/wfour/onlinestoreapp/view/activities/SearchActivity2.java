package com.wfour.onlinestoreapp.view.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.ColorProduct;
import com.wfour.onlinestoreapp.objects.HomeObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.RecomendedObj;
import com.wfour.onlinestoreapp.objects.SizeProduct;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.adapters.SearchProductAdapter;
import com.wfour.onlinestoreapp.widgets.onscroll.EndlessRecyclerOnScrollListener;
//import com.wfour.onlinestoreapp.widgets.recyclerview.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity2 extends BaseActivity implements EndlessRecyclerOnScrollListener.OnLoadMoreListener {

    LinearLayout last_seen_linear;
    private Toolbar toolbar;
    private SearchProductAdapter mAdapter;
    private RecyclerView mRclDeal;
    private ArrayList<ProductObj> mDatas;
    private String textSearch;
    private LinearLayout llNoData;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private LinearLayoutManager manager;
    private int page;
    private int numberpage;
    private String text;
    static ArrayList<ProductObj> last_seen_List = new ArrayList<>();
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "last_seen_list";

    ImageView last_seen_p1, last_seen_p2, last_seen_p3, last_seen_p4;

    TextView most_popular_tv1,most_popular_tv2,most_popular_tv3,most_popular_tv4,most_popular_tv5,most_popular_tv6,most_popular_tv7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        AppUtil.hideSoftKeyboard(this);
        mRclDeal = findViewById(R.id.rcv_data);
        llNoData = findViewById(R.id.ll_no_data);
        toolbar = findViewById(R.id.toolbar);
        last_seen_linear = findViewById(R.id.last_seen_linear);
        manager = new LinearLayoutManager(self);
        mRclDeal.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.search));
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }

        last_seen_p1 = findViewById(R.id.last_seen_p1);
        last_seen_p2 = findViewById(R.id.last_seen_p2);
        last_seen_p3 = findViewById(R.id.last_seen_p3);
        last_seen_p4 = findViewById(R.id.last_seen_p4);
        last_seen_p4 = findViewById(R.id.last_seen_p4);
        last_seen_p4 = findViewById(R.id.last_seen_p4);
        most_popular_tv1 = findViewById(R.id.most_popular_tv1);
        most_popular_tv2 = findViewById(R.id.most_popular_tv2);
        most_popular_tv3 = findViewById(R.id.most_popular_tv3);
        most_popular_tv4 = findViewById(R.id.most_popular_tv4);
        most_popular_tv5 = findViewById(R.id.most_popular_tv5);
        most_popular_tv6 = findViewById(R.id.most_popular_tv6);
        most_popular_tv7 = findViewById(R.id.most_popular_tv7);

        last_seen_p1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setData(last_seen_List.get(0));
                    }
                }
        );
        last_seen_p2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setData(last_seen_List.get(1));
                    }
                }
        );
        last_seen_p3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setData(last_seen_List.get(2));
                    }
                }
        );
        last_seen_p4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setData(last_seen_List.get(3));
                    }
                }
        );
        mAdapter = new SearchProductAdapter(self, mDatas, new IMyOnClick() {
            @Override
            public void MyOnClick(int position, ProductObj productObj) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
                GlobalFunctions.startActivityWithoutAnimation(self, DealDetailActivity.class, bundle);
            }
        });
        mRclDeal.setAdapter(mAdapter);
        onScrollListener = new EndlessRecyclerOnScrollListener(this);
        mRclDeal.addOnScrollListener(onScrollListener);
        onScrollListener.setEnded(false);
//      mRclDeal.setLayoutManager(new GridLayoutManager(self, 1));
        try {
            Gson gson = new Gson();

            String json = prefs.getString("codeList", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<ProductObj>>() {
                }.getType();
                last_seen_List = gson.fromJson(json, type);
//                Toast.makeText(SearchActivity2.this,"List Size = "+last_seen_List.size(),Toast.LENGTH_SHORT).show();
                Log.e("lastseensize", last_seen_List.size() + "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        setMostPopularItems();
        setLastSeen();
    }

    private void setLastSeen() {

        if(last_seen_List.size()==0)
        {
            last_seen_p1.setVisibility(View.GONE);
            last_seen_p2.setVisibility(View.GONE);
            last_seen_p3.setVisibility(View.GONE);
            last_seen_p4.setVisibility(View.GONE);
        }
        if(last_seen_List.size()==1)
        {
            last_seen_p1.setVisibility(View.VISIBLE);
            last_seen_p2.setVisibility(View.GONE);
            last_seen_p3.setVisibility(View.GONE);
            last_seen_p4.setVisibility(View.GONE);
        }
        if(last_seen_List.size()==2)
        {
            last_seen_p1.setVisibility(View.VISIBLE);
            last_seen_p2.setVisibility(View.VISIBLE);
            last_seen_p3.setVisibility(View.GONE);
            last_seen_p4.setVisibility(View.GONE);
        }
        if(last_seen_List.size()==3)
        {
            last_seen_p1.setVisibility(View.VISIBLE);
            last_seen_p2.setVisibility(View.VISIBLE);
            last_seen_p3.setVisibility(View.VISIBLE);
            last_seen_p4.setVisibility(View.GONE);
        }
        int i = 0;
        while (i < last_seen_List.size()) {
            if (i == 0) {
                Glide.with(this).load(last_seen_List.get(0).getImage()).into(last_seen_p1);
            } else if (i == 1) {
                Glide.with(this).load(last_seen_List.get(1).getImage()).into(last_seen_p2);
            } else if (i == 2) {
                Glide.with(this).load(last_seen_List.get(2).getImage()).into(last_seen_p3);
            } else if (i == 3) {
                Glide.with(this).load(last_seen_List.get(3).getImage()).into(last_seen_p4);
            }
            i++;
        }
    }

    @Override
    void inflateLayout() {

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_view, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(Html.fromHtml("<font color = #BDBDBD>" + getResources().getString(R.string.hintSearchMess) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                mDatas.clear();
//                if (mDatas.size() != 0) {
//                    last_seen_linear.setVisibility(View.GONE);
//                }
                text = query;
                page = 1;
                // setcurrent page ve 0
                onScrollListener.setCurrentPage(page);
                getData(text, page);
                searchItem.collapseActionView();
                searchView.clearFocus();
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getData(String query, int number) {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.getProductSearch(self, query, number, 20, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    com.wfour.onlinestoreapp.base.ApiResponse response = new com.wfour.onlinestoreapp.base.ApiResponse(jsonObject);

                    last_seen_linear.setVisibility(View.GONE);
                    if (!response.isError()) {
                        mDatas.addAll(response.getDataList(ProductObj.class));
                        mAdapter.addList(mDatas);
                        onScrollListener.onLoadMoreComplete();
                        onScrollListener.setEnded(JSONParser.isEnded(response, page));
//                        Log.e("SearchActivity2", "isEnded: "+JSONParser.isEnded(response, page) );
//                        Log.e("SearchActivity2", "onSuccess: "+ mDatas.size() );
                        checkViewHideShow();
                        AppUtil.hideSoftKeyboard(self);
                    }
                }

                @Override
                public void onError() {

                }
            });

        }
    }

    private void checkViewHideShow() {
        if (mDatas.size() == 0) {
//      llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
        }
    }

    @Override
    void initUI() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    void initControl() {

    }

    @Override
    public void onLoadMore(int page) {
        this.page = page;
        getData(text, page);
//        Log.e(SearchActivity2.class.getSimpleName(), "onLoadMore: " + page );
    }
    public void setData(ProductObj productObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
//        Log.e(TAG, "setData: " + new Gson().toJson(productObj));
        GlobalFunctions.startActivityWithoutAnimation(SearchActivity2.this, DealDetailActivity.class, bundle);
    }

    private ArrayList<ProductObj> polularList = new ArrayList<>();
/*
    private void setMostPopularItems() {
        polularList.clear();
        polularList = new ArrayList<>();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("apikey", "36638d85c67dc3ceab7901c2bc9bb36b");
        requestBody.put("domain", "@biz");

        ApiUtils.getAPIService().getPopular(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    Log.e("TAG", "popular" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        ResponseBody m = response.body();
                        try {
                            JSONObject obj = new JSONObject(response.body().string());
                            if (obj.getString("status").equals("SUCCESS")) {

                                ProductObj p;
                                JSONArray array = obj.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject item_obj = (JSONObject) array.get(i);
                                    p = new ProductObj();
                                    ArrayList image_files = new ArrayList();
                                    p.setApplication_id(String.valueOf(item_obj.getString("application_id")));
                                    p.setBanner(item_obj.getString("banner"));
                                    p.setBarcode_image(item_obj.getString("barcode_image"));
                                    p.setBrand(item_obj.getString("brand"));
                                    p.setCategory_id(item_obj.getString("category_id"));
                                    p.setCode(item_obj.getString("code"));
                                    p.setContent(item_obj.getString("content"));
                                    p.setCost(item_obj.getString("cost"));
                                    p.setCount_comments(item_obj.getString("count_comments"));
                                    p.setCount_likes(item_obj.getString("count_likes"));
                                    p.setCount_purchase(item_obj.getString("count_purchase"));
                                    p.setCount_rates(item_obj.getString("count_rates"));
                                    p.setCount_views(item_obj.getString("count_views"));
                                    p.setCreated_date(item_obj.getString("created_date"));
                                    p.setCreated_user(item_obj.getString("created_user"));
                                    p.setCurrency(item_obj.getString("currency"));
                                    p.setId(item_obj.getString("id"));
                                    p.setDiscount(item_obj.getString("discount"));
                                    p.setImage(item_obj.getString("image"));
                                    p.setIs_active(Integer.parseInt(item_obj.getString("is_active")));
                                    p.setIs_favourite(Integer.parseInt(item_obj.getString("is_favourite")));
                                    p.setIs_hot(Integer.parseInt(item_obj.getString("is_hot")));
                                    p.setIs_prize(Integer.parseInt(item_obj.getString("is_prize")));
                                    p.setIs_promotion(item_obj.getString("is_promotion"));
                                    p.setIs_tax_included(item_obj.getString("is_tax_included"));
                                    p.setIs_top(Integer.parseInt(item_obj.getString("is_top")));
                                    p.setModified_date(item_obj.getString("modified_date"));
                                    p.setModified_user(item_obj.getString("modified_user"));
                                    p.setQuantity(item_obj.getString("quantity"));
                                    p.setPromotion_id(item_obj.getString("is_promotion"));
                                    p.setOverview(item_obj.getString("overview"));
                                    p.setUnit(item_obj.getString("unit"));
                                    p.setType(item_obj.getString("type"));
                                    p.setTitle(item_obj.getString("title"));
                                    p.setThumbnail(item_obj.getString("thumbnail"));
                                    p.setPrice(Double.parseDouble(item_obj.getString("price")));
                                    p.setOld_price(Double.parseDouble(item_obj.getString("old_price")));
                                    image_files = new ArrayList();
                                    JSONArray image_array = item_obj.getJSONArray("image_files");
                                    for (int j = 0; j < image_array.length(); j++) {
                                        image_files.add(image_array.get(j));
                                    }
                                    ArrayList<SizeProduct> size_list = new ArrayList<>();
                                    ArrayList<ColorProduct> color_list = new ArrayList<>();
                                    p.setSizes(size_list);
                                    p.setColors(color_list);
                                    p.setImage_files(image_files);
                                    polularList.add(p);

                                }
                                setUpMostPopular();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        RecomendedObj obj;
                        int i = 0;
                        ProductObj p;
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setUpMostPopular()
    {
        int i=0;
        while(i<=6)
        {
            ProductObj p=polularList.get(i);
            if(i==0)
            {
                most_popular_tv1.setText(p.getTitle());
            }
            else if(i==1)
            {
                most_popular_tv2.setText(p.getTitle());
            }else if(i==2)
            {
                most_popular_tv3.setText(p.getTitle());
            }
            else if(i==3)
            {
                most_popular_tv4.setText(p.getTitle());
            }
            else if(i==4)
            {
                most_popular_tv5.setText(p.getTitle());
            }else if(i==5)
            {
                most_popular_tv6.setText(p.getTitle());
            }else if(i==6)
            {
                most_popular_tv7.setText(p.getTitle());
            }
            i++;
        }
    }*/
}
