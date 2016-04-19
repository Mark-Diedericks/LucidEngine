#version 120

uniform vec3 R_color;

void main()
{
	gl_FragColor = vec4(R_color.xyz, 1.0);
}
