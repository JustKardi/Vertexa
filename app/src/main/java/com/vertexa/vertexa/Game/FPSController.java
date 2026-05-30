package com.vertexa.vertexa.Game;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import com.vertexa.vertexa.FPSCamera;
import com.vertexa.vertexa.Input.KeyListener;
import com.vertexa.vertexa.Input.MouseListener;
import com.vertexa.vertexa.Objects.GameObject;

public class FPSController {
    private float walkSpeed = 5.0f;
    private float runSpeed = 10.0f;
    private GameObject player;
    private FPSCamera camera;
    private float mouseSensitivity = 15.0f;

    public FPSController(GameObject player, FPSCamera camera) {
        this.player = player;
        this.camera = camera;
    }

    public void update(float dt, boolean isViewportHovered, boolean isViewportFocused) {
        if (isViewportHovered && MouseListener.mouseButtonDown(1)) {
            float dx = MouseListener.getDx() * mouseSensitivity * dt;
            float dy = MouseListener.getDy() * mouseSensitivity * dt;
            camera.addPY(-dy, dx);
        }

        float yawRad = (float)Math.toRadians(camera.getYaw());
        float pitchRad = (float)Math.toRadians(camera.getPitch());
        
        Vector3f forward = new Vector3f(
            (float)(Math.cos(pitchRad) * Math.sin(yawRad)),
            (float)(-Math.sin(pitchRad)),
            (float)(-Math.cos(pitchRad) * Math.cos(yawRad))
        ).normalize();
        
        Vector3f right = new Vector3f(forward).cross(new Vector3f(0, 1, 0)).normalize();

        if (isViewportFocused) {
            float currentSpeed = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? runSpeed : walkSpeed;
            float moveDist = currentSpeed * dt;

            if (KeyListener.isKeyPressed(GLFW_KEY_W)) camera.position.add(new Vector3f(forward).mul(moveDist));
            if (KeyListener.isKeyPressed(GLFW_KEY_S)) camera.position.sub(new Vector3f(forward).mul(moveDist));
            if (KeyListener.isKeyPressed(GLFW_KEY_D)) camera.position.add(new Vector3f(right).mul(moveDist));
            if (KeyListener.isKeyPressed(GLFW_KEY_A)) camera.position.sub(new Vector3f(right).mul(moveDist));
        }
    }
}
