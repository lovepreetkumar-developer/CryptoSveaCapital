package com.svea.app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.databinding.ActivityVerifyEmailBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.FieldsValidator;

public class VerifyEmailActivity extends BaseActivity<ActivityVerifyEmailBinding> implements ResponseInterface {

    private FieldsValidator mFieldValidater;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_email;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        mFieldValidater = new FieldsValidator();
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_verify:
                if (validate()) verifyOtp();
                break;
            case R.id.text_resend:
                Apis apis =new Apis(this,this);
                apis.yks_resendotp();
                break;
        }
    };


    private boolean validate() {
        return mFieldValidater.hasText(binding.editTextOpt);
    }


    private void verifyOtp() {

        //showBaseProgress();

        Apis apis =new Apis(this,this);
        apis.yks_otpverify(binding.editTextOpt.getText().toString());
    }

}
