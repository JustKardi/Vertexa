package com.vertexa.vertexa.Objects;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.vertexa.vertexa.Meshes.Mesh;
import com.vertexa.vertexa.Renderer.Material;

public class GameObject {
    
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public String name;
    public String tag;

    private GameObject parent = null;
    private List<GameObject> children = new ArrayList<>();

    public Mesh mesh;
    public Material material;

    public GameObject(String name) {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
        this.name = name;
    }

    public void setParent(GameObject parent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            this.parent.children.add(this);
        }
    }

    public Matrix4f getWorldMatrix() {
        Matrix4f matrix = new Matrix4f()
                .translation(position)
                .rotateX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .rotateZ((float)Math.toRadians(rotation.z))
                .scale(scale);

        if (parent != null) {
            return new Matrix4f(parent.getWorldMatrix()).mul(matrix);
        }
        return matrix;
    }

    public List<GameObject> getChildren() {
        return children;
    }

    public void render(com.vertexa.vertexa.Renderer.Shader shader, Matrix4f projection, Matrix4f view) {
        if (mesh == null) return;

        Matrix4f modelMatrix = getWorldMatrix();
        Matrix4f mvp = new Matrix4f(projection).mul(view).mul(modelMatrix);

        shader.uploadMat4f("uModel", modelMatrix);
        shader.uploadMat4f("uMVP", mvp);

        org.joml.Matrix3f normalMatrix = new org.joml.Matrix3f();
        modelMatrix.get3x3(normalMatrix); 
        normalMatrix.invert().transpose();
        shader.uploadMat3f("uNormalMatrix", normalMatrix);

        if (material != null) {
            shader.uploadVec4f("uColor", material.color.toVec4()); 
            shader.setUniform("uRoughness", material.roughness);
            shader.setUniform("uMetallic", material.metallic);
            shader.setUniform("uReflectance", material.reflectance);
        }
        

        mesh.draw();

        for (GameObject child : children) {
            child.render(shader, projection, view);
        }
    }

}
