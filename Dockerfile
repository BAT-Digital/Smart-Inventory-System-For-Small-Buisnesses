# Используем базовый образ с JDK 17
FROM eclipse-temurin:17-jdk AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Собираем JAR-файл с помощью Maven
RUN ./mvnw clean package -DskipTests

# Используем легковесный JDK-образ для запуска
FROM eclipse-temurin:17-jre

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из предыдущего контейнера
COPY --from=build /app/target/SmartInventorySystem-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
