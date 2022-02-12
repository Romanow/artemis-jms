package ru.romanow.jpa;

public final class HelpPrinter {
    public static final String HELP = "help";
    public static final String BROKER_URL = "broker-url";
    public static final String USERNAME = "broker-username";
    public static final String PASSWORD = "broker-password";
    public static final String QUEUE_NAME = "messaging.queue-name";
    public static final String MESSAGE = "messaging.message";

    public static void print() {
        System.out.printf(
                "Simple Java application for JMS messaging in Artemis\n\n" +
                        "--%-24s set broker url (default [tcp://127.0.0.1:61616])\n" +
                        "--%-24s set broker url (default [admin])\n" +
                        "--%-24s set broker password (default [admin])\n" +
                        "--%-24s set queue name (default 'my-queue')\n" +
                        "--%-24s set message text (default 'hello, world + timestamp')\n" +
                        "--%-24s print help message\n",
                BROKER_URL, USERNAME, PASSWORD, QUEUE_NAME, MESSAGE, HELP
        );
    }
}
