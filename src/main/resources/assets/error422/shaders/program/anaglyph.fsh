#version 150

uniform sampler2D DiffuseSampler;
uniform float Offset;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 left = texture(DiffuseSampler, texCoord + vec2(Offset, 0.0));
    vec4 center = texture(DiffuseSampler, texCoord);
    vec4 right = texture(DiffuseSampler, texCoord - vec2(Offset, 0.0));

    vec3 color = vec3(left.r, center.g, right.b);
    color.r = min(1.0, color.r * 1.3);
    color.b = min(1.0, color.b * 1.3);

    float vignette = 1.0 - smoothstep(0.2, 0.9, length(texCoord - vec2(0.5)));
    color *= mix(0.7, 1.0, vignette);

    if (mod(floor(texCoord.y * 900.0), 2.0) < 1.0) {
        color *= 0.92;
    }

    fragColor = vec4(color, 1.0);
}
