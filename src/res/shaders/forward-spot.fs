#version 120
import lighting.fsh;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos)
{
	return CalcSpotLight(R_spotLight, normal, worldPos);
}

vec4 CalcColor() {
	return vec4(R_spotLight.pointLight.base.color * R_spotLight.pointLight.base.intensity, 1.0);
}

import lightingMain.fsh;
