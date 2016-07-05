/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseEmojiconInfoProvider;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;
import com.hyphenate.util.DensityUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String ee_1 = "/::)";
    public static final String ee_2 = "/::~";
    public static final String ee_3 = "/::B";
    public static final String ee_4 = "/::|";
    public static final String ee_5 = "/:8-)";
    public static final String ee_6 = "/::<";
    public static final String ee_7 = "/::$";
    public static final String ee_8 = "/::X";
    public static final String ee_9 = "/::Z";
    public static final String ee_10 = "/::'(";
    public static final String ee_11 = "/::-|";
    public static final String ee_12 = "/::@";
    public static final String ee_13 = "/::P";
    public static final String ee_14 = "/::D";
    public static final String ee_15 = "/::O";
    public static final String ee_16 = "/::(";
    public static final String ee_17 = "/::+";
    public static final String ee_18 = "/:--b";
    public static final String ee_19 = "/::Q";
    public static final String ee_20 = "/::T";
    public static final String ee_21 = "/:,@P";
    public static final String ee_22 = "/:,@-D";
    public static final String ee_23 = "/::d";
    public static final String ee_24 = "/:,@o";
    public static final String ee_25 = "/::g";
    public static final String ee_26 = "/:|-)";
    public static final String ee_27 = "/::!";
    public static final String ee_28 = "/::L";
    public static final String ee_29 = "/::>";
    public static final String ee_30 = "/::,@";
    public static final String ee_31 = "/:,@f";
    public static final String ee_32 = "/::-S";
    public static final String ee_33 = "/:?";
    public static final String ee_34 = "/:,@x";
    public static final String ee_35 = "/:,@@";
    public static final String ee_36 = "/::8";
    public static final String ee_37 = "/:,@!";
    public static final String ee_38 = "/:!!!";
    public static final String ee_39 = "/:xx";
    public static final String ee_40 = "/:bye";
    public static final String ee_41 = "/:wipe";
    public static final String ee_42 = "/:dig";
    public static final String ee_43 = "/:handclap";
    public static final String ee_44 = "/:&-(";
    public static final String ee_45 = "/:B-)";
    public static final String ee_46 = "/:<@";
    public static final String ee_47 = "/:@>";
    public static final String ee_48 = "/::-O";
    public static final String ee_49 = "/:>-|";
    public static final String ee_50 = "/:P-(";
    public static final String ee_51 = "/::'|";
    public static final String ee_52 = "/:X-)";
    public static final String ee_53 = "/::*";
    public static final String ee_54 = "/:@x";
    public static final String ee_55 = "/:8*";
    public static final String ee_56 = "/:pd";
    public static final String ee_57 = "/:<W>";
    public static final String ee_58 = "/:beer";
    public static final String ee_59 = "/:basketb";
    public static final String ee_60 = "/:oo";
    public static final String ee_61 = "/:coffee";
    public static final String ee_62 = "/:eat";
    public static final String ee_63 = "/:pig";
    public static final String ee_64 = "/:rose";
    public static final String ee_65 = "/:fade";
    public static final String ee_66 = "/:showlove";
    public static final String ee_67 = "/:heart";
    public static final String ee_68 = "/:break";
    public static final String ee_69 = "/:cake";
    public static final String ee_70 = "/:li";
    public static final String ee_71 = "/:bome";
    public static final String ee_72 = "/:kn";
    public static final String ee_73 = "/:footb";
    public static final String ee_74 = "/:ladybug";
    public static final String ee_75 = "/:shit";
    public static final String ee_76 = "/:moon";
    public static final String ee_77 = "/:sun";
    public static final String ee_78 = "/:gift";
    public static final String ee_79 = "/:hug";
    public static final String ee_80 = "/:strong";
    public static final String ee_81 = "/:weak";
    public static final String ee_82 = "/:share";
    public static final String ee_83 = "/:v";
    public static final String ee_84 = "/:@)";
    public static final String ee_85 = "/:jj";
    public static final String ee_86 = "/:@@";
    public static final String ee_87 = "/:bad";
    public static final String ee_88 = "/:lvu";
    public static final String ee_89 = "/:no";
    public static final String ee_90 = "/:ok";
    public static final String ee_91 = "/:love";
    public static final String ee_92 = "/:<L>";
    public static final String ee_93 = "/:jump";
    public static final String ee_94 = "/:shake";
    public static final String ee_95 = "/:<O>";
    public static final String ee_96 = "/:circle";
    public static final String ee_97 = "/:kotow";
    public static final String ee_98 = "/:turn";
    public static final String ee_99 = "/:skip";
    public static final String ee_100 = "/:oY";
//    public static final String ee_101 = "/:ee_101";
//    public static final String ee_102 = "/:ee_102";
//    public static final String ee_103 = "/:ee_103";
//    public static final String ee_104 = "/:ee_104";
//    public static final String ee_105 = "/:ee_105";

    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
        for (int i = 0; i < emojicons.length; i++) {
            addPattern(emojicons[i].getEmojiText(), emojicons[i].getIcon());
        }
        EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
                addPattern(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * 添加文字表情mapping
     *
     * @param emojiText emoji文本内容
     * @param icon      图片资源id或者本地路径
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
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
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
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
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        Drawable drawable = context.getResources().getDrawable(
                                (Integer) value);

                        drawable.setBounds(0, 0, DensityUtil.sp2px(context,20), DensityUtil.sp2px(context, 20));// 这里设置图片的大小
                        ImageSpan imageSpan = new ImageSpan(drawable,
                                ImageSpan.ALIGN_BOTTOM);
                        spannable.setSpan(imageSpan,
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
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
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
