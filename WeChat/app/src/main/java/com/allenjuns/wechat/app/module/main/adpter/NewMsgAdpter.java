package com.allenjuns.wechat.app.module.main.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.common.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-10
 */
public class NewMsgAdpter extends BaseAdapter {
    protected Context context;

    public NewMsgAdpter(Context ctx) {
        context = ctx;
    }

    @Override
    public int getCount() {
        return 5;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_msg, parent, false);
        }
        CircleImageView img_avar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
        TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
        TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
        TextView unreadLabel = ViewHolder.get(convertView,
                R.id.unread_msg_number);
        if (position == 0) {
            img_avar.setImageResource(R.drawable.icon_public);
            txt_name.setText("订阅号");
            txt_content.setText("一个逐欲成长又胆怯流逝的年纪");
        } else if (position == 1) {
            img_avar.setImageResource(R.drawable.icon_qunliao);
            txt_name.setText("大家族");
            txt_content.setText("课余围在一起讨论游戏");
        } else {
            txt_name.setText("老婆");
            txt_content.setText("吃饭了吗?");
            unreadLabel.setText("12");
            unreadLabel.setVisibility(View.VISIBLE);
        }
        txt_time.setText("昨天");
        return convertView;
    }
}
