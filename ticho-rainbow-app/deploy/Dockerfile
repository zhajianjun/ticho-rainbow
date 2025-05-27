FROM openjdk:17-jdk-alpine
MAINTAINER zhajianjun '1019319473@qq.com'
# 暴露端口
EXPOSE 5122

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
ENV MAIL_ENABLE="false"
ENV MAIL_HOST=""
ENV MAIL_PORT=""
ENV MAIL_SENDER_NAME=""
ENV MAIL_USERNAME=""
ENV MAIL_PASSWORD=""
ENV RAINBOW_FILE_DOMAIN="http://${HOST_IP}:${SERVER_PORT}/api"
ENV RAINBOW_MVC_RESOURCE_PATH="/static/file/**"
ENV RAINBOW_MAX_FILE_SIZE="20MB"
ENV RAINBOW_MAX_PART_SIZE="50MB"
ENV RAINBOW_MAX_BIG_FILE_SIZE="100MB"

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS="-Xms256m -Xmx512m -Xmn170m"

WORKDIR /data
COPY ./ticho-rainbow-app/target/*.jar app.jar
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT java \
${JAVA_OPTS} \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/heapdump.hprof \
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
-Dticho.mail.enable="${MAIL_ENABLE}" \
-Dticho.mail.host="${MAIL_HOST}" \
-Dticho.mail.port="${MAIL_PORT}" \
-Dticho.mail.sender-name="${MAIL_SENDER_NAME}" \
-Dticho.mail.username="${MAIL_USERNAME}" \
-Dticho.mail.password="${MAIL_PASSWORD}" \
-Dticho.rainbow.file.domain="${RAINBOW_FILE_DOMAIN}" \
-Dticho.rainbow.file.mvc-resource-path="${RAINBOW_MVC_RESOURCE_PATH}" \
-Dticho.rainbow.file.max-file-size="${RAINBOW_MAX_FILE_SIZE}" \
-Dticho.rainbow.file.max-part-size="${RAINBOW_MAX_PART_SIZE}" \
-Dticho.rainbow.file.max-big-file-size="${RAINBOW_MAX_BIG_FILE_SIZE}" \
-Dspring.profiles.active="${PROFILES}" \
-jar \
app.jar
