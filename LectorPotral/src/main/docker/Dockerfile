# specify the base of our image
FROM openjdk:17-oracle

# location of .jar file
ARG JAR_FILE=*.jar

# copy the .jar file into our app
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
