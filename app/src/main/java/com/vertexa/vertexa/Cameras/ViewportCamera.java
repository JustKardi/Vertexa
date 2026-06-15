package com.vertexa.vertexa.Cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ViewportCamera {
    
    public Vector3f position;
    public Vector3f orientation;

    public float yaw = 0;
    public float pitch = 0;

    public ViewportCamera(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        orientation = new Vector3f(0, 0, 0);
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

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Matrix4f getMatrix() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        Vector3f forward = new Vector3f(
            (float)(Math.cos(pitchRad) * Math.sin(yawRad)),
            (float)(-Math.sin(pitchRad)),
            (float)(-Math.cos(pitchRad) * Math.cos(yawRad))
        );

        Vector3f target = new Vector3f(position).add(forward);

        return new Matrix4f().lookAt(position, target, new Vector3f(0, 1, 0));
    }
}
