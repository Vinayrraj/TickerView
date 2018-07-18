# TickerView

[![](https://jitpack.io/v/Vinayrraj/TickerView.svg)](https://jitpack.io/#Vinayrraj/TickerView)

What is TickerView?
===============
TickerView is a simple Android UI component for displaying horizontally scrolling Views placed inside this TickerView. For reference you may see how [MoneyControl Tablet](https://play.google.com/store/apps/details?id=com.moneycontrol) (shows stock price in scrolling view at the bottom of screen) and Phone app shows the Stock prices. The TickerView scrolls child views with smooth animation.

Live Demo
---------------
Google Play Store: [https://play.google.com/store/apps/details?id=com.vinay.ticker](https://play.google.com/store/apps/details?id=com.vinay.ticker) 


GIF
---------------

![Wait for the GIF image to load](/media/image_gif.gif)


Getting started
---------------

Add it in your root `build.gradle` at the end of repositories:

```groovy

allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

```

Add the ticker dependency to your `build.gradle`.

```groovy

dependencies {
	implementation 'com.github.Vinayrraj:TickerView:v0.1-alpha'
}

```

Usage
-----

Define the `TickerView` in XML:

```xml

<com.vinay.ticker.lib.TickerView
        android:id="@+id/tickerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

Then in java code, add the views to the TickerView:

```java

// Get the TickerView object
final TickerView tickerView = findViewById(R.id.tickerView);

// Add multiple views to be shown in the ticker
for (int index = 0; index < 50; index++) {
	// childViews
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        tv.setText(String.valueOf(index + 1));
        tv.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        tv.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        tv.setPadding(10, 5, 10, 5);
        
	tickerView.addChildView(tv);
}

// Call the showTickers() to show them on the screen
tickerView.showTickers();

```



Documentation
-----

Usage Documentation: [https://vinayrraj.github.io/TickerView/](https://vinayrraj.github.io/TickerView/) , 

Javadoc for the library can be viewed at: [https://vinayrraj.github.io/TickerView/java-docs/](https://vinayrraj.github.io/TickerView/java-docs/)


How it Works?
-----

TickerView extends HorizontalScrollView, which contains a  LinearLayout. With the method setChildViews(List<View> childViews)" or "addChildView(View childView)", we add child views to be plotted in the scrolling TickerView.
Once Views are added to the TickerView, we set the left Margin for the First View and the right Margin for the Last View equalent to the width of the screen, so that the view statys on the screen for a certain time frame. 
Additionally, we have added a Marker View as the last view on the list, so that as soon as that view's Rect bounds (with width and height equal to the width and height of parent) intersects with the Rect object of the last Marker View. As soon as this contition is met, we reset the position of scroll and the auto scrolling starts back from the first View. 

Now to Automate the Scrolling process, we have created a Timer (which runs TimerTask) which schedule the movement/displacement given by the getDisplacement() method and the process continues till the view is detatched from windlow. 


Credits
-----

vinayrraj@gmail.com

[https://twitter.com/vinayrraj](https://twitter.com/vinayrraj)
