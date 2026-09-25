#version 430

#moj_import <fog.glsl>

uniform sampler2D diffuse;
uniform sampler2D normal;
uniform sampler2D pbr;
uniform sampler2D emissive;
uniform sampler2D height;

uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec3 CameraPos;
uniform vec4 ColorModulator;
uniform vec3 LightDirection;
uniform vec3 LightColor;
uniform float Exposure;
uniform float AmbientStrength;

in vec3 v_WorldPos;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;
in vec3 vWorldPos;
in vec3 vNormal;
in vec4 vTangent;

out vec4 fragColor;

void main() {
    vec4 sampledAlbedo = texture(diffuse, texCoord0) * ColorModulator * vertexColor;
    if (sampledAlbedo.a < 0.1) discard;

    sampledAlbedo.rgb += texture(diffuse, texCoord0).rgb * ColorModulator.rgb;

    fragColor = linear_fog(sampledAlbedo, vertexDistance, FogStart, FogEnd, FogColor);
}