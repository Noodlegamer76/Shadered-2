#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

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
        vec4 color = texelFetch(Sampler0, coord, 0);
        color = vec4(skyblock.x);
        fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
