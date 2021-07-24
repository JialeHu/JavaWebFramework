# Demo Web App

This is a simple useless app demonstrating some functionality of the framework.

## Instruction
- The framework is the only dependency (see [pom.xml](pom.xml)).
- Define web app config in `.env` or set environment variables.
  - Store static files in `./src/main/webapp/static/` or other dir defined in env.
  - Store JSP files in `./src/main/webapp/templates/` or other dir defined in env.
  - Set bind address and port in env, default to `localhost:8080`.
  - Set debug mode in env, when set to `FALSE`, server info and debug trace will be hide. Log level will be set to info.
- Start app by running `main` function in [DemoWebApplication](src/main/java/app/demo/DemoWebApplication.java)
- Play around with [controllers](src/main/java/app/demo/controller) to mess up endpoint handling.
- Add more [Aspects](src/main/java/app/demo/aspect) to intercept methods with AspectJ syntax.
