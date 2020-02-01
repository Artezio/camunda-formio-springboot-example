# camunda-formio-springboot-example

### Deploying with Docker:

1. Download the Dockerfile
2. Build the image:
  ```
  docker build --tag camunda-formio .
  ```
3. Start the container:
  ```
  docker run -p 8080:8080 camunda-formio
  ```
4. Open browser and navigate to http://127.0.0.1:8080
5. User login is `demo` and password is `demo`
6. Use buttons on the left to load forms and start processes
