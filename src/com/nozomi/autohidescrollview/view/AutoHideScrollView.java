package com.nozomi.autohidescrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

	private boolean isInit = false;
	private int scrollY = 0;

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
						if (!isInit) {
							isInit = true;
							ViewGroup.LayoutParams vglp = (ViewGroup.LayoutParams) getLayoutParams();
							if (vglp instanceof RelativeLayout.LayoutParams) {
								View view = getChildAt(0);
								view.setPadding(0, getTop(), 0, 0);
								((RelativeLayout.LayoutParams) vglp).addRule(
										RelativeLayout.BELOW, 0);
								((RelativeLayout.LayoutParams) vglp).topMargin = 0;
							}
							setLayoutParams(vglp);

							setOnTouchListener(new OnTouchListener() {

								@Override
								public boolean onTouch(View v, MotionEvent event) {
									int y = (int) event.getY();
									switch (event.getAction()) {
									case MotionEvent.ACTION_DOWN:
										scrollY = y;
										break;
									case MotionEvent.ACTION_MOVE:
										if (scrollY == -1) {
											scrollY = y;
										} else if (y > scrollY + 3) {
											showHeaderAndFooter();
										} else if (y + 3 < scrollY) {
											hideHeaderAndFooter();
										}
										scrollY = y;
										break;
									case MotionEvent.ACTION_UP:
										scrollY = -1;
										break;
									}
									return false;
								}
							});

						}
					}
				});
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
			if (topicBarHideAnimation == null) {
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
