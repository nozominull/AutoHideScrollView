AutoHideScrollView
==================

ScrollView supports scroll hide/show header&amp;footer,pull refresh and pull load more.


edit from [PinterestLikeAdapterView][1] and [PullRefreshScrollView][2],thanks to them.

Screen Shot
----------------
This is a screen shot of sample activity.

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/1.png)

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/2.png)

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/3.png)

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/4.png)

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/5.png)

![image](https://raw.github.com/xuyangbill/AutoHideScrollView/master/screenshots/6.png)

How to use
-------------

*To run Sample App.*

  1. clone project.

  2. run on your android phone.

*To use AutoHideScrollView.*

  1. clone project.

  2. Copy the files you need(all if u r lazy…) in com.nozomi.autohidescrollview.view.

  3. copy res files.

*Simple Example.*

```xml
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <com.nozomi.autohidescrollview.view.AutoHideXScrollView
        android:id="@+id/scroll"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/header" >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical" >
            
        </LinearLayout>
    </com.nozomi.autohidescrollview.view.AutoHideXScrollView>

    <View
        android:id="@+id/header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <View
        android:id="@+id/footer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</RelativeLayout>
```

```java
AutoHideXScrollView scrollView = (AutoHideXScrollView) findViewById(R.id.scroll);
scrollView.setHeaderAndFooter(headerView, footerView);
scrollView.setPullRefreshEnable(true);//default true;set false if you do not want to pull refresh
scrollView.setPullLoadEnable(true);//default true;set false if you do not want to pull load more	
scrollView.setXScrollViewListener(new IXScrollViewListener() {

	@Override
	public void onRefresh() {
				
	}

	@Override
	public void onLoadMore() {
	
	}
});
```
plz check code for more details.


[1]: https://github.com/GDG-Korea/PinterestLikeAdapterView
[2]: https://github.com/6a209/PullRefreshScrollView