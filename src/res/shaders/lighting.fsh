varying vec2 texCoord0;
varying vec3 worldPos0;
varying mat3 tbnMatrix;
varying vec4 shadowCoords;

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D R_shadowMap;
uniform float R_shadowVarianceMin;
uniform float R_shadowLightBleedReduction;

import lighting.glh;
