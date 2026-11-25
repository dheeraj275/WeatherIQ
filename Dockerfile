
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src

RUN javac --add-modules jdk.httpserver -d out src/weatheriq/*.java

EXPOSE 10000

CMD ["sh", "-c", "java --add-modules jdk.httpserver -cp out weatheriq.Main ${PORT:-10000}"]
