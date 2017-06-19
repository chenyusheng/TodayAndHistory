package ysheng.todayandhistory.View;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import ysheng.todayandhistory.Util.Debug_AdLog;

/**
 * Created by 敬 on 2015/11/30.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean isFirst = false;
    /**
     * 初始大小
     */
    private float mInitScale;

    /**
     * 双击放大时放大值
     */
    private float mMidScale;
    /**
     * 缩放的最大值
     */
    private float mMaxScale;

    private Matrix mScaleMatrix;

    /**
     * 获取图片缩放比例
     */
    private ScaleGestureDetector mDetector;
    /** ImageView高度 */
    private int imgHeight;
    /** ImageView宽度 */
    private int imgWidth;
    /**
     * 记录上一次多点触控的数量
     */
    private int mLastPointerCount;

    private float mLastX;
    private float mLastY;

    private int mTouchSlop;

    private boolean canMove;
    private boolean isCheckLeftRight;
    private boolean isCheckTopBottom;

    private GestureDetector mGestureDetector;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.FIT_CENTER);
//        setScaleType(ScaleType.MATRIX);
        Debug_AdLog.e("ZoomImageView create");

        mDetector = new ScaleGestureDetector(context, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnTouchListener(this);


        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Debug_AdLog.e("ZoomImageView double");
                if (getScale() < mMidScale) {
                    mScaleMatrix.postScale(mMidScale / getScale(), mMidScale / getScale(), getWidth() / 2, getHeight() / 2);
                    setImageMatrix(mScaleMatrix);
                } else {
                    mScaleMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), getWidth() / 2, getHeight() / 2);
                    setImageMatrix(mScaleMatrix);
                }

                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Debug_AdLog.e("ZoomImageView attachedToWindow");
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("NewApi")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Debug_AdLog.e("ZoomImageView detacheFromWindow");
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {
        if (!isFirst) {
            Debug_AdLog.e("ZoomImageView onGlobalLayout !isfirst");
            //获取控件宽高
            int width = getWidth();
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null) {
                return;
            }
            float scale = 1.0f;
            int h = d.getIntrinsicHeight();
            int w = d.getIntrinsicWidth();

            if (w > width && h < height) {
                scale = width * 1.0f / w;
            }

            if (w < width && h > height) {
                scale = height * 1.0f / h;
            }

            if ((w > width && h > height) || (w < width && h < height)) {
                scale = Math.min(width * 1.0f / w, height * 1.0f / h);
            }

            mInitScale = scale;
            mMidScale = mInitScale * 2;
            mMaxScale = mInitScale * 3;

            int dx = getWidth() / 2 - w / 2;
            int dy = getHeight() / 2 - h / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);
            isFirst = true;
        }
    }

    public float getScale() {
        Debug_AdLog.e("ZoomImageView getScale");
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Debug_AdLog.e("ZoomImageView onScale");
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        if (getDrawable() == null) {
            return true;
        }


        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
        }

        if (scale * scaleFactor > mMaxScale) {
            scaleFactor = mMaxScale / scale;
        }
        mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

        checkScale();
        setImageMatrix(mScaleMatrix);
        return true;
    }

    /**
     * 在缩放时控制边界和位置
     */
    private void checkScale() {
        Debug_AdLog.e("ZoomImageView checkScale");
        RectF rect = getRectF();
        float detalX = 0;
        float detalY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rect.width() >= width) {

            if (rect.left > 0) {
                detalX = -rect.left;
            }
            if (rect.right < width) {
                detalX = width - rect.right;
            }
        }

        if (rect.height() >= height) {
            if (rect.top > 0) {
                detalY = -rect.top;
            }

            if (rect.bottom < height) {
                detalY = height - rect.bottom;
            }
        }
        //如果图片宽度或高度小于控件宽高，则让其居中
        if (rect.width() < width) {
            detalX = width / 2f - rect.right + rect.width() / 2f;
        }

        if (rect.height() < height) {
            detalY = height / 2f - rect.bottom + rect.height() / 2f;
        }

        mScaleMatrix.postTranslate(detalX, detalY);

    }

    private RectF getRectF() {
        Debug_AdLog.e("ZoomImageView getRectF");
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //双击时不允许触发移动
        Debug_AdLog.e("ZoomImageView onTouch");
        if (mGestureDetector.onTouchEvent(event)) {
            Debug_AdLog.e("ZoomImageView onTouchEvent");
            return true;
        }


        mDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        //获取触摸点数量
        int pointCount = event.getPointerCount();

        if(pointCount>1){
            if (getScaleType() != ScaleType.MATRIX) {
                setScaleType(ScaleType.MATRIX);
//                            mScaleMatrix.postScale(1.0f, 1.0f, imgWidth / 2, imgHeight / 2);
            }
        }
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointCount;
        y /= pointCount;

        if (mLastPointerCount != pointCount) {
            canMove = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointCount;
        RectF rectF = getRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastPointerCount = 0;

                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!canMove) {
                    canMove = isMoveAction(dx, dy);
                }
                if (canMove) {

                    RectF rect = getRectF();
                    if (getDrawable() != null) {
                        isCheckLeftRight = isCheckTopBottom = true;
                        //宽度小于控件宽度，不允许横向移动
                        if (rect.width() < getWidth()) {
                            isCheckLeftRight = false;
                            dx = 0;
                        }
                        //高度小于控件高度，不允许纵向移动
                        if (rect.height() < getHeight()) {
                            isCheckTopBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);

                        checkBorderTranslate();

                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;

                break;
        }
        return true;
    }

    /**
     * 图片移动边界控制
     */
    private void checkBorderTranslate() {
        Debug_AdLog.e("ZoomImageView checkBorderTranslate");
        RectF rect = getRectF();
        float detalX = 0;
        float detalY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rect.top > 0 && isCheckTopBottom) {
            detalY = -rect.top;
        }
        if (rect.bottom < height && isCheckTopBottom) {
            detalY = height - rect.bottom;
        }
        if (rect.left > 0 && isCheckLeftRight) {
            detalX = -rect.left;
        }
        if (rect.right < width && isCheckLeftRight) {
            detalX = width - rect.right;
        }

        mScaleMatrix.postTranslate(detalX, detalY);
    }

    /**
     * 判断是否触发Move
     *
     * @param dx
     * @param dy
     */
    private boolean isMoveAction(float dx, float dy) {

        Debug_AdLog.e("ZoomImageView isMoveAction");
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }


    public void initImageView() {

        Debug_AdLog.e("ZoomImageView initImageView");
        int width = getWidth();
        int height = getHeight();

        Drawable d = getDrawable();
        if (d == null) {
            return;
        }
        float scale = 1.0f;
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();

        if (w > width && h < height) {
            scale = width * 1.0f / w;
        }

        if (w < width && h > height) {
            scale = height * 1.0f / h;
        }

        if ((w > width && h > height) || (w < width && h < height)) {
            scale = Math.min(width * 1.0f / w, height * 1.0f / h);
        }

        mInitScale = scale;

        int dx = getWidth() / 2 - w / 2;
        int dy = getHeight() / 2 - h / 2;

        mScaleMatrix.postTranslate(dx, dy);
        mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
        setImageMatrix(mScaleMatrix);
    }
}