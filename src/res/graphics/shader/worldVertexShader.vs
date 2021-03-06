#version 430 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 3) in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

uniform vec3 cameraPosition;

out vec4 vColor;
out vec2 vTextureCoords;

void main() {
	//gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);

	vec4 worldSpace = transformationMatrix * vec4(position, 1.0);
	worldSpace.xyz -= cameraPosition;
	//worldSpace = vec4(0.0f, sin(worldSpace.x * 0.5) + cos(worldSpace.z * 0.5), 0.0f, 0.0f);
	//worldSpace = vec4(0.0f, (worldSpace.z * worldSpace.z + worldSpace.x * worldSpace.x) * -0.009, 0.0f, 0.0f);
	
	//Nice parabola Equation
	//worldSpace = vec4(0.0f, (worldSpace.z * worldSpace.z + (worldSpace.x * worldSpace.x * 0.5)) * -0.009, 0.0f, 0.0f);
	//worldSpace = vec4(0.0f, (worldSpace.z * worldSpace.z + worldSpace.x * worldSpace.x) * -0.009, 0.0f, 0.0f);
	
	//Circle Equation
	//float groundPosition = sqrt(1000 - (worldSpace.x * worldSpace.x + (worldSpace.z * worldSpace.z))) - sqrt(1000);
	//worldSpace = vec4(0.0f, groundPosition, 0.0f, 0.0f);
	
	
	vec4 newPosition = vec4(position, 1.0);
	newPosition.xyz += (worldSpace * transformationMatrix).xyz;
	
	//vColor = vec4(0.0, 0.8, 0.0, 1.0);
	//vColor = vec4((newPosition.x / 16), 1.0, newPosition.z / 16, 1.0);
	
	vColor = color;
	vTextureCoords = textureCoords;
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * newPosition;
}
