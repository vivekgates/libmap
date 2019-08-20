package org.unyde.mapintegrationlib.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.widget.NestedScrollView;

public class MyScrollView extends NestedScrollView {

    public static final int MAX_SCROLL_FACTOR = 1;
    boolean isAutoScrolling;

    public boolean isAutoScrolling() {
        return isAutoScrolling;
    }

    public void setAutoScrolling(boolean autoScrolling) {
        isAutoScrolling = autoScrolling;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isAutoScrolling) {
            return super.onTouchEvent(event);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAutoScrolling){
            return super.onTouchEvent(event);
        }
        else {
            return false;
        }




    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {

        if (isAutoScrolling) {
            if (Math.abs(y - oldY) < MAX_SCROLL_FACTOR  || y >= getMeasuredHeight() || y == 0
                    || Math.abs(x - oldX) < MAX_SCROLL_FACTOR || x >= getMeasuredWidth() || x == 0) {
                isAutoScrolling = false;
            }
        }
        else {
            super.onScrollChanged(x, y, oldX, oldY);
        }
    }
}