package org.ros.android.android_wear_pub;

import android.os.Bundle;
import android.widget.TextView;;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import android.support.wearable.activity.WearableActivity;
import java.net.URI;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends WearableActivity {
    public static final String deviceNamePath = "com.emarolab.carfi.imustream.deviceName";
    String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView TextDevName;
        TextDevName = findViewById(R.id.deviceName);

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

        TextDevName.setText(deviceName);
        setAmbientEnabled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startStreaming(View view) {
        Intent intent = new Intent(this, SendingActivity.class);
        intent.putExtra(deviceNamePath, deviceName);
        finish();
        startActivity(intent);
    }
}

/*
public class MainActivity extends RosActivity
{
    private Speaker talker;
    private RosTextView<std_msgs.String> rosTextView;

    public MainActivity() {
        super("Pubsub Tutorial", "Pubsub Tutorial", URI.create("http://192.168.43.233:11311"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("chatter");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(std_msgs.String message) {
                return message.getData();
            }
        });
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        talker = new Speaker();
        //listener = new Listener();

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());

        nodeConfiguration.setNodeName("talker");
        nodeMainExecutor.execute(talker, nodeConfiguration);

        //nodeConfiguration.setNodeName("listener");
        //nodeMainExecutor.execute(listener, nodeConfiguration);

        //nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }
}

*/