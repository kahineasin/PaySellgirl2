# Docker image for springboot file run
# VERSION 0.0.1
# Author: eangulee
# 鍩虹闀滃儚浣跨敤java
# FROM java:8
FROM openjdk:8
# 浣滆��
MAINTAINER wxj <wxj@perfect99.com>
# VOLUME 鎸囧畾浜嗕复鏃舵枃浠剁洰褰曚负/tmp銆�
# 鍏舵晥鏋滄槸鍦ㄤ富鏈� /var/lib/docker 鐩綍涓嬪垱寤轰簡涓�涓复鏃舵枃浠讹紝骞堕摼鎺ュ埌瀹瑰櫒鐨�/tmp
VOLUME /tmp
# 灏唈ar鍖呮坊鍔犲埌瀹瑰櫒涓苟鏇村悕涓篴pp.jar
ADD app/sellgirlPayWeb-0.0.4.jar /usr/local/app/app.jar
COPY app/lib /usr/local/app/lib
WORKDIR /usr/local/app/

EXPOSE 28901

# 杩愯jar鍖�
ENTRYPOINT ["java","-jar","app.jar"]
