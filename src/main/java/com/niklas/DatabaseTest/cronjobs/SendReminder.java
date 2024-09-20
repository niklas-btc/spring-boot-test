package com.niklas.DatabaseTest.cronjobs;

import com.niklas.DatabaseTest.database.User;
import com.niklas.DatabaseTest.database.UserRepository;
import com.niklas.DatabaseTest.service.whatsapp.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@EnableScheduling
public class SendReminder {

    @Autowired
    WhatsAppService whatsAppService;

    @Autowired
    UserRepository userRepository;

    // Dieser Cronjob sendet jeden Freitag um 8 Uhr eine Erinnerung zum Arbeitszeiten freigeben an alle Benutzer in der Datenbank,
    // welche eine Telefonnummer hinterlegt haben.

    //@Scheduled(cron = "0 * * * * *", zone = "Europe/Berlin") // Jede Minute (testing)

    @Scheduled(cron = "0 0 9 ? * FRI", zone = "Europe/Berlin") // Jeden Freitag um 8 Uhr
    private void sendReminder() {

        List<User> users = userRepository.findAll();

        // For-each Schleife, welche die gefundenen User durchläuft
        for (User user : users) {
            String phoneNumber = user.getPhonenumber();
            if(phoneNumber == null) {
                return;
            }

            String title = "Arbeitszeiten freigeben";
            String message = "ㅤ";
            String footer = "Automatische Erinnerung";
            String urlIntranet = "https://btcag.sharepoi<nt.com/sites/BTCAG";
            whatsAppService.SendInteractiveLinkMessage(phoneNumber, title, message, footer, urlIntranet);
        }

    }
}
