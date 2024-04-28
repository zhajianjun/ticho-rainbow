FROM openjdk:8-jre-alpine

ENV PROFILES="prod"
ENV SERVER_PORT="5122"
ENV JASYPT_ENCRYPTOR_PASSWORD=""
ENV DATASOURCE_URL=""
ENV DATASOURCE_USERNAME=""
ENV DATASOURCE_PASSWORD=""
ENV INTRANET_SERVER_PORT="5120"
ENV INTRANET_SERVER_SSL_ENABLE="false"
ENV INTRANET_SERVER_SSL_PORT="5443"
ENV INTRANET_SERVER_SSL_PATH=""
ENV INTRANET_SERVER_SSL_PASSWORD=""
ENV INTRANET_SERVER_MAX_REQUESTS="1024"
ENV INTRANET_SERVER_MAX_BIND_PORTS="10000"
ENV FILE_DOMAIN="http://${HOST_IP}:${SERVER_PORT}/api"

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS='-Xms256m -Xmx512m -Xmn170m'

VOLUME /tmp
COPY target/*.jar app.jar
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT java \
${JAVA_OPTS} \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/heapdump.hprof \
-Dfile.encoding=UTF-8 \
-Djasypt.encryptor.password="${JASYPT_ENCRYPTOR_PASSWORD}" \
-Dserver.ip="${HOST}" \
-Dserver.port="${SERVER_PORT}" \
-Dticho.datasource.url="${DATASOURCE_URL}" \
-Dticho.datasource.username="${DATASOURCE_USERNAME}" \
-Dticho.datasource.password="${DATASOURCE_PASSWORD}" \
-Dticho.intranet.server.port="${INTRANET_SERVER_PORT}" \
-Dticho.intranet.server.sslEnable="${INTRANET_SERVER_SSL_ENABLE}" \
-Dticho.intranet.server.sslPort="${INTRANET_SERVER_SSL_PORT}" \
-Dticho.intranet.server.sslPath="${INTRANET_SERVER_SSL_PATH}" \
-Dticho.intranet.server.sslPassword="${INTRANET_SERVER_SSL_PASSWORD}" \
-Dticho.intranet.server.maxRequests="${INTRANET_SERVER_MAX_REQUESTS}" \
-Dticho.intranet.server.maxBindPorts="${INTRANET_SERVER_MAX_BIND_PORTS}" \
-Drainbow.file.domain="${FILE_DOMAIN}" \
-Dspring.profiles.active="${PROFILES}" \
-jar \
app.jar
