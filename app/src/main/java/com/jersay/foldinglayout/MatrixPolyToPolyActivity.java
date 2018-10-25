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

public class MatrixPolyToPolyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PolyToPolyView(this));
    }

    class PolyToPolyView extends View {

        //图片
        private Bitmap mBitmap;
        private Matrix mMatrix;

        //阴影
        private Paint mShadowPaint;
        private Matrix mShadowGradientMatrix;
        private LinearGradient mShadowGradientSahder;

        public PolyToPolyView(Context context) {
            super(context);
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
            mMatrix = new Matrix();

            mShadowPaint = new Paint();
            mShadowPaint.setStyle(Paint.Style.FILL);
            mShadowGradientSahder = new LinearGradient(0, 0, 0.5f, 0,
                    Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mShadowGradientSahder);

            mShadowGradientMatrix = new Matrix();
            mShadowGradientMatrix.setScale(mBitmap.getWidth(), 1);
            mShadowGradientSahder.setLocalMatrix(mShadowGradientMatrix);
            mShadowPaint.setAlpha((int) (0.9 * 255));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            float[] src = {0, 0,
                    mBitmap.getWidth(), 0,
                    mBitmap.getWidth(), mBitmap.getHeight(),
                    0, mBitmap.getHeight()};
            float[] dst = {0, 0,
                    mBitmap.getWidth(), 200,
                    mBitmap.getWidth(), mBitmap.getHeight() - 200,
                    0, mBitmap.getHeight()};
            mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
            canvas.concat(mMatrix);
            canvas.drawBitmap(mBitmap, mMatrix, null);
            canvas.drawRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),mShadowPaint);
            canvas.restore();
        }
    }
}
