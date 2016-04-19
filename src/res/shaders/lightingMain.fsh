import sampler.glh;

bool inRange(float value) {
	return value >= 0.0 && value <= 1.0;
}

float calcShadowIntensity(sampler2D shadowMapTexture, vec4 shadowCoord) {
	vec3 shadowMapCoords = (shadowCoord.xyz / shadowCoord.w) * vec3(0.5) + vec3(0.5);
	
	if(inRange(shadowMapCoords.x) && inRange(shadowMapCoords.y) && inRange(shadowMapCoords.z)) {
		return sampleVarianceShadowMap(texture2D(shadowMapTexture, shadowMapCoords.xy), shadowMapCoords.xy, shadowMapCoords.z, R_shadowVarianceMin, R_shadowLightBleedReduction);
	} else {
		return 1.0;
	}
}

void main() {
	vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture2D(normalMap, texCoord0.xy).xyz - 1));
	
	vec4 light = CalcLightingEffect(normal, worldPos0);
	vec4 shadow = vec4(calcShadowIntensity(R_shadowMap, shadowCoords));
	
	vec4 final = light * shadow;
	
	vec4 texture = texture2D(diffuse, texCoord0);
	
	gl_FragColor = texture * clamp(final, 0.2, 1.0);
}
