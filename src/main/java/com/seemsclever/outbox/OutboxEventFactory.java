package com.seemsclever.outbox;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OutboxEventFactory {
    private static final Map<String, OutboxEventHandler> mapHandlers = new HashMap<>();

    public OutboxEventFactory(List<OutboxEventHandler> handlers){
        for (OutboxEventHandler handler : handlers){
            mapHandlers.put(handler.getType(), handler);
        }
    }

    public OutboxEventHandler getHandler(String type){
        return mapHandlers.get(type);
    }
}
