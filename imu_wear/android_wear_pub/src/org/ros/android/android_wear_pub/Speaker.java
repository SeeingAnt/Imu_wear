package org.ros.android.android_wear_pub;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import org.ros.concurrent.CancellableLoop;
import org.ros.internal.message.RawMessage;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import java.util.concurrent.Semaphore;

import geometry_msgs.Vector3;
import sensor_msgs.Imu;

public class Speaker extends AbstractNodeMain {
    private String topic_name;

    public Semaphore semaphore = new Semaphore(1);
    public geometry_msgs.Vector3 acc;
    public geometry_msgs.Vector3 vel;
    public sensor_msgs.Imu imu;
    public SensorManager senSensorManager;
    public Sensor senAccelerometer, senGyroscope;
    public float[] r= new float[3];
    public float[] r1= new float[3];


    public Speaker() {
        this.topic_name = "chatter";
    }

    public Speaker(String topic) {
        this.topic_name = topic;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("android_tutorial_pubsub/talker");
    }

    public void onStart(ConnectedNode connectedNode) {
        final Publisher<geometry_msgs.Vector3> publisher1 = connectedNode.newPublisher(this.topic_name, Vector3._TYPE);
        final Publisher<sensor_msgs.Imu> publisher = connectedNode.newPublisher(this.topic_name, sensor_msgs.Imu._TYPE);
        vel= publisher1.newMessage();
        acc= publisher1.newMessage();

        connectedNode.executeCancellableLoop(new CancellableLoop() {


            protected void setup() {

            }

            protected void loop() throws InterruptedException {
                imu=(sensor_msgs.Imu)publisher.newMessage();

                vel.setX(r[0]);
                vel.setY(r[1]);
                vel.setZ(r[2]);

                acc.setX(r1[0]);
                acc.setY(r1[1]);
                acc.setZ(r1[2]);

                imu.setAngularVelocity(vel);
                imu.setLinearAcceleration(acc);
                imu.toRawMessage();

                publisher.publish(imu);

                Thread.sleep(10000L);
            }
        });
    }
}
