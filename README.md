# 🌐 pk8000.ru — открытый проект

Это **открытый проект** сайта [pk8000.ru](https://pk8000.ru).

Проект создан для развития навыков, пробы технологий и просто из любви к своему первому компьютеру ПК8000 "Сура".  

Вы можете использовать материалы проекта, соблюдать условия лицензии и вносить улучшения через `Pull Request`.

---

## 📚 Технологии

- **Backend:** Spring Boot 3.5, Spring Security, Spring Data Jpa
- **Frontend:** Thymeleaf, JavaScript, flexmark
- **Database:** MySQL 8.0
- **Migrations:** Liquibase
- **Auth:** Keycloak (OIDC, Realm для auth.pk8000.ru)
- **Server:** Nginx (reverse proxy + SSL via Let's Encrypt)
- **Runtime:** JDK 21
- **Dev environment:** Docker Compose

---

## 🧠 Возможности проекта

- AJAX-навигация без перезагрузки страниц
- Поддержка вложенных меню (через Thymeleaf-фрагменты)
- Рендеринг Markdown в HTML через flexmark
- Управление пользователями через Keycloak
- Liquibase-миграции и версионирование БД
- HTTPS и автоматическая конфигурация прокси через Nginx + Let's Encrypt
- Готовая инфраструктура для локальной разработки в Docker

---

## 🌐 Сайт

🔗 [https://pk8000.ru](https://pk8000.ru)

- Основной сайт: [https://pk8000.ru](https://pk8000.ru)
- Система авторизации: [https://auth.pk8000.ru](https://auth.pk8000.ru)

---

## 🧱 Структура проекта

content/... # контент сайта (меню, статичные страницы, файлы и тд.)
src/  
├─ main/  
│ ├─ java/ru/pk8000/... # Контроллеры, сервисы, репозитории  
│ ├─ resources/  
│ │ ├─ templates/ # HTML-шаблоны (Thymeleaf)  
│ │ ├─ static/ # JS, CSS, изображения  
│ │ ├─ db/changelog/ # Скрипты Liquibase  
│ │ └─ application.yml # Основная конфигурация  
└─ test/... # Тесты  
tools/... # docker-compose.yml для локального контейнера dev-среды.


---

## ⚙️ Развертывание локально (через Docker Compose)

Переходим в папку `./tools/pk8000-docker-container`.

### 1️⃣ Создаем контейнер MySql.
Запускаем:
```shell
docker-compose up -d  
```

### 2️⃣ Проверяем как отработали скрипты.
Иногда init.sql не отрабатывает полностью. Поэтому, лучше продублировать его. Заходим в mysql (exec):
```shell
mysql -u root -p
```
Вводим пароль из MYSQL_ROOT_PASSWORD и проверяем, все ли пользователи присутствуют:
```shell
SELECT user, host FROM mysql.user;
```
Если какого нет, то добавляем, как в `init.sql`.  
Обычно пропускается keycloak@localhost, а он нам понадобится.

### 3️⃣ Создаем контейнер Keycloak.
```shell
docker-compose --profile manual up -d keycloak
```
И ждем в консоли докера появления надписи:
```shell
WARN  [org.keycloak.quarkus.runtime.KeycloakMain] (main) Running the server in development mode. DO NOT use this configuration in production.
```

### 4️⃣ Импортируем настройки для Keycloak.
Заходим в контейнер Keycloak (exec):
```shell
mkdir /opt/keycloak/data/import
```
Из IDEA:
```shell
docker cp ./site-pk8000-ru-realm-realm.json keycloak-site-pk8000:/opt/keycloak/data/import/site-pk8000-ru-realm-realm.json
```
И из контейнера запускаем импорт настроек:
```shell
/opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import --override true --features=scripts
```
Keycloak готов. Желательно перезапустить его и mysql, во избежание недоразумений.

Далее, заходим в UI Keycloak (localhost:8080) и проверяем наличие realm: `site-pk8000-ru-realm`,
и клиента: `client_pk8000_ru_id` в этом realm.

Заходим в `Clients->client_pk8000_ru_id->Credentials` и копируем `ClientSecret`. Далее,
либо помещаем это значение в переменную окружения `PK8000_CLIENT_SECRET`, либо
прописываем в `application.yml`:   
`spring.security.oauth2.client.registration.client-pk8000-ru-id.client-secret`

На этом настройка закончена. Можно запускать программу.
При первом старте Liquibase создаст все необходимые таблицы.  

---

## 🧰 Сборка и локальный запуск.

```shell
./mvnw clean package -DskipTests
cp -r content target/
java -jar target/site-pk8000-ru-0.0.1-SNAPSHOT.jar
```

---

## 📖 Лицензия

Проект распространяется под лицензией [MIT](LICENSE).

---

💬 Обратная связь

Предложения, баг-репорты и вопросы можно оставлять в разделе [Issues](https://github.com/MrDemonid/Site-pk8000-ru/issues)
