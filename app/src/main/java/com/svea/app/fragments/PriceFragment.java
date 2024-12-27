package com.svea.app.fragments;

import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.svea.app.R;
import com.svea.app.base.BaseFragment;
import com.svea.app.base.SimpleRecyclerViewAdapter;
import com.svea.app.beans.WatchListBean;
import com.svea.app.databinding.FragmentHomeBinding;
import com.svea.app.databinding.FragmentPriceBinding;
import com.svea.app.databinding.ItemWatchlistBinding;
import com.svea.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PriceFragment extends BaseFragment<FragmentPriceBinding> {

    private SimpleRecyclerViewAdapter<WatchListBean, ItemWatchlistBinding> mOptionsAdapter;

    public PriceFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_price;
    }

    private void initView() {

        List<WatchListBean> list = new ArrayList<>();
        for (int i = 0; i < Constants.heading.length; i++) {
            WatchListBean bean = new WatchListBean(Constants.heading[i],Constants.sub_heading[i],Constants.price[i],
                    Constants.icons[i]);
            list.add(bean);
        }
        mOptionsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_prices, BR.bean, (v, settingBean) -> {


        });
        binding.recyclerWatchList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWatchList.setAdapter(mOptionsAdapter);
        mOptionsAdapter.setList(list);
    }
}
