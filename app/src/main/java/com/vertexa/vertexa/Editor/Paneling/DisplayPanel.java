package com.vertexa.vertexa.Editor.Paneling;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import org.lwjgl.opengl.GL30;

import com.vertexa.vertexa.Renderer.Framebuffer;

public class DisplayPanel {
    public String title;
    public float x, y, width, height;
    public boolean isDragging = false;

    private Framebuffer framebuffer;
    private IPanelComponent component;

    public DisplayPanel(String title, float x, float y, float width, float height, IPanelComponent component) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.component = component;

        this.framebuffer = new Framebuffer((int) width, (int) height);
        this.component.onInit(this);
    }

    public void resizeFramebuffer(int w, int h) {
        if (w != this.width || h != this.height) {
            this.width = w;
            this.height = h;
            this.framebuffer = new Framebuffer(w, h);
        }
    }

    public void updateAndRender() {
        framebuffer.bind();
        GL11.glViewport(0, 0, (int)width, (int)height);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        component.onRender(this);
        framebuffer.unbind();
    }

    public int getTextureID() {
        return framebuffer.getTextureID();
    }

    public boolean checkHeaderHover(double mx, double my, float headerH) {
        return mx >= x && mx <= x + width && my >= y && my <= y + headerH;
    }
}
