# Camunda-Formio Springboot Example
This is an example of using [Camunda-Formio] with [Camunda] platform through [Camunda Spring Boot Application].

## Deploying with Docker:

1. Download the [Dockerfile]
2. Build the image:
  ```
  docker build --tag camunda-formio-example .
  ```
3. Start the container:
  ```
  docker run -p 8080:8080 camunda-formio-example
  ```
4. Open browser and navigate to http://127.0.0.1:8080/index.html
5. User login is `demo` and password is `demo`
6. Use buttons on the left to load forms and start processes

## What is deployed

1. Camunda Webapp with Cockpit, Tasklist and Admin modules http://127.0.0.1:8080/app/welcome/default
2. A simple dashboard with ability to display forms and start/complete the process http://127.0.0.1:8080/index.html
3. [Camunda-Formio] is included into .pom file as dependency.
4. REST for camunda-formio:
  * `GET 127.0.0.1:8080/api/ext/task/{id}/form` - load a form for task by task id. The loaded form is cleaned from unused process data
  * `POST 127.0.0.:8080/api/ext/task/{id}/complete` - submit formio form data to complete the task. Data validation and cleanup is performed before saving variables into the process
5. REST for Camunda Process Engine at `127.0.0.1:8080/rest`
6. NodeJS for running server-side Formio scripts

[Camunda]: https://camunda.org
[Camunda-Formio]: https://github.com/Artezio/camunda-formio
[Camunda Spring Boot Application]: https://docs.camunda.org/get-started/spring-boot/
[Dockerfile]: https://github.com/Artezio/camunda-formio-springboot-example/blob/master/Dockerfile