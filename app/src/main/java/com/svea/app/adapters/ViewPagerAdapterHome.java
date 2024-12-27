package com.svea.app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.svea.app.fragments.FragmentBuy;
import com.svea.app.fragments.FragmentSell;
import com.svea.app.fragments.HoldingsFragment;
import com.svea.app.fragments.PriceFragment;

public class ViewPagerAdapterHome extends FragmentPagerAdapter {

    public ViewPagerAdapterHome(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HoldingsFragment();
            case 1:
                return new PriceFragment();
            default:
                throw new IllegalArgumentException("unexpected viewType at :" + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Holdings" : "Prices";
    }
}
