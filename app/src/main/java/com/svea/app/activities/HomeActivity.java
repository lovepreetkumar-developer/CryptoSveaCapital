package com.svea.app.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.svea.app.R;
import com.svea.app.base.BaseActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.databinding.ActivityHomeBinding;
import com.svea.app.fragments.HomeFragment;
import com.svea.app.fragments.SettingsFragment;
import com.svea.app.fragments.TradeViewFragment;
import com.svea.app.fragments.WalletFragment;
import com.svea.app.utils.BackStackManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    public static final String TAB_HOME = "tab_home";
    public static final String TAB_VIDEOS = "tab_videos";
    public static final String TAB_Q_BANK = "tab_q_bank";
    public static final String TAB_TESTS = "tab_tests";

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {

        replaceFragment(TAB_HOME, new HomeFragment(), true);
        binding.bottomMenus.setOnNavigationItemSelectedListener(itemSelectedListener);
        binding.bottomMenus.setSelectedItemId(R.id.menu_home);
        setBaseCallback(baseCallback);

    }

    public void replaceFragment(String tag, Fragment fragment, boolean animation) {
        BackStackManager.getInstance(this).pushFragments(R.id.frame_layout, tag, fragment, animation);
    }

    private BaseCallback baseCallback = view -> {

    };

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                //replaceFragment(TAB_HOME, new HomeFragment(), true);
                setFragment(new HomeFragment());
                break;
            case R.id.menu_wallet:
                //replaceFragment(TAB_Q_BANK, new TradeViewFragment(), true);
                setFragment(new TradeViewFragment());
                break;
            case R.id.menu_add:
                showBottomSheet();
                break;
            case R.id.menu_trade_view:
                //replaceFragment(TAB_VIDEOS, new WalletFragment(), true);
                setFragment(new WalletFragment());
                break;
            case R.id.menu_settings:
                //replaceFragment(TAB_TESTS, new SettingsFragment(), true);
                setFragment(new SettingsFragment());
                break;
        }
        return true;
    };

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_layout, fragment);
        t.commit();
    }

    private void showBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);

        RelativeLayout relativeBuy = view.findViewById(R.id.relative_buy);
        relativeBuy.setOnClickListener(v -> {
            if (v.getId() == R.id.relative_buy) {
                startActivity(new Intent(HomeActivity.this, BuyCoinActivity.class));
                goNextTransition();
            }
            sheetDialog.dismiss();
        });
        sheetDialog.show();
    }
}
