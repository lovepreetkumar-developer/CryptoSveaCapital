package com.svea.app.fragments;

import android.os.Bundle;

import com.svea.app.R;
import com.svea.app.base.BaseFragment;
import com.svea.app.databinding.FragmentHoldingsBinding;

public class HoldingsFragment extends BaseFragment<FragmentHoldingsBinding> {


    public HoldingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_holdings;
    }

    private void initView() {

    }
}
