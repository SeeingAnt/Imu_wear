package org.ros.android.android_wear_pub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;

import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.ros.android.RosActivity;
import org.ros.android.android_wear_pub.Speaker;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import java.util.concurrent.Semaphore;

import java.util.Vector;

public class SendingActivity extends RosActivity implements SensorEventListener {

    public SendingActivity() {
        super("IMU Wear", "IMU Wear", URI.create("http://192.168.43.51:11311"));
    }

    private Speaker pub=new Speaker("imu_data");

    private TextView dataAcc, dataGyro;

    private String deviceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        // Enables Always-on
        setAmbientEnabled();

        Intent intent = getIntent();
        deviceName = intent.getStringExtra(MainActivity.deviceNamePath);

        TextView TextDevName;
        TextDevName = findViewById(R.id.deviceName);
        TextDevName.setText(deviceName);

        dataAcc = findViewById(R.id.acc);
        dataGyro = findViewById(R.id.gyro);
    }

    /** Function triggered when new sensors data should be processed
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        /** If the new data is from the gyroscope display it and push it in the gyroscope data
         *  container (gyroMsg)
         */
            if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    for(int i=0;i<3;i++)
                    {pub.r[i]=sensorEvent.values[i];}

                dataGyro.setText("gyro: " + pub.r[0] + " " + pub.r[1] + " " + pub.r[2]);
            }

            /** If the new data is from the accelerometer display it and push it in the accelerometer
             *  data container (gyroAcc)
             */
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                for(int i=0;i<3;i++)
                {pub.r1[i]=sensorEvent.values[i];}
                dataAcc.setText("acc: " + pub.r1[0] + " " + pub.r1[1] + " " + pub.r[2]);
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void stopStreaming(View view) {
        pub.senSensorManager.unregisterListener(this);
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        //listener = new Listener();

        pub.senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        pub.senAccelerometer = pub.senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pub.senSensorManager.registerListener(this, pub.senAccelerometer , SensorManager.SENSOR_DELAY_GAME);

        pub.senGyroscope = pub.senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        pub.senSensorManager.registerListener(this, pub.senGyroscope , SensorManager.SENSOR_DELAY_GAME);


        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeConfiguration.setNodeName("IMU");
        nodeMainExecutor.execute(pub, nodeConfiguration);


    }
}
