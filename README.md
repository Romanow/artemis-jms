# Simple JMS application

## Start Artemis in Docker

```shell
$ docker compose up -d
```

Из-за проблемы [AMQ212054 Destination address is blocked](https://stackoverflow.com/a/53836629/5649869) используется
донастройка конфигурации Artemis: меняется параметр `max-disk-usage = 100`.

Описание
из [документации](https://activemq.apache.org/components/artemis/documentation/latest/paging.html#max-disk-usage):

> Max Disk Usage
> The System will perform scans on the disk to determine if the disk is beyond a configured limit. These are configured
> through max-disk-usage in percentage. Once that limit is reached any message will be blocked. (unless the protocol
> doesn't support flow control on which case there will be an exception thrown and the connection for those clients dropped).

## Start Java application

```shell
$ ./gradlew clean build
$ ./gradlew bootRun
```

По-умолчанию приложение создает очередь `my-queue` и раз в 5 секунд отправляет туда сообщения
вида: `Hello queue world " + now().format(ISO_LOCAL_DATE_TIME)`

Для задания настроек нужно в скрипт запуска передать параметры через `--args='...'`:

```shell
$ ./gradlew bootRun --args='--messaging.message="Hello, World!" --messaging.queue-name="default-queue" --broker-url=tcp://artemis:61616'
```

Все доступные настройки:

```
Simple Java application for JMS messaging in Artemis

--broker-url				           set broker url (default [tcp://127.0.0.1:61616])
--broker-username			           set broker url (default [admin])
--broker-password			           set broker password (default [admin])
--messaging.queue-name		           set queue name (default 'my-queue')
--messaging.message			           set message text (default 'hello, world + timestamp')
--messaging.properties.<key>=<value>   set properties
--help         						   print help message
```

## Consume from Artemis UI

Для получения сообщений из очереди через [Artemis UI](http://localhost:8161/console) -> `Queue` -> `my-queue`
-> `opeartions` -> `browse()`.