package com.example.pengyi.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pengyi on 2018/9/21.
 */

public class ProgressView extends View {
  private int bgColor = Color.RED;
  private int startRadius; // 小圆的半径
  private int endRadius; // 大圆的半径
  private int diffRadius; // 大圆小圆半径差
  private int progressWidth; // 进度的宽度
  private int progressColor = Color.WHITE;
  private int xCenter;
  private int yCenter;

  private Paint cirlePaint;
  private Paint progressPaint;

  private boolean isClick = false;
  private RectF progressOval;
  private int progress = 0;
  private boolean isFinish = false;
  private OnFinishListner onFinishListner;
  private int rate = 5;



  public ProgressView(Context context) {
    this(context, null);
  }

  public ProgressView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
    diffRadius = (int) typedArray.getDimension(R.styleable.ProgressView_diff_radius, 0);
    progressWidth = (int) typedArray.getDimension(R.styleable.ProgressView_progress_width, 0);
    bgColor = typedArray.getColor(R.styleable.ProgressView_bg_color, Color.RED);
    progressColor = typedArray.getColor(R.styleable.ProgressView_progress_color, Color.WHITE);
    init();
  }

  private void init() {
    cirlePaint = new Paint();
    cirlePaint.setAntiAlias(true);
    cirlePaint.setColor(bgColor);
    cirlePaint.setStyle(Paint.Style.FILL);

    progressPaint = new Paint();
    progressPaint.setAntiAlias(true);
    progressPaint.setColor(progressColor);
    progressPaint.setStyle(Paint.Style.STROKE);
    progressPaint.setStrokeWidth(progressWidth);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawCircle(canvas);
    drawProgress(canvas);
  }

  private void drawCircle(Canvas canvas) {
    xCenter = yCenter = getWidth() / 2;
    endRadius = getWidth() / 2;
    startRadius = endRadius - diffRadius;
    if (isClick && !isFinish) {
      canvas.drawCircle(xCenter, yCenter, endRadius, cirlePaint);
    } else {
      canvas.drawCircle(xCenter, yCenter, startRadius, cirlePaint);
    }
  }

  private void drawProgress(Canvas canvas) {
    if (isClick && !isFinish) {
      progressOval = new RectF(diffRadius, diffRadius, diffRadius + 2 * startRadius,
          diffRadius + 2 * startRadius);
      canvas.drawArc(progressOval, -90, progress, false, progressPaint);
      progress = progress + rate;
      if (progress >= 360) {
        isFinish = true;
        progress = 0;
        if (onFinishListner != null) {
          onFinishListner.onFinish();
        }
      }
      invalidate();
    } else {
      progress = 0;
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isClick = true;
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        isClick = false;
        isFinish = false;
        invalidate();
        break;

    }
    return true;
  }

  public void setOnFinishListner(OnFinishListner onFinishListner) {
    this.onFinishListner = onFinishListner;
  }

  public interface OnFinishListner {
    void onFinish();
  }


}
