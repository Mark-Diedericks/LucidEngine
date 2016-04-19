#version 120
import lighting.fsh;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos)
{
	return CalcPointLight(R_pointLight, normal, worldPos);
}

vec4 CalcColor() {
	return vec4(R_pointLight.base.color * R_pointLight.base.intensity, 1.0);
}

import lightingMain.fsh;
