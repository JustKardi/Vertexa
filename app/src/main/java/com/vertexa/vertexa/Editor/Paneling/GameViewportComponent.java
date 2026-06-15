package com.vertexa.vertexa.Editor.Paneling;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import com.vertexa.vertexa.Cameras.ViewportCamera;
import com.vertexa.vertexa.Core.Engine;
import com.vertexa.vertexa.Editor.Utility.GridRenderer;
import com.vertexa.vertexa.Input.KeyListener;
import com.vertexa.vertexa.Input.MouseListener;
import com.vertexa.vertexa.Renderer.Shader;

public class GameViewportComponent implements IPanelComponent {
    private Shader shader;
    private ViewportCamera camera;
    private GridRenderer gridRenderer;

    public int lfCtrl = GLFW_KEY_LEFT_CONTROL;
    public int lmb = GLFW_MOUSE_BUTTON_1;
    public int mmb = GLFW_MOUSE_BUTTON_MIDDLE;

    private float camSens = 0.3f;
    private float panSpeed = 2f;

    @Override
    public void onInit(DisplayPanel parent) {
        shader = new Shader("/shaders/default/default.vert", "/shaders/default/default.frag");
        camera = new ViewportCamera(0.0f, 1.0f, 5.0f);
        gridRenderer = new GridRenderer();
    }

    @Override
    public void onRender(DisplayPanel parent) {
        shader.use();

        gridRenderer.render(camera, parent.width, parent.height);

        move();

        Engine.get().getCurrentScene().render(shader, camera, parent.width, parent.height);

        shader.detach();
    }

    private void move() {
        if (MouseListener.mouseButtonDown(lmb)) {
            float xOffset = MouseListener.getDx();
            float yOffset = MouseListener.getDy();

            xOffset *= camSens;
            yOffset *= camSens;

            camera.addPY(-yOffset, xOffset);

            if (camera.pitch>89.0f) camera.pitch = 89.0f;
            if (camera.pitch < -89.0f) camera.pitch = -89.0f;
            
            camera.orientation.x = (float) (Math.cos(Math.toRadians(camera.yaw)) * Math.cos(Math.toRadians(camera.pitch)));
            camera.orientation.y = (float) (Math.sin(Math.toRadians(camera.pitch)));
            camera.orientation.z = (float) (Math.sin(Math.toRadians(camera.yaw)) * Math.cos(Math.toRadians(camera.pitch)));

            float length = (float) Math.sqrt(Math.pow(camera.orientation.x, 2) + Math.pow(camera.orientation.y, 2) + Math.pow(camera.orientation.z, 2));

            camera.orientation.x /= length;
            camera.orientation.y /= length;
            camera.orientation.z /= length;
        }

        if ((KeyListener.isKeyPressed(lfCtrl) && MouseListener.mouseButtonDown(lmb)) || MouseListener.mouseButtonDown(mmb)) {
            float xOffset = MouseListener.getDx();
            float yOffset = MouseListener.getDy();

            float rightX = camera.orientation.z * 1.0f - camera.orientation.y * 0.0f;
            float rightY = 0.0f;
            float rightZ = camera.orientation.x * 0.0f - camera.orientation.x * 1.0f;

            float rx = camera.orientation.z;
            float ry = 0.0f;
            float rz = -camera.orientation.x;

            float rLen = (float) Math.sqrt(rx*rx + ry*ry + rz*rz);
            if (rLen > 0) {rx /= rLen; ry /= rLen;}

            float ux = ry * camera.orientation.z - rz * camera.orientation.y;
            float uy = rz * camera.orientation.x - rx * camera.orientation.z;
            float uz = rx * camera.orientation.y - ry * camera.orientation.x;
            
            camera.position.x -= rx * xOffset * panSpeed;
            camera.position.z -= rz * xOffset * panSpeed;
            
            camera.position.x += ux * yOffset * panSpeed;
            camera.position.y += uy * yOffset * panSpeed;
            camera.position.z += uz * yOffset * panSpeed;

        }
    }
}