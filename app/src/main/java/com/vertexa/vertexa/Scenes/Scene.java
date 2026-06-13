package com.vertexa.vertexa.Scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.vertexa.vertexa.Cameras.ViewportCamera;
import com.vertexa.vertexa.Environment.DirectionalLight;
import com.vertexa.vertexa.Objects.GameObject;
import com.vertexa.vertexa.Renderer.Shader;

public class Scene {
    private List<GameObject> gameObjects = new ArrayList<>();
    private DirectionalLight sunLight;

    public Scene() {
        this.sunLight = new DirectionalLight(
            new Vector3f(0.3f, -0.5f, -1.0f).normalize(),
            new Vector3f(1.0f, 1.0f, 0.9f),
            10.0f
        );
    }

    public void update(float dt) { }

    public void render(Shader shader, ViewportCamera camera, float panelWidth, float panelHeight) {
        float aspect = panelHeight > 0 ? panelWidth / panelHeight : 1.0f;
        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), aspect, 0.1f, 100.0f);
        Matrix4f view = camera.getMatrix();

        shader.setUniform("uSun", sunLight);
        shader.uploadVec3f("uCameraPos", camera.position);

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

    public DirectionalLight getSunLight() {
        return sunLight;
    }
}