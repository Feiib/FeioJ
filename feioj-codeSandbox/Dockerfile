# 基础镜像
FROM openjdk:8-jdk-alpine
# 改用国内源，加快安装
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk update && \
    apk add --no-cache python3 py3-pip && \
    ln -sf python3 /usr/bin/python
# 将 jar 包添加到工作目录，比如 target/yuoj-backend-user-service-0.0.1-SNAPSHOT.jar
ADD target/feioj-codesandbox-0.0.1-SNAPSHOT.jar .
# 暴露端口
EXPOSE 8090
# 启动命令
ENTRYPOINT ["java","-Xms128m", "-Xmx256m","-jar","/feioj-codesandbox-0.0.1-SNAPSHOT.jar"]

