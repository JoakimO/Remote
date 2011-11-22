package se.newbie.remote.gui;

import se.newbie.remote.action.RemoteActionListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class RemoteScrollerViewGroup extends ViewGroup implements
		RemoteGUIComponent {
	private static final String TAG = "ViewGroupScroller";

	private static final int SNAP_VELOCITY = 1000;

	private enum TouchState {
		rest, scrolling
	};

	private int touchSlop = 0;
	private int currentScreen = 0;
	private int scrollX = 0;
	private float lastMotionX;

	private Scroller scroller;
	private TouchState touchState = TouchState.rest;
	private VelocityTracker velocityTracker = null;

	public RemoteScrollerViewGroup(Context context) {
		super(context);
		init(context);
	}

	public RemoteScrollerViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		final int count = getChildCount();
		scroller = new Scroller(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (touchState != TouchState.rest)) {
			return true;
		}
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(x - lastMotionX);
			boolean xMoved = xDiff > touchSlop;
			if (xMoved) {
				touchState = TouchState.scrolling;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			lastMotionX = x;
			touchState = scroller.isFinished() ? TouchState.rest
					: TouchState.scrolling;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TouchState.rest;
			break;
		case MotionEvent.ACTION_UP:
			touchState = TouchState.rest;
			break;
		}
		return touchState != TouchState.rest;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			final int deltaX = (int) (lastMotionX - x);
			lastMotionX = x;
			if (deltaX < 0) {
				if (scrollX > 0) {
					scrollBy(Math.max(-scrollX, deltaX), 0);
				}
			} else if (deltaX > 0) {
				final int availableToScroll = getChildAt(getChildCount() - 1)
						.getRight() - scrollX - getWidth();
				if (availableToScroll > 0) {
					scrollBy(Math.min(availableToScroll, deltaX), 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			// innerVelocityTracker = velocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();

			if (velocityX > SNAP_VELOCITY && currentScreen > 0) {
				snapToScreen(currentScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& currentScreen < getChildCount() - 1) {
				snapToScreen(currentScreen + 1);
			} else {
				snapToDestination();
			}

			if (velocityTracker != null) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
			touchState = TouchState.rest;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TouchState.rest;
			break;
		}
		scrollX = this.getScrollX();

		return true;
	}

	private void snapToDestination() {
		Log.v(TAG, "Snap to destination");
		final int screenWidth = getWidth();
		final int whichScreen = (scrollX + (screenWidth / 2)) / screenWidth;
		snapToScreen(whichScreen);
	}

	public void snapToScreen(int whichScreen) {
		Log.v(TAG, "Snap to screen: " + whichScreen);
		currentScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		final int delta = newX - scrollX;
		scroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
	}

	public void setToScreen(int whichScreen) {
		Log.v(TAG, "Set to screen: " + whichScreen);
		currentScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		scroller.startScroll(newX, 0, 0, 0, 10);
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;

		final int count = getChildCount();
		Log.v(TAG, "child count: " + count);
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isInEditMode()) {
			final int width = MeasureSpec.getSize(widthMeasureSpec);
			final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			if (widthMode != MeasureSpec.EXACTLY) {
				throw new IllegalStateException("error mode.");
			}

			final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
			if (heightMode != MeasureSpec.EXACTLY) {
				throw new IllegalStateException("error mode.");
			}

			final int count = getChildCount();
			Log.v(TAG, "child count: " + count);
			for (int i = 0; i < count; i++) {
				getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
			}
			Log.i(TAG, "moving to screen " + currentScreen);
			scrollTo(currentScreen * width, 0);
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollX = scroller.getCurrX();
			scrollTo(scrollX, 0);
			postInvalidate();
		}
	}

	public void addListener(RemoteActionListener listener) {
	}
}
