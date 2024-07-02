FROM openjdk:17

WORKDIR /app

COPY . .

ARG PORT=3000

ENV PORT=$PORT

RUN ./mvnw clean package
RUN cp target/*.jar ./program

EXPOSE $PORT

CMD [ "java", "-jar", "./program" ]
