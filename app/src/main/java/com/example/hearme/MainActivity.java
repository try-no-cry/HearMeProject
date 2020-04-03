package com.example.hearme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
private static String TAG="MainActivity";

Mat mRGBA,mRGBAT,rectMat;
    JavaCameraView vidCam;
    private TextView caption;

//https://www.tutorialspoint.com/android/android_text_to_speech.htm     Text to speech idhar se
BaseLoaderCallback baseLoaderCallback=new BaseLoaderCallback(MainActivity.this) {
    @Override
    public void onManagerConnected(int status) {

        switch (status)
        {
            case BaseLoaderCallback.SUCCESS:
                    vidCam.enableView();
                    break;

            default:super.onManagerConnected(status);

        }
    }
};



static{

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vidCam=findViewById(R.id.vidCam);
        vidCam.setVisibility(SurfaceView.VISIBLE);

        caption=findViewById(R.id.caption);
        vidCam.setCvCameraViewListener((CameraBridgeViewBase.CvCameraViewListener2) this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
            mRGBA=new Mat(height,width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA=inputFrame.rgba();
//        Rect rect=new Rect(100,100,100,100);
        Point p1=new Point(500,600);
        Point p2=new Point(50,50);

        Imgproc.rectangle(mRGBA,p1,p2, new Scalar(255.0, 0.0, 0.0, 255.0), 5);
        Rect rect=new Rect(p1.x,p1.y,p2.x,p2.y);
        rectMat=mRGBA.submat(rect); //ye apna rectangle hai cropped wala
//        mRGBAT=mRGBA.t();
//        Core.flip(mRGBA.t(),mRGBAT,1);
//        Imgproc.resize(mRGBAT,mRGBAT,mRGBA.size());
        return mRGBA;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(vidCam!=null)
        {
            vidCam.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vidCam!=null)
        {
            vidCam.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG,"OPENCV connected successfully");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }

        else
        {
            Log.d(TAG,"OPENCV not loaded properly");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,MainActivity.this,baseLoaderCallback);
         }
    }
}
