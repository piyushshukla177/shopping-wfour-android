package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.MenuLeft;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/02/2017.
 */

public class MenuLeftAdapter extends BaseAdapter{
    private ArrayList<MenuLeft> menuLefts;
    private Context context;
    private LayoutInflater inflater;
    private IOnItemClickListener iOnItemClickListener;

    public MenuLeftAdapter(ArrayList<MenuLeft> menuLefts, Context context,  IOnItemClickListener iOnItemClickListener) {
        this.menuLefts = menuLefts;
        this.context = context;
        this.iOnItemClickListener = iOnItemClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        try {
            return menuLefts.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_menu_left, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.tvItemMenu = (TextView) convertView.findViewById(R.id.tv_item_menu);
            holder.deviderFunctionMain = convertView.findViewById(R.id.devider_function_main);
            holder.llItemMenu = (LinearLayout) convertView.findViewById(R.id.ll_item_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindData(holder, position);

        return convertView;
    }

    private void bindData(ViewHolder holder, int position) {
        holder.icon.setImageResource(menuLefts.get(position).getIcon());
        holder.tvItemMenu.setText(menuLefts.get(position).getName());

        if (menuLefts.get(position).getId()== Constants.MENU_PROFILE)
            holder.deviderFunctionMain.setVisibility(View.VISIBLE);
        else
            holder.deviderFunctionMain.setVisibility(View.GONE);

        if (menuLefts.get(position).isSelected())
            holder.llItemMenu.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        else
            holder.llItemMenu.setBackgroundColor(context.getResources().getColor(R.color.transparent));
    }

    class ViewHolder{
        private LinearLayout llItemMenu;
        private ImageView icon;
        private TextView tvItemMenu;
        private View deviderFunctionMain;

    }
}
