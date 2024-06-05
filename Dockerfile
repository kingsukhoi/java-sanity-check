FROM docker.io/library/debian:12-slim as builder

RUN apt update && apt install -y wget apt-transport-https gpg
RUN wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
RUN echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
RUN apt update && apt install -y temurin-21-jdk
RUN mkdir /opt/app
WORKDIR /opt/app
COPY . .
RUN ./gradlew bootJar --no-daemon


FROM docker.io/library/debian:12-slim

RUN apt update && apt install -y apt-transport-https ca-certificates

COPY --from=builder /etc/apt/trusted.gpg.d/adoptium.gpg /etc/apt/trusted.gpg.d/
COPY --from=builder /etc/apt/sources.list.d/adoptium.list /etc/apt/sources.list.d/ 
COPY --from=builder /opt/app/build/libs/sanity-check-0.0.1-SNAPSHOT.jar /

RUN apt update && apt install -y temurin-21-jdk



ENTRYPOINT java -jar /sanity-check-0.0.1-SNAPSHOT.jar
