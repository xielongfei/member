#!/bin/bash

# ���Ƚ�����Ŀ���
set JAVA_HOME=D:\Java\jdk-17.0.3.1
call mvn clean package

echo "����ִ����������..."

# �������Ŀ�ִ���ļ��ϴ���Linux�������ϣ��滻����ʾ���еķ�������Ϣ��·��
scp member-start/target/member.jar root@124.222.0.109:server/member