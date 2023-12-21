#!/bin/sh

set -eu

echo "web 项目开始启动"
nohup \
/jre/bin/java \
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
-Dspring.profiles.active="${PROFILES}" \
-jar \
/app.jar \
>/dev/null 2>&1 &
echo "web 项目启动成功"

exit 0