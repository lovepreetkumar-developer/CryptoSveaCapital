package com.svea.app.fragments;

import android.os.Bundle;

import com.svea.app.R;
import com.svea.app.base.BaseCallback;
import com.svea.app.base.BaseFragment;
import com.svea.app.databinding.FragmentBuyBinding;
import com.svea.app.databinding.FragmentSellBinding;

public class FragmentSell extends BaseFragment<FragmentSellBinding> {

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        setBaseCallback(callback);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_sell;
    }

    private BaseCallback callback = view -> {
        if (view.getId() == R.id.button_buy) {
            if (binding.editTextAmount.getText().toString().length() != 0)
                showToast("Amount is :" + binding.editTextAmount.getText().toString());
        }
    };
}