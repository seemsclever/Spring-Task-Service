package com.seemsclever.outbox.handler;

public interface OutboxEventHandler {
    String getType();

    void handle(String payload);
}
