package com.nozomi.autohidescrollview.activity;

import java.util.ArrayList;
import java.util.Random;

import com.nozomi.autohidescrollview.R;
import com.nozomi.autohidescrollview.view.BounceListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ScrollViewActivity extends Activity {

	private ScrollView scrollView = null;
	private ArrayList<String> nameArray = new ArrayList<String>();
	private ItemAdapter itemAdapter = null;
	private Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_view_activity);

		initView();

		handler.sendMessageDelayed(handler.obtainMessage(1), 1000);
	}

	private void initView() {

		scrollView = (ScrollView) findViewById(R.id.scroll);

		BounceListView itemListView = (BounceListView) findViewById(R.id.item_list);
		itemAdapter = new ItemAdapter(this);
		itemListView.setAdapter(itemAdapter);

		itemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(ScrollViewActivity.this,
						nameArray.get(position), Toast.LENGTH_SHORT).show();

			}
		});

	}

	private class ItemAdapter extends BaseAdapter {

		private Context context;

		public ItemAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return nameArray.size();
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
						R.layout.item, null, false);

				holder.picView = (ImageView) convertView.findViewById(R.id.pic);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String name = nameArray.get(position);
			holder.picView.setImageResource(R.drawable.ic_launcher);
			holder.nameView.setText(name);

			return convertView;
		}

		private class ViewHolder {
			ImageView picView;
			TextView nameView;

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
				scrollView.smoothScrollTo(0, 0);
			}
		}
	};
}
