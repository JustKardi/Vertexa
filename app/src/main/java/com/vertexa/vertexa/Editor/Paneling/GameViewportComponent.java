package com.vertexa.vertexa.Editor.Paneling;

import com.vertexa.vertexa.Cameras.ViewportCamera;
import com.vertexa.vertexa.Core.Engine;
import com.vertexa.vertexa.Editor.Utility.GridRenderer;
import com.vertexa.vertexa.Renderer.Shader;

public class GameViewportComponent implements IPanelComponent {
    private Shader shader;
    private ViewportCamera camera;
    private GridRenderer gridRenderer;

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

        Engine.get().getCurrentScene().render(shader, camera, parent.width, parent.height);

        shader.detach();
    }
}