package com.allenjuns.wechat.utils;

import android.content.Context;

/**
 * Description :R资源反射工具类
 * Author : AllenJuns
 * Date   : 2016-3-04
 */
public class RUtils {
    public static final String POINT = ".";
    public static final String R = "R";
    public static final String JOIN = "$";
    public static final String ANIM = "anim";
    public static final String ATTR = "attr";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String RAW = "raw";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String STYLEABLE = "styleable";

    /**
     * Don't let anyone instantiate this class.
     */
    private RUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 获取动画ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getAnim(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ANIM)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取属性ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getAttr(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ATTR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取颜色ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getColor(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + COLOR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取尺寸ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getDimen(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + DIMEN)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取图片ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getDrawable(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + DRAWABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getId(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ID)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取布局ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getLayout(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + LAYOUT).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取菜单ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getMenu(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + MENU)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取RAW ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getRaw(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + RAW)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取字符串ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getString(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STRING).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取样式ID
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int getStyle(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + STYLE)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取自定义属性组
     *
     * @param context 上下文
     * @param name    名称
     */
    public static int[] getStyleable(Context context, String name) {
        try {
            return (int[]) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STYLEABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取自定义属性
     *
     * @param context       上下文
     * @param styleableName 属性组名称
     * @param attributeName 属性名称
     */
    public static int getStyleableAttribute(Context context,
                                            String styleableName, String attributeName) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STYLEABLE)
                    .getDeclaredField(styleableName + "_" + attributeName)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
