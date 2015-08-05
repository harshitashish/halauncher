package com.ha.halauncher.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ha.halauncher.R;

public class TestLayout extends ViewGroup implements BaseHomeLayout{

private int mLeftWidth;
private int mRightWidth;


/** These are used for computing child frames based on their gravity. */
private final Rect mTmpContainerRect = new Rect();
private final Rect mTmpChildRect = new Rect();
private final String TAG = "com.ha.halauncher.home.TestLayout";

public TestLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}


	public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}


	public TestLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	public TestLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
		
	 // Does not scroll
	 @Override
	 public boolean shouldDelayChildPressedState() {
	        return false;
	 }


		/**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0;
        mRightWidth = 0;

        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // Measure the child.
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                
                Log.e(TAG, "measure Child Withs");

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                
                
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else {
                    maxWidth = Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                }
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                
                
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
        maxWidth += mLeftWidth + mRightWidth;

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
        resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // This is the middle region inside of the gutter.
        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = rightPos - mRightWidth;

        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                
                ItemGrid ig = positionInLayout(width + lp.leftMargin + lp.rightMargin, height + lp.topMargin + lp.bottomMargin, rightPos-leftPos, parentBottom - parentTop);

                ig.startRow = lp.row;
                ig.startColumn = lp.column;
                
                           
                // Compute the frame in which we are placing this child.
               
                mTmpContainerRect.left = leftPos   + (((rightPos-leftPos)/4) * ig.startColumn);
                mTmpContainerRect.right = mTmpContainerRect.left + ((rightPos-leftPos)/4) * ig.numColumn;
                   
              
                mTmpContainerRect.top = parentTop + (((parentBottom - parentTop)/4) * ig.startRow);
                mTmpContainerRect.bottom = mTmpContainerRect.top + ((parentBottom - parentTop)/4) * ig.numRow;

                // Use the child's gravity and size to determine its final
                // frame within its container.
               // Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);
                
                Log.e(TAG, mTmpContainerRect.toString());

                // Place the child. *****
                child.layout(mTmpContainerRect.left,mTmpContainerRect.top,
                		mTmpContainerRect.right, mTmpContainerRect.bottom);
            }
        }
    }
    
    
    private class ItemGrid {

		int numRow;
		int numColumn;
		int startRow;
		int startColumn;
		

	}
    
    private ItemGrid positionInLayout(int width, int height, int layoutWidth, int layoutHeight) {
		ItemGrid ig = new ItemGrid();
		
		Log.e(TAG, "Height :: " + height + "  Width :: " + width);
		Log.e(TAG, "lyHeight :: " + layoutHeight + "  lyWidth :: " + layoutWidth);

		/*DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		float lyHeight = layoutHeight
				/ (displayMetrics.densityDpi / 160);
		float lyWidth = layoutWidth
				/ (displayMetrics.densityDpi / 160);

		Log.e(TAG, "densith:: " + displayMetrics.densityDpi / 160);

		ig.numRow = (int) Math.ceil((height / 3) / (lyHeight / 4));
		ig.numColumn = (int) Math.ceil((width / 3) / (lyWidth / 4));*/
		
		ig.numRow = (int) Math.max(Math.ceil((double)height / (layoutHeight/4) ), 1);
		ig.numColumn = (int) Math.max(Math.ceil((double) width / (layoutWidth/4) ), 1);
		
		

		//Log.e(TAG, "lyHeight1 :: " + lyHeight + "  lyWidth1 :: " + lyWidth);

		Log.e(TAG, "rows :: " + ig.numRow + "  columns :: " + ig.numColumn);

		return ig;
	}

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TestLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int gravity = Gravity.TOP | Gravity.START;

        public static int POSITION_MIDDLE = 0;
        public static int POSITION_LEFT = 1;
        public static int POSITION_RIGHT = 2;
        
        public int row=1;
        public int column=1;

        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.TestLayoutLP);
            gravity = a.getInt(R.styleable.TestLayoutLP_android_layout_gravity, gravity);
            position = 1;
            
            row = a.getInt(R.styleable.TestLayoutLP_layout_row,row);
            column = a.getInt(R.styleable.TestLayoutLP_layout_column, column);
            a.recycle();
            
            
        }

        public LayoutParams(int width, int height) {
            super(width, height);

        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }


}
