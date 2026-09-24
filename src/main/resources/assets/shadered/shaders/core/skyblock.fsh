#version 150

#moj_import <fog.glsl>

uniform sampler2D skybox0;
uniform sampler2D skybox1;
uniform sampler2D skybox2;
uniform sampler2D skybox3;
uniform sampler2D skybox4;
uniform sampler2D skybox5;
uniform sampler2D skybox6;
uniform sampler2D skybox7;
uniform sampler2D skybox8;
uniform sampler2D skybox9;
uniform sampler2D skybox10;
uniform sampler2D pixel;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
flat in ivec2 skyblock;

out vec4 fragColor;

void main() {
    vec2 screenUV = gl_FragCoord.xy / ScreenSize;

    vec4 skyboxColor;

    switch (skyblock.x / 16) {
        case 0: skyboxColor = texture(skybox0, screenUV); break;
        case 1: skyboxColor = texture(skybox1, screenUV); break;
        case 2: skyboxColor = texture(skybox2, screenUV); break;
        case 3: skyboxColor = texture(skybox3, screenUV); break;
        case 4: skyboxColor = texture(skybox4, screenUV); break;
        case 5: skyboxColor = texture(skybox5, screenUV); break;
        case 6: skyboxColor = texture(skybox6, screenUV); break;
        case 7: skyboxColor = texture(skybox7, screenUV); break;
        case 8: skyboxColor = texture(skybox8, screenUV); break;
        case 9: skyboxColor = texture(skybox9, screenUV); break;
        case 10: skyboxColor = texture(skybox10, screenUV); break;
        default: skyboxColor = vec4(1.0, 0.0, 1.0, 1.0); break;
    }

    vec4 finalColor = skyboxColor;

    fragColor = linear_fog(finalColor, vertexDistance, FogStart, FogEnd, FogColor);
}