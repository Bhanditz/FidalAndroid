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
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.UiThread;

@UiThread
public class ParabolicAnimationView extends View {
    private static final float G = -9.764f;
    private static final float STROKE_WIDTH = 16;
    private static final float SLOWDOWN_FACTOR = 3;
    private final Paint pointsPaint;
    private final Paint bezierPaint;
    private final Path path = new Path();
    private final PointF bezierStart = new PointF();
    private final PointF bezierEnd = new PointF();
    private final PointF bezierQuadControl = new PointF();
    private final PointF bezierCubicControl1 = new PointF();
    private final PointF bezierCubicControl2 = new PointF();
    private float factor;
    private float equationA;
    private float equationB;
    private boolean completed = false;
    private float angle;
    private float v0;
    private int paddingRight;
    private int paddingLeft;
    private int paddingTop;
    private int paddingBottom;

    public ParabolicAnimationView(Context context) {
        this(context, null, 0);
    }

    public ParabolicAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParabolicAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        pointsPaint = new Paint();
        pointsPaint.setColor(Color.RED);

        bezierPaint = new Paint();
        bezierPaint.setColor(Color.RED);
        bezierPaint.setStrokeWidth(STROKE_WIDTH);
        bezierPaint.setStrokeCap(Paint.Cap.ROUND);
        bezierPaint.setStyle(Paint.Style.STROKE);
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

    public void setPadding(@Px int top, @Px int right, @Px int bottom, @Px int left) {
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
        paddingLeft = left;
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

    public void startAnimation(float dist) {
        resetAnimation();

        factor = (getWidth() - paddingLeft - paddingRight) / dist;
        angle = (float) (Math.PI / 3);
        v0 = (float) calcV0(dist, angle, G);
        equationA = (float) getEquationA(G, v0, angle);
        equationB = (float) getEquationB(angle);
        float time = (float) calcTime(dist, angle, G);

        ValueAnimator animator = new ValueAnimator();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setFloatValues(0, 1);
        animator.setDuration((long) (time * 1000f * SLOWDOWN_FACTOR));
        animator.addUpdateListener(animation -> {
            bezierStart.x = (float) animation.getAnimatedValue() * dist;
            bezierStart.y = (float) calcTrajectory(bezierStart.x, angle, G, v0);
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                completed = true;
                invalidate();
            }
        });

        animator.start();
    }

    public void resetAnimation() {
        bezierStart.x = paddingLeft;
        bezierStart.y = paddingTop;
        completed = false;
        path.reset();
        invalidate();
    }

    private void drawByPoint(@NonNull Canvas canvas) {
        path.addCircle(bezierStart.x * factor + paddingLeft, bezierStart.y * factor + paddingTop, STROKE_WIDTH / 2, Path.Direction.CW);
        canvas.drawPath(path, pointsPaint);
    }

    private void drawBezier(@NonNull Canvas canvas) {
        bezierStart.x = 0;
        bezierStart.y = 0;
        bezierEnd.x = getWidth() - paddingLeft - paddingRight;
        bezierEnd.y = 0;

        canvas.save();
        canvas.translate(paddingLeft, paddingTop);

        getBezierCubicControl(equationA, equationB);

        path.reset();
        path.moveTo(bezierStart.x, bezierStart.y);
        path.cubicTo(bezierCubicControl1.x, bezierCubicControl1.y, bezierCubicControl2.x, bezierCubicControl2.y, bezierEnd.x, bezierEnd.y);
        canvas.drawPath(path, bezierPaint);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (completed) drawBezier(canvas);
        else drawByPoint(canvas);
    }
}
