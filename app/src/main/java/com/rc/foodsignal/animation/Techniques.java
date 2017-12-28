package com.rc.foodsignal.animation;

public enum Techniques {

    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    Shake(ShakeAnimator.class),
    Tada(TadaAnimator.class);

    private Class animatorClazz;

    private Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
