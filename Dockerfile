FROM openjdk:17-alpine
COPY ./ /home/freeter
RUN apk add font-vollkorn font-misc-cyrillic font-mutt-misc font-screen-cyrillic font-winitzki-cyrillic font-cronyx-cyrillic
RUN apk add maven
RUN mvn -f /home/freeter/pom.xml clean package
CMD ["java", "-jar", "/home/freeter/target/freeter.jar"]