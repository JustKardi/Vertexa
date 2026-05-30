package com.vertexa.vertexa.Scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.vertexa.vertexa.Environment.DirectionalLight;
import com.vertexa.vertexa.FPSCamera;
import com.vertexa.vertexa.Objects.GameObject;
import com.vertexa.vertexa.Renderer.Shader;

public class Scene {
    private List<GameObject> gameObjects = new ArrayList<>();
    private DirectionalLight sunLight;
    private FPSCamera camera = new FPSCamera(2, 1, 5);

    public Scene() {
        this.sunLight = new DirectionalLight(
            new Vector3f(0.3f, -0.5f, -1.0f).normalize(),
            new Vector3f(1.0f, 1.0f, 0.9f),
            10.0f
        );
    }

    public void update(float dt) { }

    public void render(Shader shader, float panelWidth, float panelHeight) {
        float aspect = panelHeight > 0 ? panelWidth / panelHeight : 1.0f;
        
        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), aspect, 0.1f, 100.0f);
        Matrix4f view = camera.getMatrix();

        System.out.println("[CAM] pos=" + camera.position);
        System.out.println("[PROJ] " + projection);
        System.out.println("[VIEW] " + view);

        for (GameObject obj : gameObjects) {
            Matrix4f mvp = new Matrix4f(projection).mul(view).mul(obj.getWorldMatrix());
            Vector4f clip = new Vector4f();
            mvp.getColumn(3, clip);
            System.out.println("[MVP clip w] " + clip.w + " for " + obj.name + " at " + obj.position);
        }

        shader.uploadMat4f("uProjection", projection);
        shader.uploadMat4f("uView", view);
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

    public FPSCamera getCamera() {
        return camera;
    }

    public DirectionalLight getSunLight() {
        return sunLight;
    }
}
