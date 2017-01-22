package com.yzdsmart.Dingdingwen.bean;

/**
 * Created by YZD on 2017/1/22.
 */

public class SignProcessStep {
    private String stepName;
    private Boolean isSigned;//该点是否已经签到
    private Boolean isLastStep;//是否是最后一个签到点

    public SignProcessStep() {
    }

    public SignProcessStep(String stepName, Boolean isSigned, Boolean isLastStep) {
        this.stepName = stepName;
        this.isSigned = isSigned;
        this.isLastStep = isLastStep;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Boolean getSigned() {
        return isSigned;
    }

    public void setSigned(Boolean signed) {
        isSigned = signed;
    }

    public Boolean getLastStep() {
        return isLastStep;
    }

    public void setLastStep(Boolean lastStep) {
        isLastStep = lastStep;
    }

    @Override
    public String toString() {
        return "{" +
                "stepName:'" + stepName + '\'' +
                ", isSigned:" + isSigned +
                ", isLastStep:" + isLastStep +
                '}';
    }
}
