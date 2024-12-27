package com.svea.app.fragments;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.svea.app.R;
import com.svea.app.base.BaseFragment;
import com.svea.app.beans.UserData;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.FragmentWalletBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.Constants;
import com.svea.app.utils.PrefUtils;

import java.util.Objects;

public class WalletFragment extends BaseFragment<FragmentWalletBinding> implements ResponseInterface {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        initView();

        if (PrefUtils.getInstance().getUserId() != null) {
            getData();
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_wallet;
    }

    private void initView() {

        binding.toolbar.textTitle.setText("Wallet");


    }

    private void getData() {

        //showBaseProgress();
        Apis apis = new Apis(getActivity(), this);
        apis.yks_viewwallet();

        /*try {
            FirebaseFirestore.getInstance().collection(Constants.USER)
                    .document(PrefUtils.getInstance().getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        hideBaseProgress();
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                UserData data = document.toObject(UserData.class);
                                if (data != null) {
                                    if (data.getWallet_amount() != null) {
                                        binding.textAmount.setText("€ " + data.getWallet_amount());
                                    }
                                }

                            }
                        } else {
                            //showWarnToast("Failed : " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

    @Override
    public void viewWallet(String response) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideBaseProgress();
                LoginBean loginBean = convertJsonToPojo(response);
                if (loginBean != null) {

                    if (loginBean.getStatus().equals("OK")) {
                        binding.textAmount.setText("€ " + loginBean.getValue().getWallet());
                    }
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
}
