package com.example.pengyi.progressview;

import android.animation.ValueAnimator;
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
  private int maxDiffRadius; // 大圆小圆最大半径差
  private int diffRadius; // 大圆小圆当前半径差
  private int progressWidth; // 进度条的宽度
  private int duration = 1000; // 进度条转满一圈的耗时
  private int progressColor = Color.WHITE; // 进度条颜色
  private int progressSecondColor; // 进度条背景色
  private boolean progressEnable = false; // 是否带进度条，false 不画进度条

  private int xCenter;
  private int yCenter;

  private Paint cirlePaint;
  private Paint progressPaint;
  private Paint progressSecondPaint;

  private boolean isClick = false;
  private boolean isFinish = false;
  private RectF progressOval;
  private int progress = 0;
  private OnFinishListener onFinishListener; // 带进度条设置
  private OnClickListener onClickListener; // 不带进度条设置


  public ProgressView(Context context) {
    this(context, null);
  }

  public ProgressView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
    maxDiffRadius = (int) typedArray.getDimension(R.styleable.ProgressView_diff_radius, 0);
    progressWidth = (int) typedArray.getDimension(R.styleable.ProgressView_progress_width, 0);
    duration = typedArray.getInt(R.styleable.ProgressView_duration, 1000);
    bgColor = typedArray.getColor(R.styleable.ProgressView_bg_color, Color.RED);
    progressColor = typedArray.getColor(R.styleable.ProgressView_progress_color, Color.WHITE);
    progressSecondColor =
        typedArray.getColor(R.styleable.ProgressView_progress_second_color, Color.WHITE);
    progressEnable = typedArray.getBoolean(R.styleable.ProgressView_progress_enable, false);
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

    progressSecondPaint = new Paint();
    progressSecondPaint.setAntiAlias(true);
    progressSecondPaint.setColor(progressSecondColor);
    progressSecondPaint.setStyle(Paint.Style.STROKE);
    progressSecondPaint.setStrokeWidth(progressWidth);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawCircle(canvas);
    drawProgress(canvas);
  }

  /**
   * 画圆
   * @param canvas
   */
  private void drawCircle(Canvas canvas) {
    xCenter = yCenter = getWidth() / 2;
    startRadius = getWidth() / 2 - maxDiffRadius;
    canvas.drawCircle(xCenter, yCenter, startRadius + diffRadius, cirlePaint);
  }


  /**
   * 画进度条
   * @param canvas
   */

  private void drawProgress(Canvas canvas) {
    if (!progressEnable) {
      return;
    }
    if (isClick) {
      if (progressOval == null) {
        progressOval = new RectF(maxDiffRadius, maxDiffRadius, maxDiffRadius + 2 * startRadius,
            maxDiffRadius + 2 * startRadius);
      }
      canvas.drawArc(progressOval, -90, progress, false, progressPaint);
      canvas.drawArc(progressOval, -90, 360, false, progressSecondPaint);
      if (progress >= 360) {
        progress = 0;
        isClick = false;
        isFinish = true;
        startCircleAnimator();
        if (onFinishListener != null) {
          onFinishListener.onFinish();
        }
      }
    } else {
      progress = 0;
    }
  }

  private ValueAnimator circleAnimator = null;

  /**
   * 画圆的动画  isclick 为true时扩张， 为false时收缩
   */
  private void startCircleAnimator() {
    if (circleAnimator != null) {
      circleAnimator.cancel();
    }
    if (isClick) {
      circleAnimator = ValueAnimator.ofInt(0, maxDiffRadius);
    } else {
      circleAnimator = ValueAnimator.ofInt(diffRadius, maxDiffRadius, 0);
    }
    circleAnimator.setDuration(500);
    circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        diffRadius = (int) animation.getAnimatedValue();
        invalidate();
      }
    });
    circleAnimator.start();
  }

  private ValueAnimator progressAnimator = null;

  /**
   * 画进度条的动画
   */
  public void startProgressAnimator() {
    if (!progressEnable) {
      return;
    }
    cancleProgressAnimator();
    progressAnimator = ValueAnimator.ofInt(0, 360);
    progressAnimator.setDuration(duration);
    progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        progress = (int) animation.getAnimatedValue();
        invalidate();
      }
    });
    progressAnimator.start();
  }

  public void cancleProgressAnimator() {
    if (progressAnimator != null) {
      progressAnimator.cancel();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isClick = true;
        startCircleAnimator();
        startProgressAnimator();
        break;
      case MotionEvent.ACTION_UP:
        if (!progressEnable && onClickListener != null) {
          onClickListener.onClick(this);
        }
      case MotionEvent.ACTION_CANCEL:
        isClick = false;
        cancleProgressAnimator();
        if (!isFinish) {
          startCircleAnimator();
        }
        isFinish = false;
        break;

    }
    return true;
  }

  public void setOnFinishListener(OnFinishListener onFinishListner) {
    this.onFinishListener = onFinishListner;
  }

  public void setOnClickListener(OnClickListener onClickListner) {
    this.onClickListener = onClickListner;
  }

  public interface OnFinishListener {
    void onFinish();
  }

  public interface OnClickListener {
    void onClick(View view);
  }


}
