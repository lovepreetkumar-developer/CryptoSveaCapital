package com.svea.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.base.BaseCustomDialog;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.ActivityLoginBinding;
import com.svea.app.databinding.DialogMessageBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.FieldsValidator;
import com.svea.app.utils.PrefUtils;

import java.util.Objects;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements ResponseInterface {


    private FirebaseAuth mFirebaseAuth;
    private FieldsValidator mFieldValidater;
    private BaseCustomDialog<DialogMessageBinding> mErrorDialog;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        if (PrefUtils.getInstance().getUserData() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            goNextTransition();
            finishAffinity();
        } else {
            initView();
        }
    }

    private void initView() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFieldValidater = new FieldsValidator();
        setBaseCallback(baseCallback);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.text_forgot_pass:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                goNextTransition();
                break;
            case R.id.button_login:
                if (validateLogin()) performLogin();
                break;
            case R.id.button_register:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                goNextTransition();
                break;
        }
    };

    private boolean validateLogin() {
        return mFieldValidater.hasText(binding.editTextEmail) &&
                mFieldValidater.hasText(binding.editTextPassword);
    }

    /*Login*/
    private void performLogin() {
        showBaseProgress();
        Apis apis = new Apis(this, this);
        apis.yks_login(binding.editTextEmail.getText().toString(),
                binding.editTextPassword.getText().toString());
        /*if (jsonObject != null) {

            Log.d("Dfd","fdf");
        }
        else {
            hideBaseProgress();
        }*/


        /*mFirebaseAuth.signInWithEmailAndPassword(binding.editTextEmail.getText().toString(),
                binding.editTextPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    hideBaseProgress();
                    if (task.isSuccessful()) {
                        //showSuccessToast("Login Success");
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        if (user != null) {
                            PrefUtils.getInstance().setUserId(user.getUid());
                            getData(user.getUid());
                        }
                    } else {
                        showWarnToast(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });*/
    }

    private void getData(String uid) {

        /*FirebaseFirestore.getInstance().collection(Constants.USER).document(uid).get().addOnCompleteListener(task -> {
            hideBaseProgress();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    UserData data = document.toObject(UserData.class);
                    assert data != null;
                    //PrefUtils.getInstance().setUserData(data);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    Objects.requireNonNull(LoginActivity.this).finishAffinity();
                }
            } else {
                showWarnToast("Failed : " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });*/
    }

    @Override
    public void login(String response) {
        runOnUiThread(() -> {
            hideBaseProgress();
            LoginBean loginBean = convertJsonToPoJo(response);
            if (loginBean != null) {

                if (loginBean.getStatus().equals("OK")) {
                    PrefUtils.getInstance().setUserId(String.valueOf(loginBean.getSid()));
                    if (loginBean.getValue().getStatus().equals("0")) {
                        goToVerifyEmailScreen();
                    } else {
                        PrefUtils.getInstance().setUserData(loginBean);
                        goToHomeScreen();
                    }
                }
            }
        });
    }

    @Override
    public void errorDialog(String response) {

        runOnUiThread(() -> {
            hideBaseProgress();
            LoginBean loginBean = convertJsonToPoJo(response);
            if (loginBean != null) {
                mErrorDialog = new BaseCustomDialog<>(LoginActivity.this, R.layout.dialog_message, view1 -> {
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

    private void goToVerifyEmailScreen() {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
