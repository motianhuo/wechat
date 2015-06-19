package com.juns.wechat.common;

import java.util.Comparator;

import com.juns.wechat.bean.User;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// String str1 = PingYinUtil.getPingYin((String) o1);
		// String str2 = PingYinUtil.getPingYin((String) o2);
		// return str1.compareTo(str2);

		User user0 = (User) arg0;
		User user1 = (User) arg1;
		// 按照名字排序
		String catalog0 = PingYinUtil
				.converterToFirstSpell(user0.getUserName()).substring(0, 1);
		String catalog1 = PingYinUtil
				.converterToFirstSpell(user1.getUserName()).substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
