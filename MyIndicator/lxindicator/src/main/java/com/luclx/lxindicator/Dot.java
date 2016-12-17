package com.luclx.lxindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by LucLX on 12/17/16.
 */

public class Dot {

    private Paint mPaint;
    private PointF mCenter;
    private int currentRadius;

    public Dot() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCenter = new PointF();
    }

    /**
     * @param color
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * @param alpha
     */
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * @param x
     * @param y
     */
    public void setCenter(float x, float y) {
        mCenter.set(x, y);
    }

    /**
     * @return
     */
    public int getCurrentRadius() {
        return currentRadius;
    }

    /**
     * @param currentRadius
     */
    public void setCurrentRadius(int currentRadius) {
        this.currentRadius = currentRadius;
    }

    /**
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawCircle(mCenter.x, mCenter.y, this.currentRadius, mPaint);
    }

}
