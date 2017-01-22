package com.yzdsmart.Dingdingwen.time_keeper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.SignProcessStep;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2017/1/22.
 */

public class SignProcessAdapter extends UltimateViewAdapter<SignProcessAdapter.ViewHolder> {
    private Context context;
    private List<SignProcessStep> processSteps;

    public SignProcessAdapter(Context context) {
        this.context = context;
        processSteps = new ArrayList<SignProcessStep>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<SignProcessStep> list) {
        if (null != processSteps) {
            processSteps.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != processSteps && processSteps.size() > 0) {
            processSteps.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sign_process_step_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return processSteps.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SignProcessStep signProcessStep = processSteps.get(position);
        holder.setSignProcessStepImg(signProcessStep.getSigned(), signProcessStep.getLastStep());
        holder.setSignProcessStepTxt(signProcessStep.getStepName(), signProcessStep.getSigned(), signProcessStep.getLastStep());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.sign_process_step_img)
        ImageView signProcessStepIV;
        @Nullable
        @BindView(R.id.sign_process_step_txt)
        TextView signProcessStepTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setSignProcessStepImg(Boolean isSigned, Boolean isLastStep) {
            if (isLastStep) {
                if (isSigned) {
                    Glide.with(context).load(R.mipmap.sign_process_without_arrow_yellow).asBitmap().into(signProcessStepIV);
                } else {
                    Glide.with(context).load(R.mipmap.sign_process_without_arrow_grey).asBitmap().into(signProcessStepIV);
                }
            } else {
                if (isSigned) {
                    Glide.with(context).load(R.mipmap.sign_process_with_arrow_yellow).asBitmap().into(signProcessStepIV);
                } else {
                    Glide.with(context).load(R.mipmap.sign_process_with_arrow_grey).asBitmap().into(signProcessStepIV);
                }
            }
        }

        public void setSignProcessStepTxt(String stepName, Boolean isSigned, Boolean isLastStep) {
            if (isLastStep) {
                signProcessStepTV.setText("完成");
            } else {
                signProcessStepTV.setText(stepName);
            }
        }
    }
}
