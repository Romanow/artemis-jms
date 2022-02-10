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

Приложение создает очередь `my-queue` и раз в 5 секунд отправляет туда сообщения
вида: `Hello queue world " + now().format(ISO_LOCAL_DATE_TIME)`

```shell
$ ./gradlew clean build
$ ./gradlew bootRun
```

## Consume from Artemis UI

Для получения сообщений из очереди через [Artemis UI](http://localhost:8161/console) -> `Queue` -> `my-queue`
-> `opeartions` -> `browse()`.