package com.gianlu.fidal.Animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.annotation.UiThread;

@UiThread
public class HorizontalParabolicAnimationView extends AbsCompetitionAnimationView {
    private static final float G = -9.764f;
    private static final float SLOWDOWN_FACTOR = 2.5f;
    private static final float DISTANCE_PARABOLA_PADDING = 32;
    private static final float TEXT_DISTANCE_PADDING = 16;
    private static final int DISTANCE_TEXT_SIZE = 32;
    private final float mStrokeWidth;
    private final Paint pointsPaint;
    private final Paint bezierPaint;
    private final Paint distancePaint;
    private final Paint distanceTextPaint;
    private final Path path = new Path();
    private final PointF point = new PointF();
    private final PointF bezierStart = new PointF();
    private final PointF bezierEnd = new PointF();
    private final PointF bezierQuadControl = new PointF();
    private final PointF bezierCubicControl1 = new PointF();
    private final PointF bezierCubicControl2 = new PointF();
    private final Rect textBounds = new Rect();
    private float equationA;
    private float equationB;
    private boolean completed = false;
    private float angle;
    private float v0;
    private int paddingRight;
    private int paddingLeft;
    private int paddingTop;
    private int paddingBottom;
    private float maxY;
    private ValueAnimator currentAnimator;
    private float dist;
    private float factor;

    public HorizontalParabolicAnimationView(@NonNull Context context) {
        super(context);
        setWillNotDraw(false);

        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        pointsPaint = new Paint();
        pointsPaint.setColor(Color.RED);

        bezierPaint = new Paint();
        bezierPaint.setColor(Color.RED);
        bezierPaint.setStrokeWidth(mStrokeWidth);
        bezierPaint.setStrokeCap(Paint.Cap.ROUND);
        bezierPaint.setStyle(Paint.Style.STROKE);

        distancePaint = new Paint();
        distancePaint.setColor(Color.LTGRAY);
        distancePaint.setStrokeCap(Paint.Cap.ROUND);
        distancePaint.setStrokeWidth(mStrokeWidth);

        distanceTextPaint = new Paint();
        distanceTextPaint.setColor(Color.LTGRAY);
        distanceTextPaint.setTextSize(DISTANCE_TEXT_SIZE);
    }

    private static double calcTrajectory(float x, float alpha, float a, float v0) {
        return x * getEquationB(alpha) + (getEquationA(a, v0, alpha) * Math.pow(x, 2));
    }

    private static double calcV0(float x, float alpha, float a) {
        return Math.sqrt(-a * x / Math.sin(alpha * 2));
    }

    private static double calcTime(float x, float alpha, float a) {
        return Math.sqrt(Math.sin(alpha * 2) / -a * x);
    }

    private static double getEquationA(float a, float v0, float alpha) {
        return a / (Math.pow(v0, 2) * Math.pow(Math.cos(alpha), 2) * 2);
    }

    private static double getEquationB(float alpha) {
        return Math.tan(alpha);
    }

    private static double getMaxY(float a, float alpha, float v0) {
        return -Math.pow(v0, 2) * Math.pow(Math.sin(alpha), 2) / (a * 2);
    }

    public void setPadding(@Px int top, @Px int right, @Px int bottom, @Px int left) {
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
        paddingLeft = left;

        requestLayout();
        invalidateOutline();
    }

    private void getBezierQuadControl(float a, float b) {
        bezierQuadControl.x = (bezierStart.x + bezierEnd.x) / 2;
        bezierQuadControl.y = ((bezierEnd.x - bezierStart.x) / 2) * (2 * a * bezierStart.x + b) + bezierStart.y;
    }

    private void getBezierCubicControl(float a, float b) {
        getBezierQuadControl(a, b);

        bezierCubicControl1.x = bezierQuadControl.x * 2f / 3f + bezierStart.x / 3f;
        bezierCubicControl1.y = bezierQuadControl.y * 2f / 3f + bezierStart.y / 3f;

        bezierCubicControl2.x = bezierQuadControl.x * 2f / 3f + bezierEnd.x / 3f;
        bezierCubicControl2.y = bezierQuadControl.y * 2f / 3f + bezierEnd.y / 3f;
    }

