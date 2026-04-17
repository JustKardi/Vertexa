package com.vertexa.vertexa.Scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import com.vertexa.vertexa.FPSCamera;
import com.vertexa.vertexa.Objects.GameObject;
import com.vertexa.vertexa.Renderer.Shader;

public class Scene {
    private List<GameObject> gameObjects = new ArrayList<>();
    private FPSCamera camera = new FPSCamera(0, 0, 10);

    public void update(float dt) {

    }

    public void render(Shader shader) {
        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), 1920f/1080f, 0.1f, 100.0f);
        Matrix4f view = camera.getMatrix();

        for (GameObject obj : gameObjects) {
            obj.render(shader, projection, view);
        }
    }

    public void addGameObject(GameObject go) {
        this.gameObjects.add(go);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public FPSCamera getCamera() {
        return camera;
    }
}
