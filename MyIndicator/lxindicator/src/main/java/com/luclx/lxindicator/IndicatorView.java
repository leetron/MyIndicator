package com.luclx.lxindicator;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.animation.AnimatorSet.Builder;

/**
 * Created by LucLX on 12/17/16.
 */

public class IndicatorView extends View implements LucLXIndicatorInterface, ViewPager.OnPageChangeListener {
    private static final long DEFAULT_ANIMATE_DURATION = 200;
    private static final int DEFAULT_RADIUS_SELECTED = 20;
    private static final int DEFAULT_RADIUS_UNSELECTED = 15;
    private static final int DEFAULT_DISTANCE = 40;

    private ViewPager mViewPage;
    private Dot[] dots;

    private long animateDuration = DEFAULT_ANIMATE_DURATION;
    private int radiusSelected = DEFAULT_RADIUS_SELECTED;
    private int radiusUnselected = DEFAULT_RADIUS_UNSELECTED;
    private int distance = DEFAULT_DISTANCE;
    private int colorSelected;
    private int colorUnselected;

    private int beforePosition;
    private int currentPosition;

    private ValueAnimator animatorZoomIn;
    private ValueAnimator animatorZoomOut;

    /**
     * default constructor
     *
     * @param context
     */
    public IndicatorView(Context context) {
        super(context);
    }

    /**
     * get custom value from xml layout
     *
     * @param context
     * @param attrs
     */
    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        this.radiusSelected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_luclx_radius_selected, DEFAULT_RADIUS_SELECTED);
        this.radiusUnselected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_luclx_color_unselected, DEFAULT_RADIUS_UNSELECTED);
        this.colorSelected = typedArray.getColor(R.styleable.IndicatorView_luclx_color_selected, Color.parseColor("#ffffff"));
        this.colorUnselected = typedArray.getColor(R.styleable.IndicatorView_luclx_color_unselected, Color.parseColor("#ffffff"));
        this.distance = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_luclx_distance, DEFAULT_DISTANCE);

        //recycle typeArray
        typedArray.recycle();
    }

    @Override
    public void setViewPage(ViewPager viewPager) throws PageLessException {
        this.mViewPage = viewPager;
        this.mViewPage.addOnPageChangeListener(this);
        initDot(viewPager.getAdapter().getCount());
        onPageSelected(0);
    }

    /**
     * get custom value from xml layout with style
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // lifecycle of View: onMeasure -> onLayout ->onDraw, so we need get line when override methods
    // onMeasure => to get width and height of View.
    // onLayout => from width and height from onMeasure, we can calculate dot's position inside IndicatorView. We must co that after onMeasure.
    // onDraw => from dot's position, we can draw dots.
    // ez to understand -__- (LoL)

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // after parent view calculated will sent to child view pass onMeasure
        // so child View can calculate width and height
        // EXACTLY: specify exactly size
        // AT_MOST: child view always smaller heightMeasureSpec
        // UNSPECIFIED: child view is undefined

        int desireHeight = 2 * radiusSelected;

        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = withSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = 0;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desireHeight, heightSize);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = 0;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * calculate position of all dot
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // all dot in center vertical of view
        float yCenter = getHeight() / 2;

        // calculate distance between center from distance
        int d = this.distance + 2 * radiusSelected;

        // 1 unit = d/2
        // count = 2 => 1/2 unit
        // count = 3 => 1 unit
        // count = 4 => 1.5 unit
        // count = 5 => 2 unit
        // calculate xCenter of the first dot. from the second dot = 2 * xCenter(first dot)
        float xCenterFirst = getWidth() / 2 - (dots.length - 1) * d / 2;

        // calculate position of all dots
        for (int i = 0; i < dots.length; i++) {
            dots[i].setCenter(i == 0 ? xCenterFirst : xCenterFirst + d * i, yCenter);
            dots[i].setCurrentRadius(i == currentPosition ? radiusSelected : radiusUnselected);
            dots[i].setColor(i == currentPosition ? colorSelected : colorUnselected);
            dots[i].setAlpha(i == currentPosition ? 255 : radiusUnselected * 255 / radiusSelected);
        }
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        for (Dot dot : dots) {
            dot.draw(canvas);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.beforePosition = this.currentPosition;
        this.currentPosition = position;
        Log.e("LUC", "beforePosition " + beforePosition);
        Log.e("LUC", "currentPosition " + currentPosition);

        if (this.beforePosition == this.currentPosition) {
            this.beforePosition = this.currentPosition + 1;
        }

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(animateDuration);

        // zoom in with selected dot
        animatorZoomIn = ValueAnimator.ofInt(radiusUnselected, radiusSelected);
        animatorZoomIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int newRadius = (int) valueAnimator.getAnimatedValue();
                changeNewRadius(currentPosition, newRadius);
            }
        });

        // zoom out with unselected dot
        animatorZoomOut = ValueAnimator.ofInt(radiusSelected, radiusUnselected);
        animatorZoomOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int newRadius = (int) valueAnimator.getAnimatedValue();
                changeNewRadius(beforePosition, newRadius);
            }
        });

        animationSet.play(animatorZoomIn).with(animatorZoomOut);
        animationSet.start();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * init dot count, if count < 2 throw exception
     *
     * @param dotCount
     * @throws PageLessException
     */
    private void initDot(int dotCount) throws PageLessException {
        if (dotCount < 2) throw new PageLessException();
        dots = new Dot[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new Dot();
        }
    }

    @Override
    public void setAnimateDuration(long duration) {
        this.animateDuration = duration;
    }

    @Override
    public void setRadiusSelected(int radiusSelected) {
        this.radiusSelected = radiusSelected;
    }

    @Override
    public void setRadiusUnselected(int radiusUnselected) {
        this.radiusUnselected = radiusUnselected;
    }

    @Override
    public void setDistanceDot(int distanceDot) {
        this.distance = distanceDot;
    }

    private void changeNewRadius(int positionPerform, int newRadius) {
        if (dots[positionPerform].getCurrentRadius() != newRadius) {
            dots[positionPerform].setCurrentRadius(newRadius);
            dots[positionPerform].setAlpha(newRadius * 255 / radiusSelected);

            //redraw view
            invalidate();
        }
    }
}
