package jp.ac.ritsumei.ise.phy.exp2.is0429rs.compass01;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;    //センサマネージャ
    private Sensor accelerometer;   //加速度センサ
    private Sensor magneticField;   //磁気センサ

    private float[] accelerometerValue = new float[3];  //加速度センサの値
    private float[] magneticFieldValue = new float[3];  //磁気センサの値

    //方向を求める
    static float[] orientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //センサを取り出す
        this.sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magneticField = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override   /*ポーズ*/
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);    //リスナの登録解除
    }

    @Override   /*再開*/
    protected void onResume() {
        super.onResume();
        //リスナの登録
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_UI);
    }

    public void onSensorChanged(SensorEvent event) {
        //センサごとの処理
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: //加速度センサ
                this.accelerometerValue = event.values.clone(); //cloneで配列をコピー
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:    //磁気センサ
                this.magneticFieldValue = event.values.clone(); //先と同様
                break;
        }

        //値が更新済みの角度を取得できる
        if (accelerometerValue != null && magneticFieldValue != null) {
            //方位を出すための変換行列
            float[] rotate = new float[16]; //傾斜行列
            float[] inclination = new float[16];    //回転行列

            //変換行列を作成
            SensorManager.getRotationMatrix(rotate, inclination, accelerometerValue, magneticFieldValue);

            SensorManager.getOrientation(rotate, orientation);


            //デグリー角に変換する
            float degreeDir = (float)Math.floor(Math.toDegrees(orientation[0]));

            if(degreeDir >= 0) {
                orientation[0] = degreeDir;
            } else if(degreeDir < 0) {
                orientation[0] = 360 + degreeDir;
            }

            TextView theta = findViewById(R.id.theta);
            theta.setText("theta: "+ String.valueOf(orientation[0]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}


