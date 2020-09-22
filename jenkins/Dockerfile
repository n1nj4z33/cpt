FROM jenkins/jenkins:latest

USER root
RUN apt-get -y update \
      && apt-get -y install sudo \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# RUN groupadd docker
# RUN usermod -aG docker jenkins

USER jenkins
