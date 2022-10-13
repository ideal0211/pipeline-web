FROM registry.cn-shenzhen.aliyuncs.com/develop-liuzp/jdk8:v1.0

MAINTAINER lzp

ADD target/pipeline-web-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-jar","app.jar"]