# camunda-formio-springboot-example

### Deploying with Docker:

1. Download the [Dockerfile](https://github.com/Artezio/camunda-formio-springboot-example/blob/master/Dockerfile)
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



### What is deployed

1. Camunda Webapp with Cockpit, Tasklist and Admin modules http://127.0.0.1:8080/app/welcome/default
2. A simple dashboard with ability to display forms and start/complete the process http://127.0.0.1:8080/index.html
3. [camunda-formio](https://github.com/Artezio/camunda-formio) is included into .pom file as dependency. It's available as maven artifact at [Artezio Github Maven repository](https://github.com/Artezio/ART-MVN-REPO), see instructions there on how to add it to your project
4. REST for camunda-formio:
  * `GET 127.0.0.1:8080/api/ext/task/{id}/form` - load a form for task by task id. The loaded form is cleaned from unused process data
  * `POST 127.0.0.:8080/api/ext/task/{id}/complete` - submit formio form data to complete the task. Data validation and cleanup is performed before saving variables into the process
5. REST for Camunda Process Engine at `127.0.0.1:8080/rest`
6. NodeJS for running server-side Formio scripts
