FROM openjdk:8-jre
RUN echo "Europe/Berlin" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata
RUN mkdir /data
WORKDIR /data
ADD elastic-interview-example-springboot-0.0.1.jar elastic-interview-example-springboot-0.0.1.jar

ENV springprofiles="" \
    MAXRAMIFNOLIMIT=4096

ENTRYPOINT MAXRAM=$(expr `cat /sys/fs/cgroup/memory/memory.limit_in_bytes` / 1024 / 1024) && \
           MAXRAM=$(($MAXRAM>$MAXRAMIFNOLIMIT?$MAXRAMIFNOLIMIT:$MAXRAM))m && \
           echo "MaxRam: $MAXRAM" && \
           java -XX:MaxRAM=$MAXRAM -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active="$springprofiles" elastic-interview-example-springboot-0.0.1.jar

#ENTRYPOINT java -XX:+UseCGroupMemoryLimitForHeap -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active="$springprofiles" elastic-interview-example-springboot-0.0.1.jar

EXPOSE 8080