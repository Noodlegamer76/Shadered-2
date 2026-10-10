
#version 430

#moj_import <fog.glsl>

uniform sampler2D diffuse;
uniform sampler2D normal;
uniform sampler2D pbr;
uniform sampler2D emissive;
uniform sampler2D height;
uniform sampler2D MinecraftLightTexture;

uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec3 CameraPos;
uniform vec4 ColorModulator;
uniform vec3 LightDirection;
uniform vec3 LightColor;
uniform float Exposure;
uniform float AmbientStrength;
uniform int RenderDistance;

layout(std430, binding = 1) buffer Lights {
    int PageAddress[];
};

in vec3 v_WorldPos;
in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;
in vec3 vWorldPos;
in vec3 vNormal;
in vec4 vTangent;

out vec4 fragColor;

const int SECTION_SIZE = 16;
const int LIGHTS_PER_SECTION = 4096;

int getSectionIndex(ivec3 sectionPos) {
    ivec3 cameraSection = ivec3(floor(CameraPos / 16.0));
    ivec3 offset = sectionPos - cameraSection + ivec3(RenderDistance);

    int length = RenderDistance * 2 + 1;

    if (any(lessThan(offset, ivec3(0)))
        || any(greaterThanEqual(offset, ivec3(length)))) {
        return -1;
    }

    return offset.x + (offset.y * length) + (offset.z * length * length);
}

int getPackedLight(ivec3 blockPos) {
    ivec3 sectionPos = ivec3(floor(vec3(blockPos) / 16.0));
    ivec3 localPos = ivec3(blockPos) - sectionPos * 16;

    int sectionIndex = getSectionIndex(sectionPos);

    if (sectionIndex < 0) {
        return 0;
    }

    int pageAddress = PageAddress[sectionIndex];

    if (pageAddress < 0) {
        return 0;
    }

    int localIndex = localPos.x + localPos.y * SECTION_SIZE + localPos.z * SECTION_SIZE * SECTION_SIZE;

    int byteAddress = pageAddress + localIndex;

    int wordIndex = byteAddress / 4;
    int byteShift = (byteAddress % 4) * 8;

    return int((uint(PageAddress[wordIndex]) >> uint(byteShift)) & 255u);
}

vec2 unpackLight(int packedLight) {
    float blockLight = float(packedLight & 15) / 15.0;
    float skyLight = float((packedLight >> 4) & 15) / 15.0;

    return vec2(blockLight, skyLight);
}

vec3 sampleInterpolatedLight(vec3 worldPos) {
    vec3 p = worldPos - 0.5;
    ivec3 base = ivec3(floor(p));
    vec3 f = fract(p);

    vec2 l000 = unpackLight(getPackedLight(base));
    vec2 l100 = unpackLight(getPackedLight(base + ivec3(1, 0, 0)));
    vec2 l010 = unpackLight(getPackedLight(base + ivec3(0, 1, 0)));
    vec2 l110 = unpackLight(getPackedLight(base + ivec3(1, 1, 0)));
    vec2 l001 = unpackLight(getPackedLight(base + ivec3(0, 0, 1)));
    vec2 l101 = unpackLight(getPackedLight(base + ivec3(1, 0, 1)));
    vec2 l011 = unpackLight(getPackedLight(base + ivec3(0, 1, 1)));
    vec2 l111 = unpackLight(getPackedLight(base + ivec3(1, 1, 1)));

    vec2 x00 = mix(l000, l100, f.x);
    vec2 x10 = mix(l010, l110, f.x);
    vec2 x01 = mix(l001, l101, f.x);
    vec2 x11 = mix(l011, l111, f.x);

    vec2 y0 = mix(x00, x10, f.y);
    vec2 y1 = mix(x01, x11, f.y);

    vec2 lightLevels = mix(y0, y1, f.z);
    vec2 lightUv = (lightLevels * 15.0 + 0.5) / 16.0;

    return texture(MinecraftLightTexture, lightUv).rgb;
}

void main() {
    vec4 sampledAlbedo = texture(diffuse, texCoord0) * ColorModulator * vertexColor;
    vec4 sampledEmissive = texture(emissive, texCoord0);

    if (sampledAlbedo.a < 0.1) {
        discard;
    }

    vec3 light = sampleInterpolatedLight(v_WorldPos);

    vec3 litColor = (sampledAlbedo.rgb * light) + sampledEmissive.rgb;

    fragColor = linear_fog(
            vec4(litColor, sampledAlbedo.a),
            vertexDistance,
            FogStart,
            FogEnd,
            FogColor
    );
}
