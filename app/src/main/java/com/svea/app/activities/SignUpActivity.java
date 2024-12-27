package com.svea.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.base.BaseCustomDialog;
import com.svea.app.beans.UserData;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.ActivitySignUpBinding;
import com.svea.app.databinding.DialogMessageBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.Constants;
import com.svea.app.utils.FieldsValidator;
import com.svea.app.utils.PrefUtils;

import java.util.Objects;

public class SignUpActivity extends BaseActivity<ActivitySignUpBinding> implements ResponseInterface {

    private FirebaseAuth mFirebaseAuth;
    private FieldsValidator mFieldValidater;
    private BaseCustomDialog<DialogMessageBinding> mErrorDialog;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFieldValidater = new FieldsValidator();
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_register:
                if (validateSignUp()) performSignUp();
                break;
        }
    };


    private boolean validateSignUp() {
        return mFieldValidater.hasText(binding.editTextFirstName) &&
                mFieldValidater.hasText(binding.editTextLastName) &&
                mFieldValidater.hasText(binding.editPhoneNumber) &&
                mFieldValidater.hasText(binding.editTextEmailSUp) &&
                mFieldValidater.hasText(binding.editTextPasswordSUp);
    }


    /*Sign Up*/
    private void performSignUp() {

        showBaseProgress();
        /*mFirebaseAuth.createUserWithEmailAndPassword(binding.editTextEmailSUp.getText().toString(),
                binding.editTextPasswordSUp.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    PrefUtils.getInstance().setUserId(user.getUid());
                    saveData();
                }
            } else {
                hideBaseProgress();
                showWarnToast(Objects.requireNonNull(task.getException()).getMessage());
            }
        });*/

        showBaseProgress();
        Apis apis = new Apis(this, this);
        apis.yks_register(binding.editTextFirstName.getText().toString(),
                binding.editTextLastName.getText().toString(),
                binding.editTextEmailSUp.getText().toString(),
                binding.editPhoneNumber.getText().toString(),
                binding.editTextPasswordSUp.getText().toString(),
                binding.editTextPasswordSUp.getText().toString());

    }

    private void saveData() {
        UserData userData = new UserData();
        userData.setEmail(binding.editTextEmailSUp.getText().toString());
        userData.setWallet_amount("0");
        userData.setFirstname(binding.editTextFirstName.getText().toString());
        userData.setLastname(binding.editTextLastName.getText().toString());
        userData.setPhone(binding.editPhoneNumber.getText().toString());
        // userData.setUid(PrefUtils.getInstance().getUserId());

        FirebaseFirestore.getInstance().collection(Constants.USER).document(PrefUtils.getInstance().getUserId()).set(userData).addOnSuccessListener(aVoid -> {
            hideBaseProgress();
            //PrefUtils.getInstance().setUserData(userData);
            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
            Objects.requireNonNull(SignUpActivity.this).finishAffinity();
        }).addOnFailureListener(e -> {
            hideBaseProgress();
        });

    }

    @Override
    public void signUp(String response) {

        hideBaseProgress();
        LoginBean loginBean = convertJsonToPojo(response);
        if (loginBean != null) {

            String walamout = loginBean.getValue().getWallet();
            if (loginBean.getStatus().equals("OK")) {
                PrefUtils.getInstance().setUserId(String.valueOf(loginBean.getSid()));
                if (loginBean.getValue().getStatus().equals("0")) {
                    goToVerifyEmailScreen();
                } else {
                    PrefUtils.getInstance().setUserData(loginBean);
                    goToHomeScreen();
                }
            } else {
                showErrorToast(loginBean.getMsg());
            }
        }
    }

    @Override
    public void errorDialog(String response) {
        hideBaseProgress();
        runOnUiThread(() -> {

            LoginBean loginBean = convertJsonToPojo(response);
            if (loginBean != null) {
                mErrorDialog = new BaseCustomDialog<>(SignUpActivity.this, R.layout.dialog_message, view1 -> {
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

    private LoginBean convertJsonToPojo(String json) {
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
