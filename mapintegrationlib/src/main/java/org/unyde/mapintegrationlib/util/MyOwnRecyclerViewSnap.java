package org.unyde.mapintegrationlib.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOwnRecyclerViewSnap extends RecyclerView {


    public MyOwnRecyclerViewSnap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyOwnRecyclerViewSnap(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyOwnRecyclerViewSnap(Context context) {
        super(context);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

//these four variables identify the views you see on screen.
        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);

//these variables get the distance you need to scroll in order to center your views.
//my views have variable sizes, so I need to calculate side margins separately.
//note the subtle difference in how right and left margins are calculated, as well as
//the resulting scroll distances.
        int leftMargin = (getWidth() - lastView.getWidth()) / 2;
        int rightMargin = (getWidth() - firstView.getWidth()) / 2 + firstView.getWidth();
        int leftEdge = lastView.getLeft();
        int rightEdge = firstView.getRight();
        int scrollDistanceLeft = leftEdge - leftMargin;
        int scrollDistanceRight = rightMargin - rightEdge;

//if(user swipes to the left)
        if(velocityX > 0) smoothScrollBy(scrollDistanceLeft, 0);
        else smoothScrollBy(-scrollDistanceRight, 0);

        return true;
    }
}
