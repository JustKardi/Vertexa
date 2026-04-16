package com.vertexa.vertexa;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.vertexa.vertexa.Environment.DirectionalLight;
import com.vertexa.vertexa.Game.FPSController;
import com.vertexa.vertexa.Input.KeyListener;
import com.vertexa.vertexa.Input.MouseListener;
import com.vertexa.vertexa.Meshes.Cube;
import com.vertexa.vertexa.Renderer.Material;
import com.vertexa.vertexa.Renderer.Shader;
import com.vertexa.vertexa.Utility.Color;

public class Window {
    private int width, height, shaderProgram;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    private Cube cube;
    private FPSCamera camera;
    private Shader shader;
    private DirectionalLight sun;
    private float lastFrameTime;
    private FPSController controller;
    private Cube playerCube;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "VERTEXA";
    }

    public static Window get() {
        if (Window.window == null) Window.window = new Window();
        return Window.window;
    }

    public void run() {
        init();
        loop();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        org.lwjgl.glfw.GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);

        org.lwjgl.glfw.GLFW.glfwSetInputMode(glfwWindow, org.lwjgl.glfw.GLFW.GLFW_CURSOR, org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glfwShowWindow(glfwWindow);

        shader = new Shader("/shaders/default.vert", "/shaders/default.frag");
        
        camera = new FPSCamera(0.0f, 0.0f, 3.0f);

        
        playerCube = new Cube(0.4f, 0.4f, 0.4f, 1.0f, 0.0f, -1.0f);
        playerCube.setMaterial(new Material(new Color(0.0f, 1.0f, 0.0f, 1.0f), 0.8f, 0.5f));
        playerCube.generate();

        cube = new Cube(0.4f, 0.4f, 0.4f, -0.2f, -0.2f, -0.2f);
        cube.setMaterial(new Material(new Color(1.0f, 0.5f, 0.0f, 1.0f), 0.4f, 0.0f));
        cube.generate();

        controller = new FPSController(cube, camera, MouseListener.get());

        sun = new DirectionalLight(
            new Vector3f(0.0f, 0.0f, -1.0f),
            new Vector3f(1.0f, 1.0f, 1.0f),
            1.0f
        );

        if (KeyListener.isKeyPressed(org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE)) {
            org.lwjgl.glfw.GLFW.glfwSetInputMode(
                org.lwjgl.glfw.GLFW.glfwGetCurrentContext(),
                org.lwjgl.glfw.GLFW.GLFW_CURSOR,
                org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL
            );
        }

    }


    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            float time = (float)glfwGetTime();
            float dt = time - lastFrameTime;
            lastFrameTime = time;
            controller.update(dt);

            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            int[] w = new int[1], h = new int[1];
            org.lwjgl.glfw.GLFW.glfwGetWindowSize(glfwWindow, w, h);
            org.lwjgl.opengl.GL11.glViewport(0, 0, w[0], h[0]);
            float aspect = (float)w[0] / (float)h[0];

            Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), aspect, 0.1f, 100.0f);
            Matrix4f view = camera.getMatrix();

            shader.use();
            shader.setUniform("uSun", sun);
            shader.uploadVec3f("uCameraPos", camera.position);
            shader.setUniform("uRoughness", 0.4f);
            shader.setUniform("uMetallic", 0.0f);
            shader.setUniform("uReflectance", 0.5f);

            Matrix4f model1 = new Matrix4f().rotate(time, 0, 1, 0);
            Matrix4f mvp1 = new Matrix4f(projection).mul(view).mul(model1);
            shader.uploadMat4f("uMVP", mvp1);
            shader.uploadMat4f("uModel", model1);
            cube.render(shader);

            Matrix4f model2 = new Matrix4f();
            Matrix4f mvp2 = new Matrix4f(projection).mul(view).mul(model2);
            shader.uploadMat4f("uMVP", mvp2);
            shader.uploadMat4f("uModel", model2);
            playerCube.render(shader);

            MouseListener.endFrame();

            glfwSwapBuffers(glfwWindow);
        }
    }

}