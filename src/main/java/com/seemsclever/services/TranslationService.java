package com.seemsclever.services;

import com.seemsclever.RestClientConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TranslationService {

    public final RestClient restClient;

    public TranslationService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String translateToTatarLang(String text){
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/translate")
                            .queryParam("lang", 0)
                            .queryParam("text", text)
                            .build())
                    .header("User-Agent", "Mozilla/5.0")
                    .retrieve()
                    .body(String.class);

        } catch (Exception e){
            System.err.println("Translation error: " + e.getMessage());
            throw new RuntimeException("Ошибка при обращении к сервису перевода", e);
        }
    }
}
