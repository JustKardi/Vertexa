package com.vertexa.vertexa.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    
    private static boolean[] keyPressed = new boolean[350];

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < keyPressed.length) {
            if (action == GLFW_PRESS) {
                keyPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                keyPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

}
