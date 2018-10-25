package com.jersay.foldinglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 简单讲解下，不去管绘制阴影的部分，其实折叠就是：
 * 1、初始化转换点，这里注释说的很清楚，大家最好在纸上绘制下，标一下每个变量。
 * <p>
 * 2、为matrix.setPolyToPoly
 * <p>
 * 3、绘制时使用该matrix，且clipRect控制显示区域（这个区域也很简单，原图的第一块到最后一块），最好就是绘制bitmap了。
 */
public class SimpleUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PolyToPolyView(this));
    }

    class PolyToPolyView extends View {

        private static final int NUM_OF_POINT = 8;
        private int mTranslateDis;//图片折叠后的总宽度
        private float mFactor = 0.8f;//折叠后的总宽度与原图宽度的比例
        private int mNumOfFolds = 8;//折叠块的个数
        private Matrix[] mMatrices = new Matrix[mNumOfFolds];
        private Bitmap mBitmap;
        private Paint mSolidPaint;//绘制黑色透明区域
        /*
        绘制阴影
         */
        private Paint mShadowPaint;
        private Matrix mShadowGradientMatrix;
        private LinearGradient mShadowFradientSahder;
        private int mFlodWidth;//原图每块的宽度
        private int mTranslateDisPerFlod;//折叠时每块的宽度

        public PolyToPolyView(Context context) {
            super(context);
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
            //折叠后的总宽度
            mTranslateDis = (int) (mBitmap.getWidth() * mFactor);
            //原图每块的宽度
            mFlodWidth = mBitmap.getWidth() / mNumOfFolds;
            //折叠时每块的宽度
            mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

            //初始化Matrix
            for (int i = 0; i < mNumOfFolds; i++) {
                mMatrices[i] = new Matrix();
            }

            mSolidPaint = new Paint();
            int alpha = (int) (255 * mFactor * 0.8f);
            mSolidPaint.setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));
            mShadowPaint = new Paint();
            mShadowPaint.setStyle(Paint.Style.FILL);
            mShadowFradientSahder = new LinearGradient(0, 0, 0.5f, 0,
                    Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mShadowFradientSahder);

            mShadowGradientMatrix = new Matrix();
            mShadowGradientMatrix.setScale(mFlodWidth, 1);
            mShadowFradientSahder.setLocalMatrix(mShadowGradientMatrix);
            mShadowPaint.setAlpha(alpha);

            //纵轴减小的高度,勾股定理计算
            int depth = (int) Math.sqrt(mFlodWidth * mFlodWidth - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2;
            //转换点
            float[] src = new float[NUM_OF_POINT];
            float[] dst = new float[NUM_OF_POINT];

            //原图的每一块，对应折叠后的每一块，左上、右上、右下、左下
            for (int i = 0; i < mNumOfFolds; i++) {
                src[0] = i * mFlodWidth;
                src[1] = 0;
                src[2] = src[0] + mFlodWidth;
                src[3] = 0;
                src[4] = src[2];
                src[5] = mBitmap.getHeight();
                src[6] = src[0];
                src[7] = src[5];

                boolean isEven = i % 2 == 0;

                dst[0] = i * mTranslateDisPerFlod;
                dst[1] = isEven ? 0 : depth;
                dst[2] = dst[0] + mTranslateDisPerFlod;
                dst[3] = isEven ? depth : 0;
                dst[4] = dst[2];
                dst[5] = isEven ? mBitmap.getHeight() - depth : mBitmap
                        .getHeight();
                dst[6] = dst[0];
                dst[7] = isEven ? mBitmap.getHeight() : mBitmap.getHeight()
                        - depth;

                mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //绘制mNumOfFolds次
            for (int i = 0; i < mNumOfFolds; i++) {
                canvas.save();
                //将matrix应用到canvas
                canvas.concat(mMatrices[i]);
                //控制显示的大小
                canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth, mBitmap.getHeight());
                //绘制图片
                canvas.drawBitmap(mBitmap, 0, 0, null);
                //移动绘制相关
                canvas.translate(mFlodWidth * i, 0);
                if (i % 2 == 0) {
                    //绘制黑色遮盖
                    canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mSolidPaint);
                } else {
                    //绘制阴影
                    canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mShadowPaint);
                }
                canvas.restore();
            }
        }
    }
}
