# 多阶段构建：第一阶段使用Maven镜像构建项目
FROM maven:3.9.6-amazoncorretto-21 AS builder

# 复制项目文件
COPY . /usr/src/app
WORKDIR /usr/src/app

# 使用阿里云镜像源构建项目
RUN sed -i 's|https://repo.maven.apache.org/maven2|https://maven.aliyun.com/repository/public|g' /usr/share/maven/conf/settings.xml && \
    mvn clean package -DskipTests

# 第二阶段：使用Tomcat镜像部署应用
FROM tomcat:11-jdk21

# 安装unzip工具并删除默认的ROOT应用
RUN apt-get update && apt-get install -y unzip && \
    rm -rf /usr/local/tomcat/webapps/ROOT

# 从构建阶段复制WAR文件，命名为demo_war_exploded.war以匹配期望的上下文路径
COPY --from=builder /usr/src/app/target/*.war /usr/local/tomcat/webapps/demo_war_exploded.war

# 确保MySQL驱动在Tomcat中可用
COPY --from=builder /usr/src/app/target/demo-1.0-SNAPSHOT.war /tmp/app.war
RUN unzip /tmp/app.war -d /tmp/app && \
    cp /tmp/app/WEB-INF/lib/mysql-connector-j-*.jar /usr/local/tomcat/lib/ && \
    rm -rf /tmp/app.war /tmp/app

# 暴露端口
EXPOSE 8080

# 启动Tomcat
CMD ["catalina.sh", "run"]