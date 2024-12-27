package com.svea.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.svea.app.R;
import com.svea.app.activities.LoginActivity;
import com.svea.app.base.BaseCallback;
import com.svea.app.base.BaseCustomDialog;
import com.svea.app.base.BaseFragment;
import com.svea.app.beans.UserData;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.DialogMessageBinding;
import com.svea.app.databinding.FragmentSettingsBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.PrefUtils;

import java.util.Objects;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> implements ResponseInterface {

    private BaseCustomDialog<DialogMessageBinding> mErrorDialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        initView();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void logout(String response) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideBaseProgress();
            LoginBean loginBean = convertJsonToPojo(response);
            if (loginBean != null) {

                if (loginBean.getStatus().equals("OK")) {

                    PrefUtils.getInstance().setUserData(null);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    goNextTransition();
                    Objects.requireNonNull(getActivity()).finishAffinity();
                }
            }
        });
    }

    private LoginBean convertJsonToPojo(String json) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(json);
        Gson gson = new Gson();
        return gson.fromJson(mJson, LoginBean.class);
    }

    private void initView() {
        binding.toolbar.textTitle.setText("Settings");
        setBaseCallback(callback);
        setData();
    }

    private void setData() {
        LoginBean loginBean = PrefUtils.getInstance().getUserData();
        if (loginBean != null) {
            binding.textUserName.setText(String.format("%s %s", loginBean.getValue().getFname(), loginBean.getValue().getLname()));
            binding.textPhone.setText(loginBean.getValue().getPhone());
            binding.textEmail.setText(loginBean.getValue().getEmail());
        } else {
            Apis ykh = new Apis(getActivity(), this);
            ykh.yks_viewprofile();
        }
    }

    @Override
    public void viewProfile(String response) {

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            LoginBean loginBean = convertJsonToPojo(response);
            if (loginBean != null) {

                if (loginBean.getStatus().equals("OK")) {
                    PrefUtils.getInstance().setUserId(String.valueOf(loginBean.getSid()));
                    binding.textUserName.setText(String.format("%s %s", loginBean.getValue().getFname(), loginBean.getValue().getLname()));
                    binding.textPhone.setText(loginBean.getValue().getPhone());
                    binding.textEmail.setText(loginBean.getValue().getEmail());
                }
            }
        });

    }

    private BaseCallback callback = view -> {
        if (view.getId() == R.id.text_log_out) {
            //FirebaseAuth.getInstance().signOut();

            mErrorDialog = new BaseCustomDialog<>(Objects.requireNonNull(getActivity()), R.layout.dialog_message, view1 -> {
                if (view1.getId() == R.id.text_close) {
                    mErrorDialog.dismiss();
                    showLogoutDialog();
                    Apis apis = new Apis(getActivity(), this);
                    apis.yks_logout();
                }
            });
            mErrorDialog.getBinding().textMessage.setText("Are you sure you want to logout?");
            mErrorDialog.getBinding().textClose.setText("Logout");
            Objects.requireNonNull(mErrorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mErrorDialog.setCancelable(true);
            mErrorDialog.show();
        }
    };

    private void showLogoutDialog() {

    }

    public void updateViews(UserData userData) {
        binding.textEmail.setText(userData.getEmail());
        binding.textUserName.setText(userData.getFirstname() + " " + userData.getLastname());
        binding.textPhone.setText(userData.getPhone());
    }
}
