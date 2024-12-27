package com.svea.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.protobuf.Api;
import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCustomDialog;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.ActivityForgetPasswordBinding;
import com.svea.app.databinding.DialogMessageBinding;
import com.svea.app.server.Apis;

import java.util.Objects;

public class ForgetPasswordActivity extends BaseActivity<ActivityForgetPasswordBinding> {

    //private Call<SuccessBean> callForgotPassword;
    private BaseCustomDialog<DialogMessageBinding> mErrorDialog;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        setBaseCallback(view -> {
            switch (view.getId()) {
                case R.id.image_back:
                    goBack();
                    break;
                case R.id.button_submit:
                    if (binding.editTextEmail.getText().length() > 0) forgetPassword();
                    else binding.editTextEmail.setText(R.string.please_fill);
                    break;
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void onDestroy() {
        //if (callForgotPassword != null) callForgotPassword.cancel();
        super.onDestroy();
    }

    private void forgetPassword() {
        showBaseProgress();
        Apis apis = new Apis(this, this);
        apis.yks_forgotpassword(binding.editTextEmail.getText().toString());
        /*FirebaseAuth.getInstance().sendPasswordResetEmail(binding.editTextEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.editTextEmail.setText("");
                        showSuccessToast("Email has been sent.");
                        goBack();
                        //Log.d(TAG, "Email sent.");
                    } else {
                        Log.d("dfd", task.toString());
                        //showSuccessToast("Email could not been sent.");
                    }
                });*/
    }

    @Override
    public void forgotPassword(String response) {
        runOnUiThread(() -> {
            hideBaseProgress();
            onBackPressed();
            showToast("Email has been sent, Please check");
        });
    }

    @Override
    public void errorDialog(String response) {

        runOnUiThread(() -> {
            hideBaseProgress();
            LoginBean loginBean = convertJsonToPoJo(response);
            if (loginBean != null) {
                mErrorDialog = new BaseCustomDialog<>(ForgetPasswordActivity.this, R.layout.dialog_message, view1 -> {
                    if (view1.getId() == R.id.text_close) {
                        mErrorDialog.dismiss();
                    }
                });
                mErrorDialog.getBinding().textMessage.setText(loginBean.getMsg());
                mErrorDialog.getBinding().textClose.setText("Cancel");
                Objects.requireNonNull(mErrorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mErrorDialog.setCancelable(true);
                mErrorDialog.show();
            }
        });
    }

    private LoginBean convertJsonToPoJo(String json) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(json);
        Gson gson = new Gson();
        return gson.fromJson(mJson, LoginBean.class);
    }
}
