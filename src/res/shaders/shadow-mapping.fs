#version 120

void main()
{
	float dx = dFdx(gl_FragCoord.z);
	float dy = dFdy(gl_FragCoord.z);
	float moment = gl_FragCoord.z * gl_FragCoord.z + 0.25 * (dx * dx + dy + dy);
	
	gl_FragColor = vec4(1.0 - gl_FragCoord.z, 1.0 - moment, 0.0, 0.0);
}
