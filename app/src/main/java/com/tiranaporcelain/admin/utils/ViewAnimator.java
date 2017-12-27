package com.tiranaporcelain.admin.utils;

import android.animation.Animator;
import android.view.View;

/**
 * Created by mphj on 10/15/2017.
 */

public class ViewAnimator {
    public static void showWithFadeAndTranslationY(View view){
        view.setTranslationY(view.getHeight() * -1);
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .setDuration(1000)
                .alpha(1)
                .start();
    }

    public static void hideWithFadeAndTranslationY(final View view) {
        view.setTranslationY(0);
        view.setAlpha(1);
        view.animate()
                .translationY(view.getHeight())
                .setDuration(1000)
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                })
                .start();
    }

    public static void scale(View view, float scale, int duration) {
        view.animate()
                .setDuration(duration)
                .scaleY(scale)
                .scaleX(scale)
                .start();
    }


    public static void defaultScale(View view, float scale){
        scale(view, scale, 300);
    }
}
