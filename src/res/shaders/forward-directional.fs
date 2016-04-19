#version 120
import lighting.fsh;

uniform DirectionalLight R_directionalLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos)
{
	return CalcDirectionalLight(R_directionalLight, normal, worldPos);
}

vec4 CalcColor() {
	return vec4(R_directionalLight.base.color * R_directionalLight.base.intensity, 1.0);
}

import lightingMain.fsh;
