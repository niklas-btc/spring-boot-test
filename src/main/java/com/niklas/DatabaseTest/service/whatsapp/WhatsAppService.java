package com.niklas.DatabaseTest.service.whatsapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    String api_url =  "https://graph.facebook.com/v20.0/378116905390531/messages";

    // API Zugriffs Token
    String accessToken = "Platzhalter";

    // Initialisiere RestTemplate
    RestTemplate restTemplate = new RestTemplate(); // Tempalte für Rest-api zugriff (CURL)


    public String SendPlainTextMessage(String Phonenumber, String Message){
       // Daten für die POST-Anfrage (Plain Text)
        Map<String, Object> dataPlain = new HashMap<>();
        dataPlain.put("messaging_product", "whatsapp");
        dataPlain.put("recipient_type", "individual");
        dataPlain.put("to", Phonenumber);

        Map<String, Object> text = new HashMap<>();
        text.put("preview_url", true);
        text.put("body", Message);

        dataPlain.put("type", "text");
        dataPlain.put("text", text);

        return DoRequestToWhatsAppAPI(dataPlain);
    }

    public String SendInteractiveLinkMessage(String Phonenumber, String _title, String _message, String _footer, String _link){
        // Daten für die POST-Anfrage (Plain Text)
        Map<String, Object> dataPlain = new HashMap<>();
        dataPlain.put("messaging_product", "whatsapp");
        dataPlain.put("recipient_type", "individual");
        dataPlain.put("to", Phonenumber);
        dataPlain.put("type", "interactive");

        Map<String, Object> text = new HashMap<>();
        text.put("type", "cta_url");

        // Hashmap für Header Parameter
        Map<String, String> Header = new HashMap<>();
        Header.put("type", "text");
        Header.put("text", _title);

        // Hashmap für Body Parameter
        Map<String, String> Body = new HashMap<>();
        Body.put("text", _message);

        // Hashmap für Footer Parameter
        Map<String, String> Footer = new HashMap<>();
        Footer.put("text", _footer);

        // Hashmap für Action Parameter
        Map<String, String> Params = new HashMap<>();
        Params.put("display_text", "Zum Intranet");
        Params.put("url", _link);

        Map<String, Object> Action = new HashMap<>();
        Action.put("name", "cta_url");
        Action.put("parameters", Params);

        text.put("header", Header);
        text.put("body", Body);
        text.put("footer", Footer);
        text.put("action", Action);

        dataPlain.put("interactive", text);

        return DoRequestToWhatsAppAPI(dataPlain);
    }


    // Sende eine CURL-Request an die WhatsApp API
    private String DoRequestToWhatsAppAPI(Map<String, Object> dataPlain) {

        if(dataPlain == null) {
            return null;
        }

        // Erstelle den Header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Erstelle das HttpEntity-Objekt, das die Daten und die Header enthält
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(dataPlain, headers);

        // Führe die POST-Anfrage aus und speichere die Antwort
        return restTemplate.postForObject(api_url, request, String.class);
    }

}
