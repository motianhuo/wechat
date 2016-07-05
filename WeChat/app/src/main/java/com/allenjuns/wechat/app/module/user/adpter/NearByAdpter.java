package com.allenjuns.wechat.app.module.user.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.bean.UserInfo;
import com.allenjuns.wechat.common.ViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.utils.CropCircleTransformation;

import java.util.ArrayList;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-15
 */
public class NearByAdpter extends BaseAdapter {
    protected Context context;
    ArrayList<UserInfo> objs=new ArrayList<UserInfo>();

    public NearByAdpter(Context ctx) {
        context = ctx;
    }

    public void addAll(ArrayList<UserInfo> collection) {
        if (isEmpty()) {
            objs.addAll(collection);
            notifyDataSetChanged();
        } else {
            objs.addAll(collection);
        }
    }

    public void clear() {
        objs.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        if (objs == null)
            return true;
        else
            return objs.isEmpty();
    }

    public void remove(int index) {
        if (index > -1 && index < objs.size()) {
            objs.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return objs.size();
    }

    @Override
    public Object getItem(int position) {
        if (objs == null || objs.size() == 0) return null;
        return objs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_nearby, parent, false);
        }
        ImageView img_avar = ViewHolder.get(convertView,
                R.id.portrait);
        UserInfo info = (UserInfo) getItem(position);
        TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
        TextView education = ViewHolder.get(convertView, R.id.education);
        TextView city = ViewHolder.get(convertView, R.id.city);
        TextView work_year = ViewHolder.get(convertView, R.id.work_year);
        TextView favorite = ViewHolder.get(convertView, R.id.favorite);
        txt_name.setText(info.getNickname());
        education.setText("硕士");
        city.setText("北京");
        work_year.setText("5年");
        favorite.setText("+ 关注");
        Glide.with(context)
                .load(info.getUser_avar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(com.hyphenate.easeui.R.drawable.ease_default_image)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(img_avar);
        return convertView;
    }
}
