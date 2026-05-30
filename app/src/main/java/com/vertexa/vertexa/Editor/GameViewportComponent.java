package com.vertexa.vertexa.Editor;

import com.vertexa.vertexa.Core.Engine;
import com.vertexa.vertexa.Game.FPSController;
import com.vertexa.vertexa.Objects.GameObject;
import com.vertexa.vertexa.Renderer.Shader;

import imgui.ImGui;

public class GameViewportComponent implements IPanelComponent {
    private Shader shader;
    private FPSController controller;
    private boolean isInitialized = false;

    @Override
    public void onInit(DisplayPanel parent) {
        shader = new Shader("/shaders/default.vert", "/shaders/default.frag");
    }

    @Override
    public void onRender(DisplayPanel parent) {


        Engine engine = Engine.get();
        if (engine.getCurrentScene() == null) {
            System.out.println("[DIAG] No current scene");
            return;
        }
        
        System.out.println("[DIAG] Scene has " + engine.getCurrentScene().getGameObjects().size() + " objects");
        System.out.println("[DIAG] Panel size: " + parent.width + "x" + parent.height);
        System.out.println("[DIAG] FBO texture ID: " + parent.getTextureID());
        
        System.out.println("[DIAG] Shader program ID: " + shader.getShaderProgramID());
        
        for (var go : engine.getCurrentScene().getGameObjects()) {
            System.out.println("[DIAG] GameObject: " + go.name + 
                " pos=" + go.position + 
                " mesh=" + (go.mesh != null ? "yes" : "NULL") +
                " material=" + (go.material != null ? "yes" : "NULL"));
        }

        if (engine.getCurrentScene() != null) {
            
            if (!isInitialized && !engine.getCurrentScene().getGameObjects().isEmpty()) {
                GameObject playerObject = engine.getCurrentScene().getGameObjects().get(0);
                this.controller = new FPSController(playerObject, engine.getCurrentScene().getCamera());
                isInitialized = true;
            }

            boolean isHovered = ImGui.isWindowHovered();
            boolean isFocused = ImGui.isWindowFocused();

            if (controller != null) {
                float dt = engine.getDeltaTime(); 
                controller.update(dt, isHovered, isFocused);
            }

            shader.use();
            
            engine.getCurrentScene().render(shader, parent.width, parent.height);
            
            shader.detach();
        }
    }
}
