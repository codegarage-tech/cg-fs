package com.rc.foodsignal.animation;

import android.animation.ObjectAnimator;
import android.view.View;

public class FadeOutRightAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target, "translationX", 0, target.getWidth() / 4)
        );
    }
}
