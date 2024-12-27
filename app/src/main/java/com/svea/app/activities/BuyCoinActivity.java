package com.svea.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.databinding.ActivityBuyCoinBinding;

public class BuyCoinActivity extends BaseActivity<ActivityBuyCoinBinding> {

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_buy_coin;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        binding.toolbar.textTitle.setText("Buy Sveacoin");
        setBaseCallback(callback);
    }

    private BaseCallback callback = view -> {

        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_add:
                startActivity(new Intent(BuyCoinActivity.this, AddPaymentMethodActivity.class));
                goNextTransition();
                break;
            case R.id.button_preview_buy:
                showToast("In progress");
                break;
            case R.id.t9_key_1: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key1.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key1.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_2: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key2.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key2.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_3: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key3.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key3.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_4: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key4.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key4.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_5: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key5.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key5.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_6: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key6.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key6.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_7: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key7.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key7.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_8: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key8.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key8.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_9: {
                if (binding.textAmount.getText().toString().equals("0")) {
                    binding.textAmount.setText(binding.t9Key9.getText().toString());
                } else {
                    String s = binding.textAmount.getText().toString().concat(binding.t9Key9.getText().toString());
                    binding.textAmount.setText(s);
                }
                break;
            }
            case R.id.t9_key_clear: {
                binding.textAmount.setText("0");
                break;
            }
            case R.id.t9_key_backspace: {
                if (!binding.textAmount.getText().toString().equals("0"))
                    binding.textAmount.setText(backSpaceWorking(binding.textAmount.getText().toString()));
                break;
            }
        }
    };

    public String backSpaceWorking(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
