package com.nozomi.autohidescrollview.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nozomi.autohidescrollview.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 可以下拉刷新的scrollView
 * 
 * @author 6a209
 * 
 *         2011-11-20 下午9:15:47
 */
public class XScrollView extends ScrollView {

	private static final int PULL_TO_REFRESH_STATUS = 0x00;
	private static final int RELEASE_TO_REFRESH_STATUS = 0x01;
	private static final int REFRESHING_STATUS = 0x02;
	private static final int NORMAL_STATUS = 0x03;

	// mode head or foot
	private static final int HEAD_MODE = 0x00;
	private static final int FOOT_MODE = 0x01;
	private int mStatus = NORMAL_STATUS;
	private int mMode = HEAD_MODE;
	protected RelativeLayout mContentLy;
	private float mLastY = -1000;
	private FrameLayout mHeadViewLy;
	private FrameLayout mFootViewLy;
	private TextView mHeaderTimeView;

	private float mDowY;

	private final static int PULL_LOAD_MORE_DELTA = 50;

	private ILoadingLayout mHeadLoadingView;
	private ILoadingLayout mFootLoadingView;

	private boolean mIsAnimation;
	private int mDefautlTopMargin;
	private boolean mEnablePullRefresh = true;
	private boolean mEnablePullLoad = true;

