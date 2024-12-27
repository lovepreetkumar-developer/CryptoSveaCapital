package com.svea.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.databinding.ActivityAddPaymentMethodBinding;
import com.svea.app.databinding.ActivityBuyCoinBinding;

public class AddPaymentMethodActivity extends BaseActivity<ActivityAddPaymentMethodBinding> {

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_payment_method;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {

        binding.toolbar.textTitle.setText("Add Payment Method");
        setBaseCallback(callback);
    }

    private BaseCallback callback = view -> {

        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_add:
                break;
        }
    };

}
