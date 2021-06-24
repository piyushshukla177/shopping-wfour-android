package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.interfaces.MyOnClickCategory;
import com.wfour.onlinestoreapp.objects.BannerObj;
import com.wfour.onlinestoreapp.objects.Category;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.HomeObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.activities.DealsActivity;
import com.wfour.onlinestoreapp.view.activities.ProductListActivity;
import com.wfour.onlinestoreapp.view.activities.SubCategoryActivity;

import static com.wfour.onlinestoreapp.view.fragments.HomeFragment.bannerObjs;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BaseViewHolder> {

    public static final int ITEM_NEW = 2;
    public static final int ITEM_FEATURE = 4;
    public static final int ITEM_CATEGORY = 1;
    public static final int ITEM_BANNER = 0;
    public static final int ITEM_HOT = 3;
    public static final int TEST = 5;
    private static final String TAG = ListAdapter.class.getSimpleName();
    private ArrayList<HomeObj> homeObjList;
    private ArrayList<ProductObj> productObjList;
    protected ArrayList<Category> categoryList;
    private IMyOnClick onClick;
    private Activity activity;
    private Context context;
    private int h;
    private Bundle bundle;
    private Intent intent;
    private RecyclerView.RecycledViewPool viewpool;
    private static ArrayList<BannerObj> listbanner;

    public ListAdapter(Activity activity, ArrayList<HomeObj> homeObjList, IMyOnClick onClick) {
        this.activity = activity;
        this.homeObjList = homeObjList;
        this.onClick = onClick;
        initParam(activity);
    }

    public ListAdapter(Activity activity, IMyOnClick onClick) {
        this.activity = activity;

        this.onClick = onClick;
        initParam(activity);
    }

    public void addList(ArrayList<HomeObj> homeObjList) {
        this.homeObjList = homeObjList;
        this.notifyDataSetChanged();
        viewpool = new RecyclerView.RecycledViewPool();
    }

    public void addList2(ArrayList<BannerObj> bannerObjs) {
        this.listbanner = bannerObjs;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_BANNER;
        } else if (position == 1) {
            return ITEM_CATEGORY;
        } else if (position == 2) {
            return ITEM_NEW;
        } else if (position == 3) {
            return ITEM_HOT;
        } else if (position == 4) {
            return ITEM_FEATURE;
        } else {
            return TEST;
        }
    }

    public void initParam(Activity activity) {
        h = AppUtil.getScreenHeight(activity);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
            return new ViewHolderHeader(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_row, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            final HomeObj homeObj = homeObjList.get(position);
            viewHolder.tvName.setText(homeObj.name);

            if (homeObj.name.equals(AppController.getInstance().getString(R.string.PRODUTUFOUN))) {
                Log.e(TAG, "setData: " + new Gson().toJson(homeObj.productList));
                viewHolder.rcvData.setAdapter(new SingleVerticalAdapter(activity, homeObj.productList, new IMyOnClick() {
                    @Override
                    public void MyOnClick(int position, ProductObj productObj) {
                        setData(productObj);
                    }
                }));
                viewHolder.rcvData.setHasFixedSize(true);
                viewHolder.rcvData.setLayoutManager(new GridLayoutManager(activity, 2));

                viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                        bundle.putString(Args.FILTER, "top");
                        GlobalFunctions.startActivityWithoutAnimation(activity, ProductListActivity.class, bundle);
                    }
                });
            } else if (homeObj.name.equals(AppController.getInstance().getString(R.string.CATEGORIES))) {
//              viewHolder.tvName.setVisibility(View.GONE);
                viewHolder.rcvData.setLayoutManager(new GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false));
                viewHolder.rcvData.setAdapter(new CategoryAdapter(activity, homeObj.categoryList, new MyOnClickCategory() {
                    @Override
                    public void onClick(Category category, int position) {
                        if (category.getHave_child()) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Args.KEY_ID_PRODUCT_CATE, category.getId() + "");
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, category.getName() + "");
                            if (category.getId().equals(DealCateObj.MY_FAVORITES)) {
                                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_FAVORIES);
                            } else {
                                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_NEARBY);
                            }
                            GlobalFunctions.startActivityWithoutAnimation(activity, SubCategoryActivity.class, bundle);//DealsActivity
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Args.KEY_ID_PRODUCT_CATE, category.getId() + "");
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, category.getName() + "");
                            if (category.getId().equals(DealCateObj.MY_FAVORITES)) {
                                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_FAVORIES);
                            } else {
                                bundle.putString(Args.TYPE_OF_SEARCH_PRODUCT, Constants.SEARCH_NEARBY);
                            }
                            GlobalFunctions.startActivityWithoutAnimation(activity, DealsActivity.class, bundle);//DealsActivity
                        }
                    }
                }));
                viewHolder.rcvData.setBackgroundColor(Color.WHITE);
                viewHolder.relative.setVisibility(View.GONE);
            } else if (homeObj.type.equals("TYPE")) {
                String name = homeObj.name;
                if (homeObj.name.equals("SALE PRODUCT")) {
                    viewHolder.lv_count_down.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.lv_count_down.setVisibility(View.GONE);
                }
                LinearLayoutManager linearLayout = new LinearLayoutManager(activity);

//                if(homeObj.name.equals("REKOMENDA"))
//                {
//                    viewHolder.rcvData.setAdapter(new SingleHorizontalAdapter(activity, homeObj.getmListRecomended(), new IMyOnClick() {
//                        @Override
//                        public void MyOnClick(int position, ProductObj productObj) {
//                            setData(productObj);
//                        }
//                    }));
//                }
                viewHolder.rcvData.setAdapter(new SingleHorizontalAdapter(activity, homeObj.productList, new IMyOnClick() {
                    @Override
                    public void MyOnClick(int position, ProductObj productObj) {
                        setData(productObj);
                    }
                }));
                viewHolder.rcvData.setHasFixedSize(true);
                linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
                viewHolder.rcvData.setLayoutManager(linearLayout);
                viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        if (homeObj.name.equals(AppController.getInstance().getString(R.string.PRODUCTLALAIS))) {
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                            bundle.putString(Args.FILTER, "hot");
                        } else if (homeObj.name.equals(AppController.getInstance().getString(R.string.PRODUCTPROMOTION))) {
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                            bundle.putString(Args.FILTER, "new");
                        } else if (homeObj.name.equals("Sale Product")) {
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                            bundle.putString(Args.FILTER, "hot");
                        } else if (homeObj.name.equals("REKOMENDA")) {
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                            bundle.putString(Args.FILTER, "hot");
                        } else if (homeObj.name.equals("PRODUTU POPULAR")) {
                            bundle.putString(Args.TYPE_OF_PRODUCT_NAME, homeObj.name);
                            bundle.putString(Args.FILTER, "hot");
                        }

                        GlobalFunctions.startActivityWithoutAnimation(activity, ProductListActivity.class, bundle);
                    }
                });
                viewHolder.rcvData.setBackgroundColor(activity.getResources().getColor(R.color.white));
                return;
            }
            viewHolder.rcvData.setNestedScrollingEnabled(false);
            viewHolder.rcvData.setRecycledViewPool(viewpool);
            viewHolder.rcvData.setHasFixedSize(true);
        } else if (holder instanceof ViewHolderHeader) {

            ViewHolderHeader viewholder = (ViewHolderHeader) holder;
            viewholder.setAdapter();

        }
    }

    public void setData(ProductObj productObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, productObj);
        Log.e(TAG, "setData: " + new Gson().toJson(productObj));
        GlobalFunctions.startActivityWithoutAnimation(activity, DealDetailActivity.class, bundle);
    }

    @Override
    public int getItemCount() {
//        if (homeObjList != null)

//        return (homeObjList != null && homeObjList.size() > 10) ? 10 : homeObjList.size();
        return (homeObjList == null) ? 0 : homeObjList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MyViewHolder extends BaseViewHolder {

        TextView tvName, tvMore;
        LinearLayout lv_count_down;
        RecyclerView rcvData;
        RelativeLayout relative;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            rcvData = (RecyclerView) itemView.findViewById(R.id.rcv_data);
            lv_count_down = (LinearLayout) itemView.findViewById(R.id.lv_count_down);
            relative = itemView.findViewById(R.id.relative);
        }
    }

    public class ViewHolderHeader extends BaseViewHolder {
        ViewPager viewPager;
        CircleIndicator circleIndicator;
        HeaderItemAdapter adapter;
        TextView tv_account_name_profile,tv_point_profile;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.view_pager);
            tv_account_name_profile = itemView.findViewById(R.id.tv_account_name_profile);
            tv_point_profile = itemView.findViewById(R.id.tv_point_profile);
            circleIndicator = itemView.findViewById(R.id.circle_indicator);
            if(DataStoreManager.getUser()!=null) {
                tv_account_name_profile.setText(DataStoreManager.getUser().getName());
                tv_point_profile.setText("Point : " + (int)DataStoreManager.getUser().getPoint());
            }
        }

        public void setAdapter() {
            adapter = new HeaderItemAdapter(listbanner);
            viewPager.setAdapter(adapter);
            circleIndicator.setViewPager(viewPager);
            adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            adapter.setAutoSlide();
        }

        public class HeaderItemAdapter extends PagerAdapter {
            private static final long DELAY = 5000;
            //private ArrayList<ProductObj> mListData;

            private ArrayList<BannerObj> mListData;
            private Handler handler = new Handler();
            public ImageView imageView;
            private Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int item = viewPager.getCurrentItem();
                    int totalItem = listbanner.size() - 1;
                    if (item < totalItem) {
                        item++;
                        viewPager.setCurrentItem(item);
                    } else if (item == totalItem) {
                        viewPager.setCurrentItem(0);
                    }
                    handler.postDelayed(this, DELAY);
                }
            };

//            public HeaderItemAdapter(ArrayList<ProductObj> mListData) {
//                this.mListData = mListData;
//            }

            public HeaderItemAdapter(ArrayList<BannerObj> mListData) {
                this.mListData = mListData;
            }

            @Override
            public int getCount() {
                return listbanner == null ? 0 : listbanner.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                //final ProductObj item = mListData.get(position);

                final BannerObj item = listbanner.get(position);

                View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_pager_indicator_cate, container, false);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                ImageUtil.setImage(container.getContext(), imageView, item.getImage());
//                Glide.with(context).load(item.getImage()).into(imageView);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, item);
//                        Intent intent = new Intent(activity, DealDetailActivity.class);
//                        intent.putExtras(bundle);
//                        activity.startActivity(intent);
//                    }
//                });
                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                handler.removeCallbacks(runnable);
                container.removeView((RelativeLayout) object);
            }

            public void setAutoSlide() {
                handler.postDelayed(runnable, DELAY);

            }
        }
    }
}
