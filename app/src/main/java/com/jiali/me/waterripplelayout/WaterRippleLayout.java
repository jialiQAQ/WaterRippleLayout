package com.jiali.me.waterripplelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author jiali
 * @date 2018/3/7
 */

public class WaterRippleLayout extends ViewGroup {

    private int circleRadius = 0;

    private float x, y;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private OnClickListener onClickListener;

    public WaterRippleLayout(Context context) {
        super(context);
        init(context, null);
    }

    public WaterRippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaterRippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public WaterRippleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        invalidate();
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init(Context context, AttributeSet attributeSet) {
        paint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int usedWidth = 0;
        int usedHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            usedHeight += childView.getMeasuredHeight() + layoutParams.leftMargin + layoutParams.rightMargin;
            usedWidth += childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        }
        usedHeight += getPaddingTop() + getPaddingBottom();
        usedWidth += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(resolveSize(Math.max(usedWidth, getSuggestedMinimumWidth()), widthMeasureSpec), resolveSize(Math.max(usedHeight, getSuggestedMinimumHeight()), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            view.layout(left, top, left + width, top + height);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(x, y, circleRadius, paint);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x = event.getRawX();
                y = event.getRawY();
                int max = getMeasuredHeight() > getMeasuredWidth() ? getMeasuredHeight() : getMeasuredWidth();
                ObjectAnimator animator = ObjectAnimator.ofInt(this, "circleRadius", 0, max);
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        x = 0;
                        y = 0;
                        circleRadius = 0;
                        if (onClickListener != null) {
                            onClickListener.onClick(WaterRippleLayout.this);
                        }
                    }
                });
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
