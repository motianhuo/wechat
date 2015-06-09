package com.juns.wechat.chat.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.juns.wechat.R;

//表情
public class SmileUtils {
	public static final String f_static_00 = "[):]";
	public static final String f_static_01 = "[):]";
	public static final String f_static_02 = "[:D]";
	public static final String f_static_03 = "[;)]";
	public static final String f_static_04 = "[:-o]";
	public static final String f_static_05 = "[:p]";
	public static final String f_static_06 = "[(H)]";
	public static final String f_static_07 = "[:@]";
	public static final String f_static_08 = "[:s]";
	public static final String f_static_09 = "[:$]";
	public static final String f_static_010 = "[:(]";
	public static final String f_static_011 = "[:'(]";
	public static final String f_static_012 = "[:|]";
	public static final String f_static_013 = "[(a)]";
	public static final String f_static_014 = "[8o|]";
	public static final String f_static_015 = "[8-|]";
	public static final String f_static_016 = "[+o(]";
	public static final String f_static_017 = "[<o)]";
	public static final String f_static_018 = "[|-)]";
	public static final String f_static_019 = "[*-)]";
	public static final String f_static_020 = "[:-#]";
	public static final String f_static_021 = "[:-*]";
	public static final String f_static_022 = "[^o)]";
	public static final String f_static_023 = "[8-)]";
	public static final String f_static_024 = "[(|)]";
	public static final String f_static_025 = "[(u)]";
	public static final String f_static_026 = "[(S)]";
	public static final String f_static_027 = "[(*)]";
	public static final String f_static_028 = "[(#)]";
	public static final String f_static_029 = "[(R)]";
	public static final String f_static_030 = "[({)]";
	public static final String f_static_031 = "[(})]";
	public static final String f_static_032 = "[(k)]";
	public static final String f_static_033 = "[(F)]";
	public static final String f_static_034 = "[(W)]";
	public static final String f_static_035 = "[(D)]";

	private static final Factory spannableFactory = Spannable.Factory
			.getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {
		addPattern(emoticons, f_static_00, R.drawable.f_static_00);
		addPattern(emoticons, f_static_01, R.drawable.f_static_01);
		addPattern(emoticons, f_static_02, R.drawable.f_static_02);
		addPattern(emoticons, f_static_03, R.drawable.f_static_03);
		addPattern(emoticons, f_static_04, R.drawable.f_static_04);
		addPattern(emoticons, f_static_05, R.drawable.f_static_05);
		addPattern(emoticons, f_static_06, R.drawable.f_static_06);
		addPattern(emoticons, f_static_07, R.drawable.f_static_07);
		addPattern(emoticons, f_static_08, R.drawable.f_static_08);
		addPattern(emoticons, f_static_09, R.drawable.f_static_09);
		addPattern(emoticons, f_static_010, R.drawable.f_static_010);
		addPattern(emoticons, f_static_011, R.drawable.f_static_011);
		addPattern(emoticons, f_static_012, R.drawable.f_static_012);
		addPattern(emoticons, f_static_013, R.drawable.f_static_013);
		addPattern(emoticons, f_static_014, R.drawable.f_static_014);
		addPattern(emoticons, f_static_015, R.drawable.f_static_015);
		addPattern(emoticons, f_static_016, R.drawable.f_static_016);
		addPattern(emoticons, f_static_017, R.drawable.f_static_017);
		addPattern(emoticons, f_static_018, R.drawable.f_static_018);
		addPattern(emoticons, f_static_019, R.drawable.f_static_019);
		addPattern(emoticons, f_static_020, R.drawable.f_static_020);
		addPattern(emoticons, f_static_021, R.drawable.f_static_021);
		addPattern(emoticons, f_static_022, R.drawable.f_static_022);
		addPattern(emoticons, f_static_023, R.drawable.f_static_023);
		addPattern(emoticons, f_static_024, R.drawable.f_static_024);
		addPattern(emoticons, f_static_025, R.drawable.f_static_025);
		addPattern(emoticons, f_static_026, R.drawable.f_static_026);
		addPattern(emoticons, f_static_027, R.drawable.f_static_027);
		addPattern(emoticons, f_static_028, R.drawable.f_static_028);
		addPattern(emoticons, f_static_029, R.drawable.f_static_029);
		addPattern(emoticons, f_static_030, R.drawable.f_static_030);
		addPattern(emoticons, f_static_031, R.drawable.f_static_031);
		addPattern(emoticons, f_static_032, R.drawable.f_static_032);
		addPattern(emoticons, f_static_033, R.drawable.f_static_033);
		addPattern(emoticons, f_static_034, R.drawable.f_static_034);
		addPattern(emoticons, f_static_035, R.drawable.f_static_035);
	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
			int resource) {
		map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * 
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
		boolean hasChanges = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(spannable);
			while (matcher.find()) {
				boolean set = true;
				for (ImageSpan span : spannable.getSpans(matcher.start(),
						matcher.end(), ImageSpan.class))
					if (spannable.getSpanStart(span) >= matcher.start()
							&& spannable.getSpanEnd(span) <= matcher.end())
						spannable.removeSpan(span);
					else {
						set = false;
						break;
					}
				if (set) {
					hasChanges = true;
					Drawable drawable = context.getResources().getDrawable(
							entry.getValue());
					drawable.setBounds(0, 0, 50, 50);// 这里设置图片的大小
					ImageSpan imageSpan = new ImageSpan(drawable,
							ImageSpan.ALIGN_BOTTOM);
					spannable.setSpan(imageSpan, matcher.start(),
							matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
		Spannable spannable = spannableFactory.newSpannable(text);
		addSmiles(context, spannable);
		return spannable;
	}

	public static boolean containsKey(String key) {
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(key);
			if (matcher.find()) {
				b = true;
				break;
			}
		}

		return b;
	}

}
