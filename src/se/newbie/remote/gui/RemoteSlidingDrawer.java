package se.newbie.remote.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SlidingDrawer;

public class RemoteSlidingDrawer extends SlidingDrawer {

    private boolean vertical;
    private int topOffset;
	
	
	public RemoteSlidingDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		int orientation = attrs.getAttributeIntValue("android", "orientation",
				ORIENTATION_VERTICAL);
		topOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
		vertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode == MeasureSpec.UNSPECIFIED
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			throw new RuntimeException(
					"SlidingDrawer cannot have UNSPECIFIED dimensions");
		}

		final View handle = getHandle();
		final View content = getContent();
		measureChild(handle, widthMeasureSpec, heightMeasureSpec);

		if (vertical) {
			int height = heightSpecSize - handle.getMeasuredHeight()
					- topOffset;
			content.measure(widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(height, heightSpecMode));
			heightSpecSize = handle.getMeasuredHeight() + topOffset
					+ content.getMeasuredHeight();
			widthSpecSize = content.getMeasuredWidth();
			if (handle.getMeasuredWidth() > widthSpecSize)
				widthSpecSize = handle.getMeasuredWidth();
		} else {
			int width = widthSpecSize - handle.getMeasuredWidth() - topOffset;
			getContent().measure(
					MeasureSpec.makeMeasureSpec(width, widthSpecMode),
					heightMeasureSpec);
			widthSpecSize = handle.getMeasuredWidth() + topOffset
					+ content.getMeasuredWidth();
			heightSpecSize = content.getMeasuredHeight();
			if (handle.getMeasuredHeight() > heightSpecSize)
				heightSpecSize = handle.getMeasuredHeight();
		}

		setMeasuredDimension(widthSpecSize, heightSpecSize);
	}
}
