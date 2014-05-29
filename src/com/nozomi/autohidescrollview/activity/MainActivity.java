package com.nozomi.autohidescrollview.activity;

import com.nozomi.autohidescrollview.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		initView();

	}

	private void initView() {
		Button bounceListView = (Button) findViewById(R.id.bounce_list_view);
		bounceListView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						BounceListViewActivity.class);
				startActivity(intent);
			}
		});
		
		Button bounceGridView = (Button) findViewById(R.id.bounce_grid_view);
		bounceGridView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						BounceGridViewActivity.class);
				startActivity(intent);
			}
		});

		Button autoHideScrollViewView = (Button) findViewById(R.id.auto_hide_scroll_view);
		autoHideScrollViewView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AutoHideScrollViewActivity.class);
				intent.putExtra("is_animation", false);
				startActivity(intent);
			}
		});
		
		
		Button autoHideScrollViewAniamtionView = (Button) findViewById(R.id.auto_hide_scroll_view_animation);
		autoHideScrollViewAniamtionView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AutoHideScrollViewActivity.class);
				intent.putExtra("is_animation", true);
				startActivity(intent);
			}
		});

		Button XScrollViewView = (Button) findViewById(R.id.xscroll_view);
		XScrollViewView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						XScrollViewActivity.class);
				startActivity(intent);
			}
		});

		Button AutoHideXScrollViewView = (Button) findViewById(R.id.auto_hide_xscroll_view);
		AutoHideXScrollViewView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AutoHideXScrollViewActivity.class);
				intent.putExtra("is_animation", false);
				startActivity(intent);
			}
		});
		
		
		Button AutoHideXScrollViewAnimationView = (Button) findViewById(R.id.auto_hide_xscroll_view_animation);
		AutoHideXScrollViewAnimationView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AutoHideXScrollViewActivity.class);
				intent.putExtra("is_animation", true);
				startActivity(intent);
			}
		});

	}

}
