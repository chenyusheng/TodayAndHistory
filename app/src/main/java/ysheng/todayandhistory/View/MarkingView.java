package ysheng.todayandhistory.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import ysheng.todayandhistory.R;


public class MarkingView extends View {

    private Paint mPaint;

    //底色的圆
    private float bottomWidth = 200;
    private float bottomHeight;
    private Paint bottomPaint;
    private int bottomColor = R.color.moccasin;//底圆的颜色
    //上层的圆
    private float topWidth;
    private float topHeight;
    private Paint topPaint;
    private int topColor = R.color.gold;//底圆的颜色

    private int ringWidth = 20;//圆环的宽度

    private int angle = 270;//上层圆环的着色角度
    private int currentAngle = 0;//上层圆环的着色角度

    private int centerColor = R.color.white;//圆环中间的颜色
    private Paint centerPaint;

    public MarkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //初始化画笔
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentAngle = 0;

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        mPaint.setColor(Color.WHITE);
////        mPaint.setAlpha(0);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        if(currentAngle==0){
            topWidth = getWidth();
            bottomWidth = getWidth();
            ringWidth = 20;
            startAnimation();
        }else{
            drawBottom(canvas);
            drawTop(canvas);
            drawCenter(canvas);
        }


    }

    public void setRingWidth(int ringWidth) {
        this.ringWidth = ringWidth;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
    }

    protected void drawBottom(Canvas canvas) {
        bottomPaint.setColor(getResources().getColor(bottomColor));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, bottomWidth / 2, bottomPaint);
    }

    protected void drawTop(Canvas canvas) {
        topPaint.setColor(getResources().getColor(topColor));
        RectF rectF = new RectF(0, 0, getWidth(), getWidth());
        canvas.drawArc(rectF, 180, currentAngle, true, topPaint);
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, topWidth / 2, topPaint);
    }

    protected void drawCenter(Canvas canvas) {
        centerPaint.setColor(getResources().getColor(centerColor));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (topWidth - ringWidth * 2) / 2, centerPaint);
    }

    private void startAnimation() {

        ValueAnimator anim = ValueAnimator.ofInt(1, angle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                currentPoint = (Point) animation.getAnimatedValue();
                currentAngle = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(3000);
        anim.start();
    }

}  