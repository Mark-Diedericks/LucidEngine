uniform vec3 eyePos;
uniform float specularInt;
uniform float specularExp;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct DirectionalLight {
	BaseLight base;
	vec3 direction;
};

struct PointLight
{
    BaseLight base;
    Attenuation atten;
    vec3 position;
    float range;
};

struct SpotLight {
	PointLight pointLight;
	vec3 direction;
	float cutoff;
};

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal, vec3 worldPos0)
{
    float diffuseFactor = dot(normal, -direction);
    
    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specularColor = vec4(0,0,0,0);
    
    if(diffuseFactor > 0)
    {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
        
        vec3 directionToEye = normalize(eyePos - worldPos0);
        vec3 halfDir = normalize(directionToEye-direction);
        
        float specularFactor = dot(halfDir, normal);
        specularFactor = pow(specularFactor, specularExp);
        
        if(specularFactor > 0)
        {
            specularColor = vec4(base.color, 1.0) * specularInt * specularFactor;
        }
    }
    
    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight dirLight, vec3 normal, vec3 worldPos0) {
	return calcLight(dirLight.base, -dirLight.direction, normal, worldPos0);
}

vec4 calcPointLight(PointLight pointLight, vec3 normal, vec3 worldPos0)
{
    vec3 lightDirection = worldPos0 - pointLight.position;
    float distanceToPoint = length(lightDirection);
    
    if(distanceToPoint > pointLight.range)
        return vec4(0,0,0,0);
    
    lightDirection = normalize(lightDirection);
    
    vec4 color = calcLight(pointLight.base, -lightDirection, normal, worldPos0);
    
    float attenuation = pointLight.atten.constant + pointLight.atten.linear * distanceToPoint + pointLight.atten.exponent * distanceToPoint * distanceToPoint + 0.0001;
                         
    return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal, vec3 worldPos0) {
	vec3 lightDirection = normalize(worldPos0 - spotLight.pointLight.position);
    float spotFactor = dot(lightDirection, spotLight.direction);
    
    vec4 color = vec4(0,0,0,0);
    
    if(spotFactor > spotLight.cutoff)
    {
        color = calcPointLight(spotLight.pointLight, normal, worldPos0) * (1.0 - (1.0 - spotFactor)/(1.0 - spotLight.cutoff));
    }
    
    return color;
}

float calcTess(float distance) {
	if(distance <= 2.0) {
		return 10.0;
	} else if(distance <= 4.0) {
		return 8.0;
	} else if(distance <= 6.0) {
		return 6.0;
	} else if(distance <= 8.0) {
		return 4.0;
	} else {
		return 2.0;
	}
}