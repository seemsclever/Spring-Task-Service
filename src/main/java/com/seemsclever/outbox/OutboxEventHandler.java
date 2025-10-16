package com.seemsclever.outbox;

public interface OutboxEventHandler {
    String getType();

    void handle(String payload);
}
