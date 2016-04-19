#version 120

varying vec2 texCoord0;

uniform sampler2D diffuse;
uniform float R_godrayDensity;
uniform float R_godrayWeight;
uniform float R_godrayExposure;
uniform float R_godrayDecay;
uniform int R_godraySamples;
uniform vec3 R_lightPos;

void main()
{
		vec2 deltaTextCoord = vec2(texCoord0 - R_lightPos.xy);
    	vec2 textCoo = texCoord0;
    	deltaTextCoord *= 1.0 /  float(R_godraySamples) * R_godrayDensity;
    	float illuminationDecay = 1.0;
		vec4 color = vec4(0.0);
	
    	for(int i = 0; i < R_godraySamples; i++)
        {
                 textCoo -= deltaTextCoord;
                 vec4 sample = texture2D(diffuse, textCoo);
			
                 sample *= illuminationDecay * R_godrayWeight;

                 color += sample;

                 illuminationDecay *= R_godrayDecay;
         }
         
         color *= R_godrayExposure;
         gl_FragColor = clamp(color, 0.0, 1.0);
}
