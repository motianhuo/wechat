package com.allenjuns.wechat.app.module.account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.app.model.account.LoginCallback;
import com.allenjuns.wechat.app.model.account.LoginModelImpl;
import com.allenjuns.wechat.chatuidemo.ChatHelper;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.utils.L;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :注册
 * Author : AllenJuns
 * Date   : 2016-3-03
 */
public class RegiseterActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.et_usertel)
    EditText userNameEditText;
    @Bind(R.id.et_password)
    EditText passwordEditText;
    @Bind(R.id.et_code)
    EditText et_codeEditText;

    private LoginModelImpl LoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        txt_title.setText(getString(R.string.login_btn_register));
        img_back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        LoginModel = new LoginModelImpl();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_send)
    public void btn_send() {
        et_codeEditText.setText("1825");
    }

    @OnClick(R.id.btn_register)
    public void btn_register() {
        register();
    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }

    public void register() {
        final String username = userNameEditText.getText().toString().trim();
        final String pwd = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            userNameEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(username, pwd);
                        LoginModel.login(username, pwd, callback);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegiseterActivity.this.isFinishing())
                                    pd.dismiss();
                                // save current user
                                ChatHelper.getInstance().setCurrentUserName(username);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), 0).show();
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegiseterActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NETWORK_ERROR) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

    LoginCallback callback = new LoginCallback() {
        @Override
        public void onSuccess(String data) {
            L.e(data);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().addContact("1245238818", "");
                    } catch (final HyphenateException e) {
                    }
                    MFGT.gotoMainActivity(RegiseterActivity.this);
                    finish();
                }
            }).start();
        }

        @Override
        public void onFailure(String error) {
            L.e(error);
        }
    };

}
