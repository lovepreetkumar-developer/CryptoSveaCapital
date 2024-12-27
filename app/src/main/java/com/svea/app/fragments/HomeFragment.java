package com.svea.app.fragments;

import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.svea.app.R;
import com.svea.app.adapters.ViewPagerAdapter;
import com.svea.app.adapters.ViewPagerAdapterHome;
import com.svea.app.base.BaseFragment;
import com.svea.app.base.SimpleRecyclerViewAdapter;
import com.svea.app.beans.UserData;
import com.svea.app.beans.WatchListBean;
import com.svea.app.beans.server_beans.LoginBean;
import com.svea.app.databinding.FragmentHomeBinding;
import com.svea.app.databinding.ItemWatchlistBinding;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.server.Apis;
import com.svea.app.utils.Constants;
import com.svea.app.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements ResponseInterface {

    private SimpleRecyclerViewAdapter<WatchListBean, ItemWatchlistBinding> mOptionsAdapter;
    private ViewPagerAdapterHome mViewPagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    private void initView() {

        if (PrefUtils.getInstance().getUserId() != null) {
            getData();
        }

        List<WatchListBean> list = new ArrayList<>();
        for (int i = 0; i < Constants.heading.length; i++) {
            WatchListBean bean = new WatchListBean(Constants.heading[i], Constants.sub_heading[i], Constants.price[i],
                    Constants.icons[i]);
            list.add(bean);
        }
        mOptionsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_watchlist, BR.bean, (v, settingBean) -> {


        });
        binding.recyclerWatchList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWatchList.setAdapter(mOptionsAdapter);
        mOptionsAdapter.setList(list);

        mViewPagerAdapter = new ViewPagerAdapterHome(getChildFragmentManager());
        binding.viewPager.setAdapter(mViewPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);

    }

    @Override
    public void viewWallet(String response) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideBaseProgress();
            LoginBean loginBean = convertJsonToPojo(response);
            if (loginBean != null) {

                if (loginBean.getStatus().equals("OK")) {
                    binding.textWalletAmount.setText("€ " + loginBean.getValue().getWallet());
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

    private void getData() {
        //showBaseProgress();
        Apis apis = new Apis(getActivity(), this);
        apis.yks_viewwallet();

/*        try {
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
                                        binding.textWalletAmount.setText("€ " + data.getWallet_amount());
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
}
