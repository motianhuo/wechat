//package com.allenjuns.wechat.app.module.main.adpter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.SectionIndexer;
//import android.widget.TextView;
//
//import com.allenjuns.wechat.R;
//import com.allenjuns.wechat.app.module.user.FriendInfoActivity;
//import com.allenjuns.wechat.utils.PingYinUtil;
//import com.allenjuns.wechat.utils.PinyinComparator;
//
//import java.util.Arrays;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Description :
// * Author : AllenJuns
// * Date   : 2016-3-09
// */
//public class ContactAdapter extends BaseAdapter implements SectionIndexer {
//    private Context mContext;
//    private String[] mNicks;
//    private static String[] nicks = {"阿雅", "北风", "张山", "李四", "欧阳锋", "郭靖",
//            "黄蓉", "杨过", "凤姐", "芙蓉姐姐", "移联网", "樱木花道", "风清扬", "张三丰", "梅超风"};
//
//    @SuppressWarnings("unchecked")
//    public ContactAdapter(Context mContext) {
//        this.mContext = mContext;
//        this.mNicks = nicks;
//        // 排序(实现了中英文混排)
//        Arrays.sort(mNicks, new PinyinComparator());
//    }
//
//    @Override
//    public int getCount() {
//        return mNicks.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mNicks[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final String nickName = mNicks[position];
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(
//                    R.layout.view_contact_item, null);
//            viewHolder = new ViewHolder();
//            viewHolder.tvCatalog = (TextView) convertView
//                    .findViewById(R.id.contactitem_catalog);
//            viewHolder.ivAvatar = (CircleImageView) convertView
//                    .findViewById(R.id.contactitem_avatar_iv);
//            viewHolder.tvNick = (TextView) convertView
//                    .findViewById(R.id.contactitem_nick);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        String catalog = PingYinUtil.converterToFirstSpell(nickName)
//                .substring(0, 1);
//        if (position == 0) {
//            viewHolder.tvCatalog.setVisibility(View.VISIBLE);
//            viewHolder.tvCatalog.setText(catalog);
//        } else {
//            String lastCatalog = PingYinUtil.converterToFirstSpell(
//                    mNicks[position - 1]).substring(0, 1);
//            if (catalog.equals(lastCatalog)) {
//                viewHolder.tvCatalog.setVisibility(View.GONE);
//            } else {
//                viewHolder.tvCatalog.setVisibility(View.VISIBLE);
//                viewHolder.tvCatalog.setText(catalog);
//            }
//        }
//
//        viewHolder.ivAvatar.setImageResource(R.drawable.head);
//        viewHolder.tvNick.setText(nickName);
////        viewHolder.ivAvatar.setOnClickListener(onUserInfoClick);
//        return convertView;
//    }
//
//    static class ViewHolder {
//        TextView tvCatalog;// 目录
//        CircleImageView ivAvatar;// 头像
//        TextView tvNick;// 昵称
//    }
//
//    @Override
//    public int getPositionForSection(int section) {
//        for (int i = 0; i < mNicks.length; i++) {
//            String l = PingYinUtil.converterToFirstSpell(mNicks[i])
//                    .substring(0, 1);
//            char firstChar = l.toUpperCase().charAt(0);
//            if (firstChar == section) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    @Override
//    public int getSectionForPosition(int position) {
//        return 0;
//    }
//
//    @Override
//    public Object[] getSections() {
//        return null;
//    }
//
//    public View.OnClickListener onUserInfoClick=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Activity act=(Activity)mContext;
//            int[] startingLocation = new int[2];
//            view.getLocationOnScreen(startingLocation);
//            startingLocation[0] += view.getWidth() / 2;
//            FriendInfoActivity.startUserProfileFromLocation(startingLocation,act);
//            act. overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//        }
//    };
//}
