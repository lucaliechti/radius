# Radius

### Security

This project uses git-secret to exchange sensitive information over git.

Before pushing, make sure you run `git secret hide`. After pulling, execute `git secret reveal`.

### Building the project

In IntelliJ, add the maven goal `properties:write-project-properties` before deploying the artifact locally. Make sure to use the `dev`profile.

For deployment, run `mvn clean package -P prod` and deploy the resulting war file.
