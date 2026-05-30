package com.vertexa.vertexa.Editor;

import com.vertexa.vertexa.Window;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDir;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImInt;

public class ImGuiLayer {
    private final ImGuiImplGlfw imGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGl3 = new ImGuiImplGl3();

    public void init() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable); 

        long nativeWindow = Window.get().getNativeWindow();
        imGlfw.init(nativeWindow, true);
        imGl3.init("#version 330 core");

        applyRoyalPurpleTheme();
    }

    public void render(PanelLayoutManager layoutManager) {
        imGlfw.newFrame();
        ImGui.newFrame();

        int windowFlags = ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar |
                        ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus |
                        ImGuiWindowFlags.NoNavFocus;

        ImGui.setNextWindowPos(0.0f, 0.0f);
        ImGui.setNextWindowSize(ImGui.getMainViewport().getSizeX(), ImGui.getMainViewport().getSizeY());
        
        ImGui.begin("MasterDockspaceWindow", windowFlags);
        int dockspaceId = ImGui.getID("MainViewportDockspace");
        ImGui.dockSpace(dockspaceId);

        if (imgui.internal.ImGui.dockBuilderGetNode(dockspaceId) == null) {
            imgui.internal.ImGui.dockBuilderRemoveNode(dockspaceId);
            imgui.internal.ImGui.dockBuilderAddNode(dockspaceId);
            imgui.internal.ImGui.dockBuilderSetNodeSize(dockspaceId, ImGui.getMainViewport().getSizeX(), ImGui.getMainViewport().getSizeY());

            ImInt centerNode = new ImInt(dockspaceId);
            ImInt leftNode = new ImInt();
            ImInt rightNode = new ImInt();
            ImInt bottomNode = new ImInt();

            imgui.internal.ImGui.dockBuilderSplitNode(centerNode.get(), ImGuiDir.Left, 0.20f, leftNode, centerNode);
            imgui.internal.ImGui.dockBuilderSplitNode(centerNode.get(), ImGuiDir.Right, 0.25f, rightNode, centerNode);
            imgui.internal.ImGui.dockBuilderSplitNode(centerNode.get(), ImGuiDir.Down, 0.30f, bottomNode, centerNode);

            imgui.internal.ImGui.dockBuilderFinish(dockspaceId);
        }
        ImGui.end();

        // org.lwjgl.opengl.GL30.glBindVertexArray(0);

        for (DisplayPanel panel : layoutManager.getPanels()) {
            ImGui.begin(panel.title);
            
            float width = ImGui.getContentRegionAvailX();
            float height = ImGui.getContentRegionAvailY();
            panel.resizeFramebuffer((int)width, (int)height);
            panel.updateAndRender();
            ImGui.image(panel.getTextureID(), width, height, 0, 1, 1, 0);
            
            ImGui.end();
        }

        ImGui.render();
        imGl3.renderDrawData(ImGui.getDrawData());
    }

    private void applyRoyalPurpleTheme() {
        ImGui.getStyle().setColor(ImGuiCol.WindowBg, 0.07f, 0.04f, 0.12f, 1.0f);     
        ImGui.getStyle().setColor(ImGuiCol.Header, 0.35f, 0.12f, 0.55f, 1.0f);       
        ImGui.getStyle().setColor(ImGuiCol.HeaderHovered, 0.48f, 0.16f, 0.72f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.HeaderActive, 0.24f, 0.07f, 0.38f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.TitleBg, 0.12f, 0.06f, 0.20f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.TitleBgActive, 0.22f, 0.08f, 0.36f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.Tab, 0.15f, 0.07f, 0.25f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.TabHovered, 0.40f, 0.15f, 0.60f, 1.0f);
        ImGui.getStyle().setColor(ImGuiCol.TabActive, 0.30f, 0.10f, 0.48f, 1.0f);
        
        ImGui.getStyle().setWindowRounding(6.0f);
        ImGui.getStyle().setFrameRounding(4.0f);
    }

    public void destroy() {
        imGl3.dispose();
        imGlfw.dispose();
        ImGui.destroyContext();
    }
}
