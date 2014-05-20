package com.nozomi.autohidescrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class BounceGridView extends GridView {

	public BounceGridView(Context context) {
		super(context);

	}

	public BounceGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public BounceGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}