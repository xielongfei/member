#!/bin/bash

# 首先进行项目打包
set JAVA_HOME=D:\Java\jdk-17.0.3.1
call mvn clean package

echo "继续执行其他命令..."

# 将打包后的可执行文件上传到Linux服务器上，替换以下示例中的服务器信息和路径
scp member-start/target/member.jar root@124.222.0.109:server/member