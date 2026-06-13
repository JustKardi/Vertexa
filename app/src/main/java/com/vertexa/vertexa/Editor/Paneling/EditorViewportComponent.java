package com.vertexa.vertexa.Editor.Paneling;

import com.vertexa.vertexa.Cameras.ViewportCamera;
import com.vertexa.vertexa.Core.Engine;
import com.vertexa.vertexa.Input.MouseListener;
import com.vertexa.vertexa.Renderer.Shader;

import imgui.ImGui;

public class EditorViewportComponent implements IPanelComponent {
    private Shader shader;
    private ViewportCamera camera;
    private boolean isInit = false;

    private final float SENS = 15.0f;

    @Override
    public void onInit(DisplayPanel parent) {
        shader = new Shader("/shaders/default/default.vert", "/shaders/default/default.frag");
        camera = new ViewportCamera(0, 1, 5);
    }

    @Override
    public void onRender(DisplayPanel parent) {
        Engine engine = Engine.get();
        if (engine.getCurrentScene()==null) return;

        float dt = engine.getDeltaTime();
        boolean hovered = ImGui.isWindowHovered();
        boolean focused = ImGui.isWindowFocused();

        update(dt, hovered, focused);

        shader.use();
        engine.getCurrentScene().render(shader, camera, parent.width, parent.height);
        shader.detach();
    }

    public void update(float dt, boolean hovered, boolean focused) {
        if (MouseListener.mouseButtonDown(0) && hovered) {
            float dx = MouseListener.getDx() * SENS * dt;
            float dy = MouseListener.getDy() * SENS * dt;
            camera.addPY(-dy, dx);
        }
    }
}