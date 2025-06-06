FROM nginx:1.25.3
MAINTAINER zhajianjun '1019319473@qq.com'
# 暴露端口
EXPOSE 5121

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
ENV RAINBOW_SECURITY_KEY="ticho"

ENV TZ=Asia/Shanghai
ENV LANG C.UTF-8
ENV JAVA_OPTS="-Xms256m -Xmx512m -Xmn170m"

WORKDIR /data
COPY ./jre /jre
COPY ./ticho-rainbow-app/target/ticho-rainbow.jar app.jar
COPY ./ticho-rainbow-web/dist /usr/share/nginx/html
COPY ./ticho-rainbow-web/nginx.conf /etc/nginx/conf.d/default.conf
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./deploy/start.sh /docker-entrypoint.d/50-start.sh
RUN chmod 0755 /docker-entrypoint.d/50-start.sh