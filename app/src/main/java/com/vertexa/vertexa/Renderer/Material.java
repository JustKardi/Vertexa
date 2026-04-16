package com.vertexa.vertexa.Renderer;

import com.vertexa.vertexa.Utility.Color;

public class Material {
    
    public Color color;
    public float roughness;
    public float metallic;
    public float reflectance;

    public Material(Color color, float roughness, float metallic) {
        this.color = color;
        this.roughness = roughness;
        this.metallic = metallic;
        this.reflectance = 0.5f;
    }

}
