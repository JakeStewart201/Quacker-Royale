#version 300 es
layout (location = 0) in vec4 position;
layout (location = 1) in vec4 colour;

uniform mat4 Transformation;

out vec4 ourColour;

void main() {
    gl_Position = Transformation * position;
    ourColour = colour;
}
