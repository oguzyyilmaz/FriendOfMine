package com.fom.msesoft.fomapplication.extras;

import android.app.Dialog;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;


/**
 * Created by jaeger on 27.07.2016.
 */
public class Rotate3dAnimation extends Animation
{
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private Camera mCamera;

    public Rotate3dAnimation(RelativeLayout relativeLayout,int toDegrees) {
        mFromDegrees = 0;
        mToDegrees = toDegrees;
        mCenterX = relativeLayout.getWidth() / 2.0f;
        mCenterY = relativeLayout.getHeight() / 2.0f;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    public void applyPropertiesInRotation()
    {
        this.setDuration(300);
        this.setFillAfter(true);
        this.setInterpolator(new AccelerateInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees
                + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();

        Log.e("Degree",""+degrees) ;
        Log.e("centerX",""+centerX) ;
        Log.e("centerY",""+centerY) ;

        camera.rotateY(degrees);

        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);

    }
}
