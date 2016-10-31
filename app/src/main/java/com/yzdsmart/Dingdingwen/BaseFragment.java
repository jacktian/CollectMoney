package com.yzdsmart.Dingdingwen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YZD on 2016/7/15.
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        //绑定ButterKnife
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public abstract int getLayoutResource();

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
