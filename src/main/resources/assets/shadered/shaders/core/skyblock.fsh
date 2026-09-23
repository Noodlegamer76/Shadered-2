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
uniform sampler2D skybox11;
uniform sampler2D skybox12;
uniform sampler2D skybox13;
uniform sampler2D skybox14;
uniform sampler2D skybox15;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
        flat in ivec2 skyblock;

out vec4 fragColor;

void main() {
        ivec2 coord = ivec2(gl_FragCoord.xy);
        vec4 color = vec4(0.0);

        switch (skyblock.x) {
                case 0: color = texelFetch(skybox0, coord, 0); break;
                case 1: color = texelFetch(skybox1, coord, 0); break;
                case 2: color = texelFetch(skybox2, coord, 0); break;
                case 3: color = texelFetch(skybox3, coord, 0); break;
                case 4: color = texelFetch(skybox4, coord, 0); break;
                case 5: color = texelFetch(skybox5, coord, 0); break;
                case 6: color = texelFetch(skybox6, coord, 0); break;
                case 7: color = texelFetch(skybox7, coord, 0); break;
                case 8: color = texelFetch(skybox8, coord, 0); break;
                case 9: color = texelFetch(skybox9, coord, 0); break;
                case 10: color = texelFetch(skybox10, coord, 0); break;
                case 11: color = texelFetch(skybox11, coord, 0); break;
                case 12: color = texelFetch(skybox12, coord, 0); break;
                case 13: color = texelFetch(skybox13, coord, 0); break;
                case 14: color = texelFetch(skybox14, coord, 0); break;
                case 15: color = texelFetch(skybox15, coord, 0); break;
                default: color = vec4(float(skyblock.x) / 255, 0.0, 0.0, 1.0); break;
        }

        fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
