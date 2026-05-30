package com.vertexa.vertexa.Core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import com.vertexa.vertexa.Editor.ImGuiLayer;
import com.vertexa.vertexa.Editor.PanelLayoutManager;
import com.vertexa.vertexa.Meshes.Mesh;
import com.vertexa.vertexa.Meshes.MeshFactory;
import com.vertexa.vertexa.Objects.GameObject;
import com.vertexa.vertexa.Renderer.Material;
import com.vertexa.vertexa.Renderer.Shader;
import com.vertexa.vertexa.Scenes.Scene;
import com.vertexa.vertexa.Utility.Color;
import com.vertexa.vertexa.Window;

public class Engine {
    private static Engine instance;
    private Window window;
    private Scene currentScene;
    private Shader defaultShader;
    private PanelLayoutManager layoutManager;
    private ImGuiLayer imguiLayer;
    
    private float lastFrameTime = 0.0f;
    private float dt = 0.0f;
    private boolean isPlaying = false;

    private Engine() {
        this.window = Window.get();
    }

    public static Engine get() {
        if (instance == null) instance = new Engine();
        return instance;
    }

    public void start() {
        window.init();
        defaultShader = new Shader("/shaders/default.vert", "/shaders/default.frag");
        
        layoutManager = new PanelLayoutManager();
        imguiLayer = new ImGuiLayer();
        imguiLayer.init();

        currentScene = new Scene();

        MeshFactory factory = new MeshFactory();

        Material cubeMaterial = new Material(new Color(1.0f, 0.0f, 0.0f, 1.0f), 0.4f, 0.10f);
        Mesh cubeMesh = factory.generateCube(cubeMaterial.color);
        GameObject cube = new GameObject("Cube");

        cube.mesh = cubeMesh;
        cube.material = cubeMaterial;

        cube.position.set(0.0f, 0.0f, 0.0f);

        currentScene.addGameObject(cube);

        run();
        imguiLayer.destroy();
    }

    private void run() {
        while (!window.shouldClose()) {
            window.pollEvents();
            
            float time = (float)glfwGetTime();
            this.dt = time - lastFrameTime;
            lastFrameTime = time;

            if (isPlaying) {
                currentScene.update(dt);
            }

            window.clear();

            imguiLayer.render(layoutManager);

            window.swapBuffers();
            com.vertexa.vertexa.Input.MouseListener.endFrame();
        }
    }


    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public float getDeltaTime() {
        return this.dt;
    }
}
