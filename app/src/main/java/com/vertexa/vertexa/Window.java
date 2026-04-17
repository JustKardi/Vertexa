package com.vertexa.vertexa;

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
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
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

import com.vertexa.vertexa.Input.KeyListener;
import com.vertexa.vertexa.Input.MouseListener;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;

    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    
    private com.vertexa.vertexa.Objects.GameObject selectedObject = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "VERTEXA ENGINE - Editor";
    }

    public static Window get() {
        if (Window.window == null) Window.window = new Window();
        return Window.window;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) throw new IllegalStateException("Failed to create window.");

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

         ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        applyModernStyle();

        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init("#version 330");
        
        glfwShowWindow(glfwWindow);
    }

    public boolean shouldClose() { return glfwWindowShouldClose(glfwWindow); }
    
    public void pollEvents() { glfwPollEvents(); }
    
    public void swapBuffers() { glfwSwapBuffers(glfwWindow); }

    public void clear() {
        glClearColor(0.10f, 0.04f, 0.18f, 1.0f); 
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderEditorUI(com.vertexa.vertexa.Scenes.Scene currentScene) {

    }

    public void updateEditor(com.vertexa.vertexa.Scenes.Scene currentScene, int sceneTextureId) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(width, height);
        
        ImGui.begin("EditorDockSpace", new ImBoolean(true), windowFlags);
        int dockspaceId = ImGui.getID("MainDockSpace");
        ImGui.dockSpace(dockspaceId);
        
        drawHierarchy(currentScene);
        drawInspector();
        drawViewport(sceneTextureId);

        ImGui.end(); 

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }


    private void drawHierarchy(com.vertexa.vertexa.Scenes.Scene scene) {
        ImGui.begin("Hierarchy");
        for (com.vertexa.vertexa.Objects.GameObject obj : scene.getGameObjects()) {
            if (ImGui.selectable(obj.name, selectedObject == obj)) {
                selectedObject = obj;
            }
        }
        ImGui.end();
    }

    private void drawInspector() {
        ImGui.begin("Inspector");
        if (selectedObject != null) {
            ImGui.text("Object: " + selectedObject.name);
            ImGui.separator();

            float[] pos = {selectedObject.position.x, selectedObject.position.y, selectedObject.position.z};
            if (ImGui.dragFloat3("Position", pos, 0.1f)) {
                selectedObject.position.set(pos[0], pos[1], pos[2]);
            }

            float[] rot = {selectedObject.rotation.x, selectedObject.rotation.y, selectedObject.rotation.z};
            if (ImGui.dragFloat3("Rotation", rot, 0.1f)) {
                selectedObject.rotation.set(rot[0], rot[1], rot[2]);
            }
        } else {
            ImGui.text("Select an object to inspect");
        }
        ImGui.end();
    }

    private void drawViewport(int textureId) {
        ImGui.begin("Viewport");
        
        float windowWidth = ImGui.getContentRegionAvailX();
        float windowHeight = ImGui.getContentRegionAvailY();

        ImGui.image(textureId, windowWidth, windowHeight, 0, 1, 1, 0);
        
        ImGui.end();
    }


    public void renderEditor() {

    }

    private void applyModernStyle() {
        ImGuiStyle style = ImGui.getStyle();
        
        style.setWindowRounding(12.0f);
        style.setFrameRounding(8.0f);
        style.setGrabRounding(8.0f);
        style.setPopupRounding(10.0f);
        style.setScrollbarRounding(12.0f);
        style.setWindowBorderSize(0.0f);
        style.setFramePadding(8.0f, 6.0f);

        style.setColor(ImGuiCol.WindowBg, 0.10f, 0.04f, 0.18f, 0.94f);
        style.setColor(ImGuiCol.Text, 0.88f, 0.84f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.FrameBg, 0.20f, 0.10f, 0.35f, 0.54f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.75f, 0.00f, 1.00f, 0.40f);
        style.setColor(ImGuiCol.FrameBgActive, 0.75f, 0.00f, 1.00f, 0.67f);
        style.setColor(ImGuiCol.Header, 0.75f, 0.00f, 1.00f, 0.31f);
        style.setColor(ImGuiCol.HeaderHovered, 0.75f, 0.00f, 1.00f, 0.80f);
        style.setColor(ImGuiCol.HeaderActive, 0.75f, 0.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.Button, 0.75f, 0.00f, 1.00f, 0.40f);
        style.setColor(ImGuiCol.ButtonHovered, 0.75f, 0.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.SliderGrab, 1.00f, 0.68f, 0.37f, 1.00f);
        style.setColor(ImGuiCol.Tab, 0.15f, 0.08f, 0.25f, 0.86f);
        style.setColor(ImGuiCol.TabActive, 0.50f, 0.00f, 0.80f, 1.00f);
        style.setColor(ImGuiCol.TitleBgActive, 0.10f, 0.04f, 0.18f, 1.00f);
        style.setColor(ImGuiCol.DockingEmptyBg, 0.10f, 0.04f, 0.18f, 1.00f);
    }



    public long getNativeWindow() { return glfwWindow; }
}
