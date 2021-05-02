package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.MenuLeft;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/02/2017.
 */

public class MenuLeftAdapte extends RecyclerView.Adapter<MenuLeftAdapte.ViewHolder> {

    private ArrayList<MenuLeft> menuLefts;
    private Context context;
    private MenuLeft menuLeft;
    private IOnItemClickListener iOnItemClickListener;

    public MenuLeftAdapte(ArrayList<MenuLeft> menuLefts, Context context, IOnItemClickListener iOnItemClickListener) {
        this.menuLefts = menuLefts;
        this.context = context;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_left, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        menuLeft = menuLefts.get(position);
        holder.icon.setImageResource(menuLeft.getIcon());
        holder.tvItemMenu.setText(menuLeft.getName());
        if (menuLeft.getId() == 5)
            holder.deviderFunctionMain.setVisibility(View.GONE);
        else
            holder.deviderFunctionMain.setVisibility(View.GONE);
        if (menuLeft.isSelected())
            holder.llItemMenu.setBackgroundColor(context.getResources().getColor(R.color.cam));
        else
            holder.llItemMenu.setBackgroundColor(context.getResources().getColor(R.color.transparent));
    }

    @Override
    public int getItemCount() {
        if (DataStoreManager.getUser() != null)
            return menuLefts.size();
        else
            return menuLefts.size() - 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llItemMenu;
        private ImageView icon;
        private TextView tvItemMenu;
        private View deviderFunctionMain;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.img_icon);
            tvItemMenu = (TextView) itemView.findViewById(R.id.tv_item_menu);
            deviderFunctionMain = itemView.findViewById(R.id.devider_function_main);
            llItemMenu = (LinearLayout) itemView.findViewById(R.id.ll_item_menu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
