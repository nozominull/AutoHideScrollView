package com.nozomi.autohidescrollview.activity;

import java.util.ArrayList;
import java.util.Random;

import com.nozomi.autohidescrollview.R;
import com.nozomi.autohidescrollview.view.AutoHideXScrollView;
import com.nozomi.autohidescrollview.view.BounceListView;
import com.nozomi.autohidescrollview.view.XScrollView.IXScrollViewListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AutoHideXScrollViewActivity extends Activity {

	private AutoHideXScrollView scrollView = null;
	private ArrayList<String> nameArray = new ArrayList<String>();
	private ItemAdapter itemAdapter = null;
	private Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_hide_xscroll_view_activity);

		initView();

		handler.sendMessageDelayed(handler.obtainMessage(1), 1000);
	}

	private void initView() {
		TextView headerView = (TextView) findViewById(R.id.header);
		TextView footerView = (TextView) findViewById(R.id.footer);
		scrollView = (AutoHideXScrollView) findViewById(R.id.scroll);
		scrollView.setHeaderAndFooter(headerView, footerView);
		scrollView.setXScrollViewListener(new IXScrollViewListener() {

			@Override
			public void onRefresh() {
				handler.sendMessageDelayed(handler.obtainMessage(1), 1000);
			}

			@Override
			public void onLoadMore() {
				handler.sendMessageDelayed(handler.obtainMessage(2), 1000);
			}
		});

		BounceListView itemListView = (BounceListView) findViewById(R.id.item_list);
		itemAdapter = new ItemAdapter(this);
		itemListView.setAdapter(itemAdapter);
	}

	private class ItemAdapter extends BaseAdapter {

		private Context context;

		public ItemAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return (nameArray.size() + 2) / 3;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.three_items, null, false);
				holder.leftView = (RelativeLayout) convertView
						.findViewById(R.id.left);
				View leftView = LayoutInflater.from(context).inflate(
						R.layout.item, null, false);
				holder.leftPicView = (ImageView) leftView
						.findViewById(R.id.pic);
				holder.leftNameView = (TextView) leftView
						.findViewById(R.id.name);
				holder.leftView.addView(leftView,
						new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT));

				holder.middleView = (RelativeLayout) convertView
						.findViewById(R.id.middle);
				View middleView = LayoutInflater.from(context).inflate(
						R.layout.item, null, false);
				holder.middlePicView = (ImageView) middleView
						.findViewById(R.id.pic);
				holder.middleNameView = (TextView) middleView
						.findViewById(R.id.name);
				holder.middleView.addView(middleView,
						new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT));

				holder.rightView = (RelativeLayout) convertView
						.findViewById(R.id.right);
				View rightView = LayoutInflater.from(context).inflate(
						R.layout.item, null, false);
				holder.rightPicView = (ImageView) rightView
						.findViewById(R.id.pic);
				holder.rightNameView = (TextView) rightView
						.findViewById(R.id.name);
				holder.rightView.addView(rightView,
						new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String leftName = nameArray.get(3 * position);
			holder.leftPicView.setImageResource(R.drawable.ic_launcher);
			holder.leftNameView.setText(leftName);
			holder.leftView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(context, leftName, Toast.LENGTH_SHORT)
							.show();
				}
			});

			if (3 * position + 1 < nameArray.size()) {
				holder.middleView.setVisibility(View.VISIBLE);
				final String middleName = nameArray.get(3 * position + 1);
				holder.middlePicView.setImageResource(R.drawable.ic_launcher);
				holder.middleNameView.setText(middleName);
				holder.middleView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(context, middleName, Toast.LENGTH_SHORT)
								.show();
					}
				});
			} else {
				holder.middleView.setVisibility(View.INVISIBLE);
			}

			if (3 * position + 2 < nameArray.size()) {
				holder.rightView.setVisibility(View.VISIBLE);
				final String rightName = nameArray.get(3 * position + 2);
				holder.rightPicView.setImageResource(R.drawable.ic_launcher);
				holder.rightNameView.setText(rightName);
				holder.rightView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(context, rightName, Toast.LENGTH_SHORT)
								.show();

					}
				});
			} else {
				holder.rightView.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

		private class ViewHolder {
			RelativeLayout leftView;
			ImageView leftPicView;
			TextView leftNameView;
			RelativeLayout middleView;
			ImageView middlePicView;
			TextView middleNameView;
			RelativeLayout rightView;
			ImageView rightPicView;
			TextView rightNameView;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				nameArray.clear();
				for (int i = 0; i < 20; i++) {
					nameArray.add("name" + random.nextInt());
				}
				itemAdapter.notifyDataSetChanged();
				scrollView.stopRefresh();
				scrollView.smoothScrollTo(0, 0);
			} else if (msg.what == 2) {
				for (int i = 0; i < 20; i++) {
					nameArray.add("name" + random.nextInt());
				}
				itemAdapter.notifyDataSetChanged();
				scrollView.stopLoadMore();
			}
		}
	};
}
