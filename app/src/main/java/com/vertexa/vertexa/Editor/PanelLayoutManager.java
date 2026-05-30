package com.vertexa.vertexa.Editor;

import java.util.ArrayList;
import java.util.List;

public class PanelLayoutManager {
    private final List<DisplayPanel> panels = new ArrayList<>();
    private final float HEADER_HEIGHT = 30.0f;

    public PanelLayoutManager() {
        panels.add(new DisplayPanel("Engine ViewPort", 50, 50, 960, 540, new GameViewportComponent()));
        panels.add(new DisplayPanel("Scene Heirarchy", 1040, 50, 400, 540, new DummyToolComponent(0.15f, 0.15f, 0.18f)));
        panels.add(new DisplayPanel("Asset Browser", 50, 620, 960, 360, new DummyToolComponent(0.12f, 0.12f, 0.14f)));
        panels.add(new DisplayPanel("Inspector Workspace", 1040, 620, 400, 360, new DummyToolComponent(0.18f, 0.12f, 0.12f)));
    }

    public void spawnCustomDisplay(String title, IPanelComponent component) {
        panels.add(new DisplayPanel(title, 200, 200, 400, 300, component));
    }

    public List<DisplayPanel> getPanels() {
        return panels;
    }
}
