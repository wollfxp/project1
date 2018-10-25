echo building jar
mvnw package -DskipTests
echo building image
docker build -t starships .