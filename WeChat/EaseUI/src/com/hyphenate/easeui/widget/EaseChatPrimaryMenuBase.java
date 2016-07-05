package com.hyphenate.easeui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public abstract class EaseChatPrimaryMenuBase extends RelativeLayout{
    protected EaseChatPrimaryMenuListener listener;
    protected Activity activity;
    protected InputMethodManager inputManager;

    public EaseChatPrimaryMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public EaseChatPrimaryMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseChatPrimaryMenuBase(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context){
        this.activity = (Activity) context;
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    
    /**
     * set primary menu listener
     * @param listener
     */
    public void setChatPrimaryMenuListener(EaseChatPrimaryMenuListener listener){
        this.listener = listener;
    }
    
    /**
     * emoji icon input event
     * @param emojiContent
     */
    public abstract void onEmojiconInputEvent(CharSequence emojiContent);

    /**
     * emoji icon delete event
     */
    public abstract void onEmojiconDeleteEvent();
    
    /**
     * hide extend menu
     */
    public abstract void onExtendMenuContainerHide();
    
    
    /**
     * insert text
     * @param text
     */
    public abstract void onTextInsert(CharSequence text);
    
    public abstract EditText getEditText();
    
    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    
    public interface EaseChatPrimaryMenuListener{
        /**
         * when send button clicked
         * @param content
         */
        void onSendBtnClicked(String content);
        
        /**
         * when speak button is touched
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
        
        /**
         * toggle on/off voice button
         */
        void onToggleVoiceBtnClicked();
        
        /**
         * toggle on/off extend menu
         */
        void onToggleExtendClicked();
        
        /**
         * toggle on/off emoji icon
         */
        void onToggleEmojiconClicked();
        
        /**
         * on text input is clicked
         */
        void onEditTextClicked();
        
    }

}
