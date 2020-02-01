# Build Camunda Formio
FROM maven:3.5.2-jdk-8-alpine as maven_builder

ARG resetcache=0

RUN apk add --no-cache git

ARG resetcache=1
RUN cd / &&\
	git clone https://github.com/Artezio/camunda-formio-springboot-example.git &&\
	cd /camunda-formio-springboot-example &&\
	mvn clean install -Dmaven.test.skip=true


# Deploy nodejs+java
FROM centos:7

ARG NODEJS_VERSION=13.x
ENV MAX_HEAP_SIZE_MB 2048


# Install NodeJS server
RUN cd /opt && \
    curl -sL https://rpm.nodesource.com/setup_$NODEJS_VERSION | bash -
RUN yum -y install java-1.8.0-openjdk-devel java-1.8.0-openjdk curl nodejs && yum clean all 

ENV JAVA_HOME /usr/lib/jvm/java-openjdk

# Install formio libs
RUN mkdir /app
WORKDIR /app
RUN npm install jsdom-global jsdom formiojs
COPY --from=maven_builder /camunda-formio-springboot-example/target/formio-springboot-example.jar /app/formio-springboot-example.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/formio-springboot-example.jar"]
