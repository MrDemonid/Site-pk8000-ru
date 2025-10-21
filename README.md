## О проекте.

В папке ./content/menu хранятся статичные страницы сайта, в формате makrdown. Папки сканируются
и на их основе создается меню сайта.  
Если в папке отсутствует meta.yaml, то она в меню не входит, а является просто служебной (/images, /files, etc.).

В папке ./content/menu-icons - иконки меню. Поскольку настроена
переадресация (src/main/java/mr/demonid/pk8000/ru/configs/ImageWebConfig.java),
то в меню они просто пишутся по имени, без указания пути.

Папки ./content/soft/** - для динамически формируемых страниц.


## Настройки контейнера, для локальной разработки.

Переходим в папку `./tools/pk8000-docker-container`.


### Создаем контейнеры.

Запускаем:
```shell
docker-compose up -d  
```

#### Проверяем как отработали скрипты.

Иногда init.sql не отрабатывает полностью. Поэтому, лучше продублировать его. Заходим в mysql (exec):
```shell
mysql -u root -p
```
вводим пароль из MYSQL_ROOT_PASSWORD и проверяем, все ли пользователи присутствуют:
```shell
SELECT user, host FROM mysql.user;
```
Если какого нет, то добавляем, как в `init.sql`.  
Обычно пропускается keycloak@localhost, а он нам понадобится.


#### Создаем контейнер Keycloak.

```shell
docker-compose --profile manual up -d keycloak
```

И ждем в консоли докера появления надписи:
```shell
WARN  [org.keycloak.quarkus.runtime.KeycloakMain] (main) Running the server in development mode. DO NOT use this configuration in production.
```


### Импортируем настройки для Keycloak.

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

Далее, заходим в UI (localhost:8080) и проверяем наличие realm: `site-pk8000-ru-realm`, 
и клиента: `client_pk8000_ru_id` в этом realm.

Заходим в `Clients->client_pk8000_ru_id->Credentials` и копируем `ClientSecret`. Далее,
либо помещаем это значение в переменную окружения `PK8000_CLIENT_SECRET`, либо
прописываем в `application.yml`:   
`spring.security.oauth2.client.registration.client-pk8000-ru-id.client-secret`

На этом настройка закончена. Можно запускать программу. 
При первом старте Liquibase создаст все необходимые таблицы.
