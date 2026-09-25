#version 330 core

#import <sodium:include/fog.glsl>

in vec2 v_TexCoord; // The interpolated block texture coordinates
in float v_FragDistance; // The fragment's distance from the camera

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

in float v_MaterialMipBias;
in float v_MaterialAlphaCutoff;

flat in ivec2 skyblock;

uniform vec4 u_FogColor; // The color of the shader fog
uniform float u_FogStart; // The starting position of the shader fog
uniform float u_FogEnd; // The ending position of the shader fog

out vec4 fragColor; // The output fragment for the color framebuffer

void main() {
    ivec2 coord = ivec2(gl_FragCoord.xy);

    vec4 diffuseColor;

    switch (skyblock.x / 16) {
        case 0: diffuseColor = texelFetch(skybox0, coord, 0); break;
        case 1: diffuseColor = texelFetch(skybox1, coord, 0); break;
        case 2: diffuseColor = texelFetch(skybox2, coord, 0); break;
        case 3: diffuseColor = texelFetch(skybox3, coord, 0); break;
        case 4: diffuseColor = texelFetch(skybox4, coord, 0); break;
        case 5: diffuseColor = texelFetch(skybox5, coord, 0); break;
        case 6: diffuseColor = texelFetch(skybox6, coord, 0); break;
        case 7: diffuseColor = texelFetch(skybox7, coord, 0); break;
        case 8: diffuseColor = texelFetch(skybox8, coord, 0); break;
        case 9: diffuseColor = texelFetch(skybox9, coord, 0); break;
        case 10: diffuseColor = texelFetch(skybox10, coord, 0); break;
        default: diffuseColor = vec4(1.0, 0.0, 1.0, 1.0); break;
    }

    #ifdef USE_FRAGMENT_DISCARD
    if (diffuseColor.a < v_MaterialAlphaCutoff) {
        discard;
    }
    #endif

    fragColor = _linearFog(diffuseColor, v_FragDistance, u_FogColor, u_FogStart, u_FogEnd);
}