#version 120

varying vec2 texCoords;

uniform sampler2D diffuse;
uniform vec3 R_inverseFilterTextureSize;
uniform float R_fxaaSpanMax;
uniform float R_fxaaReduceMin;
uniform float R_fxaaReduceMul;

void main()
{
	vec3 luma = vec3(0.299, 0.587, 0.114);
	
	float lumaTL = dot(luma, texture2D(diffuse, texCoords.xy + (vec2(-1.0, -1.0) * R_inverseFilterTextureSize.xy)).xyz);
	float lumaTR = dot(luma, texture2D(diffuse, texCoords.xy + (vec2(1.0, -1.0) * R_inverseFilterTextureSize.xy)).xyz);
	float lumaBL = dot(luma, texture2D(diffuse, texCoords.xy + (vec2(-1.0, 1.0) * R_inverseFilterTextureSize.xy)).xyz);
	float lumaBR = dot(luma, texture2D(diffuse, texCoords.xy + (vec2(1.0, 1.0) * R_inverseFilterTextureSize.xy)).xyz);
	float lumaMi = dot(luma, texture2D(diffuse, texCoords.xy).xyz);
	
	vec2 blurDir = vec2(-((lumaTL + lumaTR) - (lumaBL + lumaBR)), ((lumaTL + lumaBL) - (lumaTR + lumaBR)));
	
	float reduceDir = max((lumaTL + lumaTR + lumaBL + lumaBR) * (R_fxaaReduceMul * 0.25), R_fxaaReduceMin);
	float blurDirAdj = 1.0 / (min(abs(blurDir.x), abs(blurDir.y)) + reduceDir);
	blurDir = min(vec2(R_fxaaSpanMax, R_fxaaSpanMax), max(vec2(-R_fxaaSpanMax, -R_fxaaSpanMax), blurDir * blurDirAdj)) * R_inverseFilterTextureSize.xy;
	
	vec3 res1 = (1.0 / 2.0) * (texture2D(diffuse, texCoords.xy + (blurDir.xy * vec2(1.0 / 3.0 - 0.5))).xyz + texture2D(diffuse, texCoords.xy + (blurDir.xy * vec2(2.0 / 3.0 - 0.5))).xyz);
	vec3 res2 = res1 * (1.0 / 2.0) + (1.0 / 4.0) * (texture2D(diffuse, texCoords.xy + (blurDir.xy * vec2(0.0 / 3.0 - 0.5))).xyz + texture2D(diffuse, texCoords.xy + (blurDir.xy * vec2(3.0 / 3.0 - 0.5))).xyz);
	
	float lumaRes2 = dot(luma, res2);
	float lumaMin = min(lumaMi, min(min(lumaTL, lumaTR), min(lumaBL, lumaBR)));
	float lumaMax = max(lumaMi, max(max(lumaTL, lumaTR), max(lumaBL, lumaBR)));
	
	if(lumaRes2 < lumaMin || lumaRes2 > lumaMax) gl_FragColor = vec4(res1, 1.0);
	else gl_FragColor = vec4(res2, 1.0);
}
