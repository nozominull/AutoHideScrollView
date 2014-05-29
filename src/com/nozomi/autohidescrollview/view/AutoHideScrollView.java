package com.nozomi.autohidescrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class AutoHideScrollView extends ScrollView {

	private View autoHideHeaderView = null;
	private View autoHideFooterView = null;

	private Animation topicBarShowAnimation = null;
	private Animation topicBarHideAnimation = null;
	private Animation footBarShowAnimation = null;
	private Animation footBarHideAnimation = null;

	public AutoHideScrollView(Context context) {
		super(context);

	}

	public AutoHideScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void setHeaderAndFooter(final View autoHideHeaderView,
			final View autoHideFooterView) {

		this.autoHideHeaderView = autoHideHeaderView;
		this.autoHideFooterView = autoHideFooterView;

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						ViewGroup.LayoutParams vglp = (ViewGroup.LayoutParams) getLayoutParams();
						if (vglp instanceof RelativeLayout.LayoutParams) {
							View view = getChildAt(0);
							view.setPadding(0, getTop(), 0, 0);
							((RelativeLayout.LayoutParams) vglp).addRule(
									RelativeLayout.BELOW, 0);
							((RelativeLayout.LayoutParams) vglp).topMargin = 0;
							setLayoutParams(vglp);
						}
						
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

					}
				});
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (t > oldt
				&& (autoHideHeaderView == null || t > autoHideHeaderView
						.getHeight())) {
			hideHeaderAndFooter();
		} else if (oldt - t > 3) {
			showHeaderAndFooter();
		}
	}

	public void showHeaderAndFooter() {

		if (autoHideHeaderView != null
				&& autoHideHeaderView.getVisibility() == View.INVISIBLE) {
			autoHideHeaderView.setVisibility(View.VISIBLE);
			if (topicBarShowAnimation == null) {
				if (autoHideHeaderView.getHeight() != 0) {
					topicBarShowAnimation = new TranslateAnimation(0, 0,
							-autoHideHeaderView.getHeight(), 0);
					topicBarShowAnimation.setDuration(500);
					autoHideHeaderView.startAnimation(topicBarShowAnimation);
				}
			} else {
				autoHideHeaderView.startAnimation(topicBarShowAnimation);
			}
		}

		if (autoHideFooterView != null
				&& autoHideFooterView.getVisibility() == View.INVISIBLE) {
			autoHideFooterView.setVisibility(View.VISIBLE);
			if (footBarShowAnimation == null) {
				if (autoHideFooterView.getHeight() != 0) {
					footBarShowAnimation = new TranslateAnimation(0, 0,
							autoHideFooterView.getHeight(), 0);
					footBarShowAnimation.setDuration(500);
					autoHideFooterView.startAnimation(footBarShowAnimation);
				}
			} else {
				autoHideFooterView.startAnimation(footBarShowAnimation);
			}
		}
	}

	public void hideHeaderAndFooter() {
		if (autoHideHeaderView != null
				&& autoHideHeaderView.getVisibility() == View.VISIBLE) {
			autoHideHeaderView.setVisibility(View.INVISIBLE);

			if (topicBarHideAnimation == null) {
				if (autoHideHeaderView.getHeight() != 0) {
					topicBarHideAnimation = new TranslateAnimation(0, 0, 0,
							-autoHideHeaderView.getHeight());
					topicBarHideAnimation.setDuration(500);
					autoHideHeaderView.startAnimation(topicBarHideAnimation);
				}
			} else {
				autoHideHeaderView.startAnimation(topicBarHideAnimation);
			}
		}
		if (autoHideFooterView != null
				&& autoHideFooterView.getVisibility() == View.VISIBLE) {
			autoHideFooterView.setVisibility(View.INVISIBLE);
			if (footBarHideAnimation == null) {
				if (autoHideHeaderView.getHeight() != 0) {
					footBarHideAnimation = new TranslateAnimation(0, 0, 0,
							autoHideFooterView.getHeight());
					footBarHideAnimation.setDuration(500);
					autoHideFooterView.startAnimation(footBarHideAnimation);
				}
			} else {
				autoHideFooterView.startAnimation(footBarHideAnimation);
			}
		}
	}

}
