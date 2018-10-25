package com.jersay.foldlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangjie on 2018/10/24.
 */
public class FoldLayout extends ViewGroup {

    private static final int NUM_OF_POINT = 8;
    private float mTranslateDis;//图片折叠后的总宽度
    protected float mFactor = 0.6f;//折叠后的宽度与原宽度的比例
    private int mNumOfFolds = 8;//折叠的块数

    private Bitmap mBitmap;
    private Canvas mCanvas = new Canvas();
    private boolean isReady;

    private Matrix[] mMatrices = new Matrix[mNumOfFolds];
    private Paint mSolidPaint;//黑色透明区域

    private Paint mShadowPaint;//阴影部分
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowLinearGradient;

    private float mFlodwidth;//原图每块的宽度
    private float mTrannslateDisPerFlod;//折叠时每块的宽度

    private float mAnchor = 0;

    public FoldLayout(Context context) {
        this(context, null);
    }

    /**
     * 将需要初始化的东西，不依赖于宽度的，比如画笔以、渐变、矩阵等初始化放在构造方法中
     */
    public FoldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i] = new Matrix();
        }
        mSolidPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowLinearGradient = new LinearGradient(0, 0, 0.5f, 0,
                Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowLinearGradient);
        mShadowGradientMatrix = new Matrix();
        this.setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
    }

    /**
     * 依赖宽高的，放在onLayout()之后
     */
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
        updateFold();
    }

    /**
     * 初始化相关数据
     */
    private void updateFold() {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        mTranslateDis = w * mFactor;
        mFlodwidth = w / mNumOfFolds;
        mTrannslateDisPerFlod = mTranslateDis / mNumOfFolds;

        int alpha = (int) (255 * (1 - mFactor));
        mSolidPaint.setColor(Color.argb((int) (alpha * 0.8f), 0, 0, 0));

        mShadowGradientMatrix.setScale(mFlodwidth, 1);
        mShadowLinearGradient.setLocalMatrix(mShadowGradientMatrix);
        mShadowPaint.setAlpha(alpha);

        float depth = (float) (Math.sqrt(mFlodwidth * mFlodwidth -
                mTrannslateDisPerFlod * mTrannslateDisPerFlod) / 2);
        float anchorPoint = mAnchor * w;
        float midFold = anchorPoint / mFlodwidth;

        float[] src = new float[NUM_OF_POINT];
        float[] dst = new float[NUM_OF_POINT];

        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i].reset();
            src[0] = i * mFlodwidth;
            src[1] = 0;
            src[2] = src[0] + mFlodwidth;
            src[3] = 0;
            src[4] = src[2];
            src[5] = h;
            src[6] = src[0];
            src[7] = src[5];

            boolean isEven = i % 2 == 0;
            dst[0] = i * mTrannslateDisPerFlod;
            dst[1] = isEven ? 0 : depth;
            dst[2] = dst[0] + mTrannslateDisPerFlod;
            //引入anchor
            dst[0] = (anchorPoint > i * mFlodwidth) ? anchorPoint
                    + (i - midFold) * mTrannslateDisPerFlod : anchorPoint
                    - (midFold - i) * mTrannslateDisPerFlod;
            dst[2] = (anchorPoint > (i + 1) * mFlodwidth) ? anchorPoint
                    + (i + 1 - midFold) * mTrannslateDisPerFlod : anchorPoint
                    - (midFold - i - 1) * mTrannslateDisPerFlod;
            dst[3] = isEven ? depth : 0;
            dst[4] = dst[2];
            dst[5] = isEven ? h - depth : h;
            dst[6] = dst[0];
            dst[7] = isEven ? h : h - depth;

            for (int y = 0; y < 8; y++) {
                dst[y] = Math.round(dst[y]);
            }

            mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mFactor == 0) {//mFactor为0表示全折叠
            return;
        }
        if (mFactor == 1) {//mFactor为1表示不折叠，直接调用super.dispatchDraw(canvas);
            super.dispatchDraw(canvas);
            return;
        }
        for (int i = 0; i < mNumOfFolds; i++) {
            canvas.save();

            canvas.concat(mMatrices[i]);
            canvas.clipRect(mFlodwidth * i, 0, mFlodwidth * i + mFlodwidth, getHeight());
            if (isReady) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            } else {//第一次绘制
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                isReady = true;
            }
            canvas.translate(mFlodwidth * i, 0);
            if (i % 2 == 0) {
                canvas.drawRect(0, 0, mFlodwidth, getHeight(), mSolidPaint);
            } else {
                canvas.drawRect(0, 0, mFlodwidth, getHeight(), mShadowPaint);
            }
            canvas.restore();
        }
    }

    public float getFactor() {
        return mFactor;
    }

    public void setFactor(float factor) {
        this.mFactor = factor;
        updateFold();
        invalidate();
    }

    public void setAnchor(float anchor) {
        this.mAnchor = anchor;
        updateFold();
        invalidate();
    }
}
