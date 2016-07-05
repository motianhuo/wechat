package com.hyphenate.easeui.domain;

public class EaseEmojicon {
    public EaseEmojicon(){
    }
    
    /**
     * constructor
     * @param icon- resource id of the icon
     * @param emojiText- text of emoji icon
     */
    public EaseEmojicon(int icon, String emojiText){
        this.icon = icon;
        this.emojiText = emojiText;
        this.type = Type.NORMAL;
    }
    
    /**
     * constructor
     * @param icon - resource id of the icon
     * @param emojiText - text of emoji icon
     * @param type - normal or big
     */
    public EaseEmojicon(int icon, String emojiText, Type type){
        this.icon = icon;
        this.emojiText = emojiText;
        this.type = type;
    }
    
    
    /**
     * identity code
     */
    private String identityCode;
    
    /**
     * static icon resource id
     */
    private int icon;
    
    /**
     * dynamic icon resource id
     */
    private int bigIcon;
    
    /**
     * text of emoji, could be null for big icon
     */
    private String emojiText;
    
    /**
     * name of emoji icon
     */
    private String name;
    
    /**
     * normal or big
     */
    private Type type;
    
    /**
     * path of icon
     */
    private String iconPath;
    
    /**
     * path of big icon
     */
    private String bigIconPath;
    
    
    /**
     * get the resource id of the icon
     * @return
     */
    public int getIcon() {
        return icon;
    }


    /**
     * set the resource id of the icon
     * @param icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }


    /**
     * get the resource id of the big icon
     * @return
     */
    public int getBigIcon() {
        return bigIcon;
    }


    /**
     * set the resource id of the big icon
     * @return
     */
    public void setBigIcon(int dynamicIcon) {
        this.bigIcon = dynamicIcon;
    }


    /**
     * get text of emoji icon
     * @return
     */
    public String getEmojiText() {
        return emojiText;
    }


    /**
     * set text of emoji icon
     * @param emojiText
     */
    public void setEmojiText(String emojiText) {
        this.emojiText = emojiText;
    }

    /**
     * get name of emoji icon
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * set name of emoji icon
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get type
     * @return
     */
    public Type getType() {
        return type;
    }


    /**
     * set type
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }


    /**
     * get icon path
     * @return
     */
    public String getIconPath() {
        return iconPath;
    }


    /**
     * set icon path
     * @param iconPath
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }


    /**
     * get path of big icon
     * @return
     */
    public String getBigIconPath() {
        return bigIconPath;
    }


    /**
     * set path of big icon
     * @param bigIconPath
     */
    public void setBigIconPath(String bigIconPath) {
        this.bigIconPath = bigIconPath;
    }

    /**
     * get identity code
     * @return
     */
    public String getIdentityCode() {
        return identityCode;
    }
    
    /**
     * set identity code
     * @param identityId
     */
    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public static final String newEmojiText(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }



    public enum Type{
        /**
         * normal icon, can be input one or more in edit view
         */
        NORMAL,
        /**
         * big icon, send out directly when your press it
         */
        BIG_EXPRESSION
    }
}
