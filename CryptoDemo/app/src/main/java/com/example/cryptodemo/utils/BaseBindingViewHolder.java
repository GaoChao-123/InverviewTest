package com.example.cryptodemo.utils;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public class BaseBindingViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private Binding mBinding;

    BaseBindingViewHolder(View view) {
        super(view);
    }

    public Binding getBinding() {
        return mBinding;
    }

    void setBinding(Binding binding) {
        mBinding = binding;
    }
}