	private final static int SCROLL_DURATION = 200;

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",
			Locale.CHINA);

	public interface OnScrollUpDownListener {
		public void onScrollUp(boolean isUp);
	}

	private OnScrollUpDownListener mScrollUpDown;
	private IXScrollViewListener mScrollViewListener;

	public XScrollView(Context context) {
		this(context, null);
	}

	public XScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		View view = LayoutInflater.from(context).inflate(R.layout.xscrollview,
				null, false);
		ViewGroup.LayoutParams params = generateLayoutParams(attrs);
		super.addView(view, params);

		mContentLy = (RelativeLayout) findViewById(R.id.content_ly);
		mHeadViewLy = (FrameLayout) findViewById(R.id.head_ly);
		mFootViewLy = (FrameLayout) findViewById(R.id.foot_ly);
		mHeadLoadingView = new HeadLoadingView(context);
		mHeaderTimeView = (TextView) ((View) mHeadLoadingView)
				.findViewById(R.id.xscrollview_header_time);

		mFootLoadingView = new FootLoadingView(context);
		mHeadViewLy.addView((View) mHeadLoadingView);
		mFootViewLy.addView((View) mFootLoadingView);
		mHeadLoadingView.normal();
		mFootLoadingView.normal();

		mHeadViewLy.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mDefautlTopMargin = -mHeadViewLy.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		setFadingEdgeLength(0);
		setRefreshTime();
	}

	public void addView(View child, ViewGroup.LayoutParams params) {
		mContentLy.addView(child, params);
	}

	public void setOnScrollUpDownListener(OnScrollUpDownListener listener) {
		mScrollUpDown = listener;
	}

	public void stopRefresh() {
		MaginAnimation maginAnim = new MaginAnimation(0,
				(int) mDefautlTopMargin, SCROLL_DURATION);
		maginAnim.startAnimation(mHeadViewLy);
		maginAnim.setOnAnimationOverListener(new OnAnimationOverListener() {
			@Override
			public void onOver() {
				updateStatus(NORMAL_STATUS, mHeadLoadingView);
			}
		});
		setRefreshTime();
	}

	public void stopLoadMore() {
		updateStatus(NORMAL_STATUS, mFootLoadingView);
	}

	public void setPullRefreshEnable(boolean enable) {
		if (enable) {
			mEnablePullRefresh = true;
			mHeadViewLy.setVisibility(View.VISIBLE);
		} else {
			mEnablePullRefresh = false;
			mHeadViewLy.setVisibility(View.INVISIBLE);
		}
	}

	public void setPullLoadEnable(boolean enable) {
		if (enable) {
			mEnablePullLoad = true;
			mFootViewLy.setVisibility(View.VISIBLE);
		} else {
			mEnablePullLoad = false;
			mFootViewLy.setVisibility(View.INVISIBLE);
			LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) mFootViewLy
					.getLayoutParams();
			lllp.height = 0;
			mFootViewLy.setLayoutParams(lllp);
		}
	}

	private void setRefreshTime() {
		mHeaderTimeView.setText(sdf.format(new Date()));
	}

	public void setToRefreshing() {
		MaginAnimation maginAnim = new MaginAnimation(getHeadViewTopMargin(),
				0, SCROLL_DURATION);
		maginAnim.startAnimation(mHeadViewLy);
		maginAnim.setOnAnimationOverListener(new OnAnimationOverListener() {
			@Override
			public void onOver() {
				updateStatus(REFRESHING_STATUS, mHeadLoadingView);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mIsAnimation) {
			return super.onTouchEvent(ev);
		}
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getY();
			mDowY = ev.getY();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (null != mScrollUpDown) {
				if (ev.getY() - mDowY >= 20) {
					mScrollUpDown.onScrollUp(true);
				} else if (ev.getY() - mDowY <= -30) {
					mScrollUpDown.onScrollUp(false);
				}
			}
			release(mStatus);
			mLastY = -1000;
			break;
		case MotionEvent.ACTION_MOVE:
			// 判断是不是首次移动
			if (-1000 == mLastY) {
				mLastY = ev.getY();
				mDowY = ev.getY();
				return super.onTouchEvent(ev);
			}
			final float lastY = mLastY;
			float nowY = ev.getY();
			int deltaY = (int) (lastY - nowY);
			mLastY = nowY;
			if (deltaY < 0) {
				// down
				if (getScrollY() == 0 && mStatus != REFRESHING_STATUS) {

					updateMode(HEAD_MODE);
					updateHeadMargin(deltaY / 2);

					if (getHeadViewTopMargin() >= 0) {
						updateStatus(RELEASE_TO_REFRESH_STATUS,
								mHeadLoadingView);
					} else {
						updateStatus(PULL_TO_REFRESH_STATUS, mHeadLoadingView);
					}

					return super.onTouchEvent(ev);
				}

				if (mStatus != REFRESHING_STATUS && mStatus != NORMAL_STATUS) {
					// foot
					updateMode(FOOT_MODE);
					updateFootPadding(deltaY / 2);

					if (getPaddingBottom() > 0
							&& getPaddingBottom() < PULL_LOAD_MORE_DELTA) {

						updateStatus(PULL_TO_REFRESH_STATUS, mFootLoadingView);

					} else if (getPaddingBottom() >= PULL_LOAD_MORE_DELTA) {
						updateStatus(RELEASE_TO_REFRESH_STATUS,
								mFootLoadingView);
					} else if (getPaddingBottom() == 0) {
						updateStatus(NORMAL_STATUS, mFootLoadingView);
					}

				}

			} else {
				// up
				if (mMode == HEAD_MODE && mStatus != REFRESHING_STATUS
						&& mStatus != NORMAL_STATUS) {
					// head

					updateMode(HEAD_MODE);
					updateHeadMargin(deltaY / 2);
					if (getHeadViewTopMargin() > mDefautlTopMargin
							&& getHeadViewTopMargin() < 0) {
						if (mEnablePullRefresh) {
							updateStatus(PULL_TO_REFRESH_STATUS,
									mHeadLoadingView);
						}
					} else if (getHeadViewTopMargin() == mDefautlTopMargin) {
						updateStatus(NORMAL_STATUS, mHeadLoadingView);
					}
					return super.onTouchEvent(ev);
				}
				if (getScrollY() + getHeight() >= mContentLy.getHeight()
						+ mFootViewLy.getHeight()
						&& mStatus != REFRESHING_STATUS) {
					// foot

					updateMode(FOOT_MODE);
					updateFootPadding(deltaY / 2);
					if (getPaddingBottom() >= PULL_LOAD_MORE_DELTA) {
						updateStatus(RELEASE_TO_REFRESH_STATUS,
								mFootLoadingView);
					} else {
						updateStatus(PULL_TO_REFRESH_STATUS, mFootLoadingView);
					}
				}
			}

			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void updateHeadMargin(int deltaY) {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) mHeadViewLy
				.getLayoutParams();
		param.topMargin -= deltaY;
		if (param.topMargin <= mDefautlTopMargin) {
			param.topMargin = (int) mDefautlTopMargin;
		}

		mHeadViewLy.setLayoutParams(param);
	}

	private void updateFootPadding(int deltaY) {
		int bottomPadding = getPaddingBottom() + deltaY;
		if (bottomPadding <= 0) {
			bottomPadding = 0;
		}
		setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
				bottomPadding);
	}

	private void updateStatus(int status, ILoadingLayout layout) {
		if (mStatus == status) {
			return;
		}
		mStatus = status;
		switch (status) {
		case PULL_TO_REFRESH_STATUS:
			layout.pullToRefresh();
			break;
		case RELEASE_TO_REFRESH_STATUS:
			layout.releaseToRefresh();
			break;
		case REFRESHING_STATUS:
			layout.refreshing();
			break;
		case NORMAL_STATUS:
			layout.normal();
			break;
		default:
			break;
		}
	}

	private void updateMode(int mode) {
		mMode = mode;
	}

	private int getHeadViewTopMargin() {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) mHeadViewLy
				.getLayoutParams();
		return param.topMargin;
	}

	private void release(int status) {
		if (HEAD_MODE == mMode) {
			headReleas();
		} else if (FOOT_MODE == mMode) {
			footReleas();
		}

	}

	private void headReleas() {
		int toMagin;
		if (RELEASE_TO_REFRESH_STATUS == mStatus) {
			toMagin = 0;
		} else if (PULL_TO_REFRESH_STATUS == mStatus) {
			toMagin = (int) mDefautlTopMargin;
		} else {
			return;
		}
		MaginAnimation maginAnim = new MaginAnimation(getHeadViewTopMargin(),
				toMagin, SCROLL_DURATION);
		maginAnim.startAnimation(mHeadViewLy);
		maginAnim.setOnAnimationOverListener(new OnAnimationOverListener() {
			@Override
			public void onOver() {
				if (mStatus == RELEASE_TO_REFRESH_STATUS) {
					if (null != mScrollViewListener) {
						updateStatus(REFRESHING_STATUS, mHeadLoadingView);
						if (mEnablePullRefresh) {
							mScrollViewListener.onRefresh();
						} else {
							stopRefresh();
						}
					}
				} else if (mStatus == PULL_TO_REFRESH_STATUS) {
					updateStatus(NORMAL_STATUS, mHeadLoadingView);
				}
			}
		});
	}

	private void footReleas() {
		if (getPaddingBottom() > PULL_LOAD_MORE_DELTA) {
			if (null != mScrollViewListener) {
				updateStatus(REFRESHING_STATUS, mFootLoadingView);
				if (mEnablePullLoad) {
					mScrollViewListener.onLoadMore();
				} else {
					stopLoadMore();
				}
			}
		} else {
			updateStatus(NORMAL_STATUS, mFootLoadingView);
		}
		updateFootPadding(-getPaddingBottom());
	}

	public interface IXScrollViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	public void setXScrollViewListener(IXScrollViewListener l) {
		mScrollViewListener = l;
	}
}
