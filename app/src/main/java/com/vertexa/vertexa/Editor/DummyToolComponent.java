package com.vertexa.vertexa.Editor;

import static org.lwjgl.opengl.GL11.*;

public class DummyToolComponent implements IPanelComponent {
    private float r, g, b;

    public DummyToolComponent(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override public void onInit(DisplayPanel parent) {}

    @Override
    public void onRender(DisplayPanel parent) {
        glClearColor(r, g, b, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
