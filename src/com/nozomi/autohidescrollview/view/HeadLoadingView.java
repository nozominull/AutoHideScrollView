package com.nozomi.autohidescrollview.view;

import com.nozomi.autohidescrollview.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HeadLoadingView extends LinearLayout implements ILoadingLayout {

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private View mLeftProgress;
	private ImageView mLeftImage;
	private TextView mTitle;
	private boolean mImageIsUp;

	private final int ROTATE_ANIM_DURATION = 180;

	public HeadLoadingView(Context context) {
		this(context, null);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		LayoutInflater.from(context).inflate(R.layout.xscrollview_header, this);
		mLeftProgress = (ProgressBar) findViewById(R.id.xscrollview_header_progressbar);
		mLeftImage = (ImageView) findViewById(R.id.xscrollview_header_arrow);
		mTitle = (TextView) findViewById(R.id.xscrollview_header_hint_textview);
	}

	public HeadLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void pullToRefresh() {
		if (mImageIsUp) {
			mLeftImage.startAnimation(mRotateDownAnim);
			mImageIsUp = false;
		}
		mTitle.setText(getResources().getString(
				R.string.xscrollview_header_hint_normal));
	}

	@Override
	public void releaseToRefresh() {
		mLeftImage.startAnimation(mRotateUpAnim);
		mImageIsUp = true;
		mTitle.setText(getResources().getString(
				R.string.xscrollview_header_hint_ready));
	}

	@Override
	public void refreshing() {
		mImageIsUp = false;
		mLeftProgress.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(
				R.string.xscrollview_header_hint_loading));
		mLeftImage.clearAnimation();
		mLeftImage.setVisibility(View.INVISIBLE);
	}

	@Override
	public void normal() {
		mImageIsUp = false;
		mLeftImage.setVisibility(View.VISIBLE);
		mLeftProgress.setVisibility(View.GONE);
		mTitle.setText(getResources().getString(
				R.string.xscrollview_header_hint_normal));
	}

}