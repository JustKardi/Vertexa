package com.vertexa.vertexa.Core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import com.vertexa.vertexa.Renderer.Framebuffer;
import com.vertexa.vertexa.Renderer.Shader;
import com.vertexa.vertexa.Scenes.Scene;
import com.vertexa.vertexa.Window;

public class Engine {
    private static Engine instance;
    private Window window;
    private Scene currentScene;
    private Shader defaultShader;
    private Framebuffer framebuffer;
    
    private float lastFrameTime = 0.0f;
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
        
        framebuffer = new Framebuffer(1920, 1080);
        
        currentScene = new Scene();
        
        run();
    }

    private void run() {
        while (!window.shouldClose()) {
            window.pollEvents();

            float time = (float)glfwGetTime();
            float dt = time - lastFrameTime;
            lastFrameTime = time;

            if (isPlaying) {
                currentScene.update(dt);
            }

            framebuffer.bind();
            window.clear(); 
            currentScene.render(defaultShader);
            framebuffer.unbind();

            window.clear(); 

            window.updateEditor(currentScene, framebuffer.getTextureID());
            
            window.swapBuffers();
        }
    }

    public void setPlaying(boolean playing) { this.isPlaying = playing; }
    public Scene getCurrentScene() { return currentScene; }
}
