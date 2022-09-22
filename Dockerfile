FROM reg.local.liuzp.com/develop/jdk8:v1.0

MAINTAINER lzp

ADD target/pipeline-web-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["/sbin/tini","--","java","-jar","app.jar"]