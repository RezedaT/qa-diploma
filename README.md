# Дипломный проект по профессии «Тестировщик»

# [Веб-сервис заказа путешествия](https://github.com/netology-code/qa-diploma)

Проект предназначен для функционального тестирования веб-приложения "aqa-shop.jar". 
Цель тестирования – проверить позитивные и негативные сценарии покупки тура через приложение, в том числе UI-тесты и корректность внесения информации в базу данных. 
В процессе работы приложение взаимодействует с базами данных MySQL и PostgreSQL, а также API эмулятора банковских сервисов, написанном на Node.js. Среда для их запуска находится в Docker-контейнерах.

## ПО для работы с проектом:
- Intellij IDEA;
- Docker Desktop;
- Chrome Google Браузер.

## Запуск проекта.

Для запуска автотестов потребуется выполнить следующие шаги:
1. Клонировать репозиторий на локальный ПК:
   `git clone git@github.com:RezedaT/qa-diploma.git`
2. Открыть проект в Intellij IDEA.
3. Запустить в терминале контейнеры MySQL, PostgreSQL, Node.js:
   `docker-compose up`
4. Проверить в терминале статус контейнеров командой:
   `docker-compose ps`
Cтатус контейнеров `UP`.
5. Запустить в терминале тестируемое приложение (jar-file):
   `java -jar artifacts/aqa-shop.jar`
6. Запустить в терминале автотесты:
   `./gradlew clean test`
7. Для просмотра отчёта Allure о тестировании:
   `./gradlew allureServe`
8. Завершить в терминале  работу тестируемого приложения: 
`CTRL + C` с подтверждением действия в терминале вводом `Y`
9. Завершить в терминале работу контейнеров:
`docker-compose down`

## Интеграция CI
[![Java CI with Gradle](https://github.com/RezedaT/qa-diploma/actions/workflows/gradle.yml/badge.svg)](https://github.com/RezedaT/qa-diploma/actions/workflows/gradle.yml)

### Документация
[План автоматизации](documentation/Plan.md)  
[Отчётные документы по итогам тестирования](documentation/Report.md)    
[Отчётные документы по итогам автоматизации](documentation/Summary.md)  
