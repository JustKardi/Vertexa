#version 330 core

const float PI = 3.14159265359;

struct DirectionalLight {
    vec3 direction;
    vec3 color;
    float intensity;
};

uniform DirectionalLight uSun;
uniform vec3 uCameraPos;
uniform float uRoughness;
uniform float uMetallic;
uniform float uReflectance;

in vec3 fColor;
in vec3 fNormal;
in vec3 fFragPos;

out vec4 FragColor;

float D_GGX(vec3 N, vec3 H, float roughness) {
    float a = roughness * roughness;
    float a2 = a * a;
    float NdotH = max(dot(N, H), 0.0);
    float denom = (NdotH * NdotH * (a2 - 1.0) + 1.0);
    return a2 / (PI * denom * denom);
}

float G_SchlickGGX(float NdotV, float roughness) {
    float r = roughness + 1.0;
    float k = (r * r) / 8.0;
    return NdotV / (NdotV * (1.0 - k) + k);
}

float G_Smith(vec3 N, vec3 V, vec3 L, float roughness) {
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    return G_SchlickGGX(NdotV, roughness) * G_SchlickGGX(NdotL, roughness);
}

vec3 F_Schlick(float cosTheta, vec3 F0) {
    return F0 + (1.0 - F0) * pow(clamp(1.0 - cosTheta, 0.0, 1.0), 5.0);
}

void main() {
    vec3 albedo = fColor;
    vec3 N = normalize(fNormal);
    vec3 V = normalize(uCameraPos - fFragPos);
    vec3 L = normalize(-uSun.direction);
    vec3 H = normalize(V + L);

    vec3 F0 = vec3(uReflectance);
    F0 = mix(F0, albedo, uMetallic);

    float D = D_GGX(N, H, uRoughness);
    float G = G_Smith(N, V, L, uRoughness);
    vec3  F = F_Schlick(max(dot(H, V), 0.0), F0);

    vec3 ks = F;
    vec3 kd = (vec3(1.0) - ks) * (1.0 - uMetallic);

    float NdotL = max(dot(N, L), 0.0);
    float NdotV = max(dot(N, V), 0.0);

    vec3 specular = (D * G * F) / max(4.0 * NdotV * NdotL, 0.001);
    vec3 diffuse  = kd * albedo / PI;

    vec3 radiance = uSun.color * uSun.intensity;
    vec3 Lo = (diffuse + specular) * radiance * NdotL;

    vec3 ambient = vec3(0.03) * albedo;

    FragColor = vec4(ambient + Lo, 1.0);
}