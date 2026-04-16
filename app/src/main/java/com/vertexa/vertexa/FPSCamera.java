package com.vertexa.vertexa;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FPSCamera {
    
    public  Vector3f position;
    private Vector3f orientation;

    private float yaw = 0;
    private float pitch = 0;
    private float lastMouseX = 0;
    private float lastMouseY = 0;

    public FPSCamera(float x, float y, float  z) {
        position = new Vector3f(x, y, z);
        orientation = new Vector3f(0, 1, 0);
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void setLookDir(float x, float y) {
        yaw = x;
        pitch = y;
    }

    public void addPY(float p, float y) {
        pitch += p;
        yaw += y;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Matrix4f getMatrix() {

        float yawRad = (float)Math.toRadians(yaw);
        float pitchRad = (float)Math.toRadians(pitch);

        Vector3f forward = new Vector3f(
            (float)(Math.cos(pitchRad) * Math.sin(yawRad)),
            (float)(-Math.sin(pitchRad)),
            (float)(-Math.cos(pitchRad) * Math.cos(yawRad))
        );

        Vector3f target = new Vector3f(position).add(forward);

        return new Matrix4f().lookAt(position, target, new Vector3f(0, 1, 0));
    }

}