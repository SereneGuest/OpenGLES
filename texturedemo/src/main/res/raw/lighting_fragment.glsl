#version 300 es
precision mediump float;
out vec4 color;
uniform vec3 lightColor;
uniform vec3 objectColor;
uniform vec3 lightPos;

in vec3 Normal;
in vec3 FragPos;
uniform vec3 viewPos;

float specularStrength = 0.5f;
float ambientStrength = 0.5f;

void main() {
    //Diffuse Lighting
    /*float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(Normal);
    vec3 lightDirection = normalize(lightPos - FragPos);

    float diff = max(dot(norm,lightDirection),0.0);
    vec3 diffuse = diff * lightColor;
    vec3 result = (ambient + diffuse) * objectColor;
    color = vec4(result,1.0f);*/
    //Specular Lighting

    vec3 ambient = ambientStrength * lightColor;
    vec3 norm = normalize(Normal);
    vec3 lightDirection = normalize(lightPos - FragPos);
    float diff = max(dot(norm,lightDirection),0.0);
    vec3 diffuse = diff * lightColor;

    vec3 viewDirection = normalize(viewPos - FragPos);
    vec3 reflectDirection = reflect(-lightDirection,norm);

    float spec = pow(max(dot(viewDirection,reflectDirection),0.0),32.0);
    vec3 specular = specularStrength * spec * lightColor;
    vec3 result = (ambient + diffuse + specular) * objectColor;
    color = vec4(result,1.0f);
}
