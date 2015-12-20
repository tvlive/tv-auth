FROM java:openjdk-7-jdk


ADD target/universal/tv-auth-999-SNAPSHOT.zip /root/
WORKDIR /root
RUN unzip tv-auth-999-SNAPSHOT.zip
RUN rm tv-auth-999-SNAPSHOT.zip

ADD run.sh /root/run.sh

CMD ["/bin/bash","/root/run.sh"]






