FROM ubuntu:bionic

#SHELL
SHELL ["/bin/bash", "-c"]

#WORKDIR
WORKDIR /root

RUN apt-get update -y
RUN apt-get install default-jre -y

COPY apparel-inventory-manager.jar .

ENTRYPOINT ["java","-jar","./apparel-inventory-manager.jar"]