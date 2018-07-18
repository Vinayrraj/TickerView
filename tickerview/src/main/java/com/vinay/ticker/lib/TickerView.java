package com.vinay.ticker.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The {@code TickerView} class is a complex view which contains a {@code LinearLayout} which may contain any number of {@code View}s passed to it,
 * to be shown in horizontal layout.
 * <p>
 * These child {@code View}s scroll horizontally in the main view holder, from left to right.
 * The speed of the view scrolling can be controlled by setting up the {@code displacement} value of views. Also can be controlled by user by finger gesture / sling motion.
 *
 * @see HorizontalScrollView
 */
public class TickerView extends HorizontalScrollView {

    private int displacement = 1;
    private int scrollPos = 0;
    private Timer scrollTimer = null;
    private TimerTask scrollerSchedule;
    private TextView lastTicker;
    private List<View> childViews = null;
    private LinearLayout linearLayout;


    public TickerView(Context context) {
        super(context);
        init(context, null);
    }

    public TickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public TickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null);
    }

    /**
     * If the views are added to the container view, the tickers start showing up. This method calls {@code showTickers()}
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showTickers();
    }

    /**
     * If the views are added to the container view, this method, removed all scheduled {@code TimerTask}s by calling {@code destroyAllScheduledTasks()}
     */
    @Override
    protected void onDetachedFromWindow() {
        destroyAllScheduledTasks();
        super.onDetachedFromWindow();
    }


    /**
     * This method initialized the View container by adding a horizontal {@code LinearLayout} onside the root view.
     *
     * @param context
     * @param attributeSet
     */
    private void init(Context context, AttributeSet attributeSet) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        setForegroundGravity(Gravity.CENTER_VERTICAL);
        setHorizontalScrollBarEnabled(false);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.background_dark));
        setDisplacement(displacement);
    }


    /**
     * Use this method to get the speed of sticker movement, displacement speed of ticker views.
     *
     * @return the speed of view's rate of displacement
     */
    public int getDisplacement() {
        return displacement;
    }

    /**
     * This method is used to set the variable speed of displacement of auto-scrolling of views.
     *
     * @param displacement value by which the auto-scrolling displacement occurs
     */
    public void setDisplacement(int displacement) {
        this.displacement = (int) Math.ceil((displacement) * 5.0 / 100.0);
    }


    /**
     * Saves the views collection to be plotted in the ticker view.
     *
     * @param childViews {@code List<View>} which contains all the views to be added into the {@code TickerView}
     */
    public void setChildViews(List<View> childViews) {
        this.childViews = childViews;
    }

    /**
     * User may individually add views to be shown into the {@code TickerView}
     *
     * @param childView {@code View} to be added as child to the {@code TickerView}.
     */
    public void addChildView(View childView) {
        if (childViews == null) {
            childViews = new ArrayList<>();
        }
        this.childViews.add(childView);
    }

    /**
     * This method to show the child views added by {@code addChildView()} or {@code setChildViews(List<View> childViews)} methods.
     * Method Details:
     * 1. This method first removes any older child views already shown inside the {@code TickerView}
     * 2. Adds all the passed views to the {@code TickerView} by the methods {@code addChildView()} or {@code setChildViews(List<View> childViews)}.
     * 3. Corresponding Layout params are set for all the views, the first and last components are set with width equal to screen width, so that the view scrooling starts at left side of screen and the last component completes the cycle at rigjt end of screen, then only the new cycle begins.
     * 4. An empty marker view is placed at the end of the view list so that, as soon as the ScrollBounds of that view comes into the {@code Rect}, we start a new cycle and starts scrolling form the first record.
     * 5. We add {@code ViewTreeObserver.OnGlobalLayoutListener} to intercept changes in global layout and start auto scrolling by calling {@code startAutoScrolling()} method.
     */
    public void showTickers() {
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        removeAllViewsInLayout();
        linearLayout = new LinearLayout(getContext());

        final LinearLayout.LayoutParams lp_par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final LinearLayout.LayoutParams lp_par_0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final LinearLayout.LayoutParams lp_par_last = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        lp_par.setMargins(10, 3, 5, 3);
        lp_par_0.setMargins(((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth(), 3, 5, 3);
        lp_par_last.setMargins(5, 3, ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth(), 3);
        if (childViews != null && !childViews.isEmpty()) {
            for (int index = 0; index < childViews.size(); index++) {
                if (index == 0) {
                    linearLayout.addView(childViews.get(index), lp_par_0);
                } else if (index == childViews.size() - 1) {
                    linearLayout.addView(childViews.get(index), lp_par_last);

                } else {
                    linearLayout.addView(childViews.get(index), lp_par);
                }
            }
            lastTicker = new TextView(getContext());
            lastTicker.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            lastTicker.setVisibility(INVISIBLE);
            linearLayout.addView(lastTicker, lp_par);

            ViewTreeObserver vto = getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    startAutoScrolling();
                }
            });
            addView(linearLayout);
        }
    }

    /**
     * This method initiates scheduler for auto scrolling, If there is already a scheduled {@code Timer}, this method will first cancel the timer and reschedule it. On Regular intervals,
     * this {@code Timer} runs a {@code moveScrollView()} method on UI thread to scroll the views by the provided {@code getDisplacement()} value.
     * <p>
     * This only schedules a timer if the {@code getDisplacement()} value is greater than 0.
     */
    public void startAutoScrolling() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
        }
        scrollTimer = new Timer();
        final Runnable timer_tick = new Runnable() {
            public void run() {
                moveScrollView();
            }
        };

        if (scrollerSchedule != null) {
            scrollerSchedule.cancel();
            scrollerSchedule = null;
        }
        scrollerSchedule = new TimerTask() {
            @Override
            public void run() {
                try {
                    ((Activity) getContext()).runOnUiThread(timer_tick);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this != null) {
                        this.cancel();
                    }
                }
            }
        };

        if (displacement > 0) {
            scrollTimer.schedule(scrollerSchedule, 30, 30);
        }

    }

    /**
     * All the smooth scrolling/moving logic is implemented in this method. This method moves the Ticker views by the provided displacement value.
     * <p>
     * The logic in place is to get a {@code Rect} and set it for custom last marker ticker(invisible). An additional {@code Rect} object is created whose horizontal bounds are
     * for how much the view has scrolled added to its width. Once we have both of these objects, we simply check if the {@code Rect}object for last custom ticker view (invisible)
     * intersects with  the current screen {@code Rect} object, as soon as the condition is met, which means the last hidden view has scrolled on to the screen, which in tern means all the ticker items have been shown, we set the scroll position to 0 again,, and the scrolling starts from the first Tivker view again.
     */
    public void moveScrollView() {

        try {
            scrollPos = (int) (getScrollX() + displacement);

            final Rect bounds = new Rect();
            lastTicker.getHitRect(bounds);

            final Rect scrollBounds = new Rect(getScrollX(), getScrollY(), getScrollX()
                    + getWidth(), getScrollY() + getHeight());

            if (Rect.intersects(scrollBounds, bounds)) {
                // is visible
                scrollPos = 0;
                scrollTo(scrollPos, 0);
            } else {
                smoothScrollTo(scrollPos, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method cancels {@code Timer} and {@code TimerTask} and there callback.
     */
    public void destroyAllScheduledTasks() {
        clearTimerTaks(scrollerSchedule);
        clearTimers(scrollTimer);

        scrollerSchedule = null;
        scrollTimer = null;

    }

    /**
     * This method cancels {@code Timer}.
     */
    private void clearTimers(Timer timer) {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * This method cancels {@code TimerTask}.
     */
    private void clearTimerTaks(TimerTask timerTask) {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}
