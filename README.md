# Дипломный проект по профессии «Тестировщик»

# [Веб-сервис заказа путешествия](https://github.com/netology-code/qa-diploma)

Проект предназначен для функционального тестирования веб-приложения "aqa-shop.jar". 
Цель тестирования – проверить позитивные и негативные сценарии покупки тура через приложение, в том числе UI-тесты и корректность внесения информации в базу данных. 
В процессе работы приложение взаимодействует с базами данных MySQL и PostgreSQL, а также API эмулятора банковских сервисов, написанном на Node.js. Среда для их запуска находится в Docker-контейнерах.

Для запуска автотестов потребуется выполнить следующие шаги:

1. Установить IntelliJ IDEA 
2. Установить Docker
3. Загрузить необходимые Docker-образы с компонентами тестовой среды. Для этого нужно выполнить в командной строке команды

```
docker pull mysql:8.0.34
docker pull postgres:13-alpine3.19
docker pull node:current-alpine
```
4. Перейти в папку с проектом, для этого выполнить команду

```cd <путь к папке проекта>```

5. Для запуска тестовой среды выполнить команду

```docker-compose up```

6. Проверить статус контейнеров командой:

```docker-compose ps```

Cтатус контейнеров `UP`.

7. Для запуска тестируемого приложения выполнить команду

```java -jar artifacts/aqa-shop.jar```

в зависимости от выбранной для работы `СУБД` выполнить команду в консоли:

- Для PostgreSQL:

```java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./artifacts/aqa-shop.jar```

- Для MySQL:

```java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar```

Сервис открывается по адресу: _http://localhost:8080/_

8. Для запуска автотестов выполнить команду

```./gradlew clean test```

- Для PostgreSQL:

```./gradlew clean test "-Ddatasource.url=jdbc:postgresql://localhost:5432/app"```

- Для MySQL:

```./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/app"```

9. Для просмотра отчёта о тестировании выполнить команду
   
```./gradlew allureServe```

10. Для завершения работы тестируемого приложения выполнить 

`CTRL + C` с подтверждением действия в терминале вводом `Y`

11. Для завершения работы контейнеров выполнить команду

```docker-compose down```

