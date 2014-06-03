package com.nozomi.autohidescrollview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
	private boolean isAnimation = true;

	private Animation topicBarShowAnimation = null;
	private Animation topicBarHideAnimation = null;
	private Animation footBarShowAnimation = null;
	private Animation footBarHideAnimation = null;

	private int lastT = 0;

	public AutoHideScrollView(Context context) {
		super(context);

	}

	public AutoHideScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoHideScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setHeaderAndFooter(final View autoHideHeaderView,
			final View autoHideFooterView, boolean isAnimation) {

		setOverScrollMode(OVER_SCROLL_NEVER);

		this.autoHideHeaderView = autoHideHeaderView;
		this.autoHideFooterView = autoHideFooterView;
		this.isAnimation = isAnimation;

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
		if (isAnimation) {
			if (t > oldt
					&& (autoHideHeaderView == null || t > autoHideHeaderView
							.getHeight())) {
				hideHeaderAndFooter();
			} else if (oldt - t > 3) {
				showHeaderAndFooter();
			}
		} else {
			if (lastT != t) {
				if (autoHideHeaderView != null) {
					ViewGroup.LayoutParams vglp = (ViewGroup.LayoutParams) autoHideHeaderView
							.getLayoutParams();
					if (vglp instanceof RelativeLayout.LayoutParams) {
						int topMargin = ((RelativeLayout.LayoutParams) vglp).topMargin;
						topMargin -= t - lastT;
						if (topMargin < -autoHideHeaderView.getHeight()) {
							topMargin = -autoHideHeaderView.getHeight();
						} else if (topMargin > 0) {
							topMargin = 0;
						}
						((RelativeLayout.LayoutParams) vglp).topMargin = topMargin;
						autoHideHeaderView.setLayoutParams(vglp);
					}
				}

				if (autoHideFooterView != null) {
					ViewGroup.LayoutParams vglp = (ViewGroup.LayoutParams) autoHideFooterView
							.getLayoutParams();
					if (vglp instanceof RelativeLayout.LayoutParams) {
						int bottomMargin = ((RelativeLayout.LayoutParams) vglp).bottomMargin;
						bottomMargin -= t - lastT;
						if (bottomMargin < -autoHideFooterView.getHeight()) {
							bottomMargin = -autoHideFooterView.getHeight();
						} else if (bottomMargin > 0) {
							bottomMargin = 0;
						}
						((RelativeLayout.LayoutParams) vglp).bottomMargin = bottomMargin;
						autoHideFooterView.setLayoutParams(vglp);
					}
				}

				lastT = t;
			}
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