    private void calcFactor() {
        factor = (getMeasuredWidth() - paddingLeft - paddingRight) / dist;
    }

    public void prepareAnimation(float dist, float degrees) {
        resetAnimation();

        this.dist = dist;
        calcFactor();

        angle = (float) Math.toRadians(degrees);
        v0 = (float) calcV0(dist, angle, G);
        maxY = (float) getMaxY(G, angle, v0);
        equationA = (float) getEquationA(G, v0, angle);
        equationB = (float) getEquationB(angle);
        float time = (float) calcTime(dist, angle, G);

        currentAnimator = new ValueAnimator();
        currentAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        currentAnimator.setFloatValues(0, 1);
        currentAnimator.setDuration((long) (time * 1000f * SLOWDOWN_FACTOR));
        currentAnimator.addUpdateListener(animation -> {
            point.x = (float) animation.getAnimatedValue() * dist;
            point.y = (float) calcTrajectory(point.x, angle, G, v0);
            invalidate();
        });
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                completed = true;
                invalidate();
            }
        });

        requestLayout();
        invalidateOutline();
    }

    public void startAnimation() {
        if (currentAnimator != null) currentAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxY > 0) {
            distanceTextPaint.getTextBounds("1", 0, 1, textBounds);

            calcFactor();

            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec((int) (maxY * factor + paddingTop + paddingBottom + DISTANCE_PARABOLA_PADDING + TEXT_DISTANCE_PADDING + textBounds.height()), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void resetAnimation() {
        bezierStart.x = paddingLeft;
        bezierStart.y = paddingTop;
        completed = false;
        path.reset();
        invalidate();
    }

    private void drawByPoint(@NonNull Canvas canvas) {
        path.addCircle(point.x * factor + paddingLeft, point.y * factor + paddingBottom + DISTANCE_PARABOLA_PADDING + TEXT_DISTANCE_PADDING + textBounds.height(), mStrokeWidth / 2, Path.Direction.CW);
        canvas.drawPath(path, pointsPaint);
    }

    private void drawBezier(@NonNull Canvas canvas) {
        bezierStart.x = 0;
        bezierStart.y = paddingBottom - paddingTop;
        bezierEnd.x = getWidth() - paddingLeft - paddingRight;
        bezierEnd.y = paddingBottom - paddingTop;

        canvas.save();
        canvas.translate(paddingLeft, paddingTop + DISTANCE_PARABOLA_PADDING + TEXT_DISTANCE_PADDING + textBounds.height());

        getBezierCubicControl(equationA, equationB);

        path.reset();
        path.moveTo(bezierStart.x, bezierStart.y);
        path.cubicTo(bezierCubicControl1.x, bezierCubicControl1.y, bezierCubicControl2.x, bezierCubicControl2.y, bezierEnd.x, bezierEnd.y);
        canvas.drawPath(path, bezierPaint);
        canvas.restore();
    }

    private void drawDistance(@NonNull Canvas canvas) {
        String str = String.format(Locale.getDefault(), "%.2f", point.x);

        distanceTextPaint.getTextBounds(str, 0, str.length(), textBounds);
        canvas.drawText(str, paddingLeft + point.x * factor - textBounds.width() / 2f, getHeight() - paddingBottom, distanceTextPaint);

        float distanceBarY = getHeight() - paddingBottom - textBounds.height() - TEXT_DISTANCE_PADDING;
        canvas.drawLine(paddingLeft, distanceBarY, paddingLeft + point.x * factor, distanceBarY, distancePaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcFactor();

        drawDistance(canvas);

        canvas.save();
        canvas.scale(1, -1, getWidth() / 2f, getHeight() / 2f);
        if (completed) drawBezier(canvas);
        else drawByPoint(canvas);
        canvas.restore();
    }
}
