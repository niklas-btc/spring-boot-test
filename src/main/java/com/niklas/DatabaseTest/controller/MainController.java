package com.niklas.DatabaseTest.controller;

import com.niklas.DatabaseTest.database.User;
import com.niklas.DatabaseTest.database.UserRepository;
import com.niklas.DatabaseTest.service.email.EmailService;
import com.niklas.DatabaseTest.service.whatsapp.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    private EmailService emailService; // E-Mail Service Objekt, um funktionen zum E-Mail senden aus der EmailService Klasse zu erreichen

    // Bei Aufruf der Seite wird eine über die EmailService-Klasse gesendet
    @RequestMapping("/sendemail")
    @ResponseBody // ← Zeige das den Wert im return direkt an, statt eine HTML-Datei auszulesen
    public String getEmailSite(){
        try {
            emailService.sendHTMLEmail("niklas.szach@btc-ag.com", "Ich bin der Titel", "<span style=\"font-weight: bold;\">Ich bin der HTML-Content</span>");
        }catch (Exception e){
            e.printStackTrace();
            return "Etwas ist Schiefgelaufen";
        }
        return "E-Mail wurde gesendet";
    }


    @Autowired
    private WhatsAppService whatsAppService;

    @RequestMapping("sendReminder/{UID}")
    @ResponseBody
    public String getWhatsAppSite(@PathVariable String UID){

        try {
            int parsedUID = Integer.parseInt(UID);

            User user = userRepository.findByUID(parsedUID);
            if(user == null) {
               throw new Exception();
            }

            String phoneNumber = user.getPhonenumber();
            String title = "Arbeitszeiten freigeben";
            String message = "ㅤ";
            String footer = "Automatische Erinnerung";
            String urlIntranet = "https://btcag.sharepoi<nt.com/sites/BTCAG";
            return "Erinnerung erfolgreich gesendet.<br><br>CURL Antwort: " + whatsAppService.SendInteractiveLinkMessage(phoneNumber, title, message, footer, urlIntranet);

        }catch (Exception e){
            return "Etwas ist Schiefgelaufen";
        }
    }


    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/profile/{userID}")
    public String getProfile(@PathVariable long userID, Model model) {
        User user = userRepository.findByUID((int) userID);
        if (user == null) {
            model.addAttribute("error", "Benutzer nicht gefunden");
            return "profile";  // Hier sollte die Profilseite auch den Fehler anzeigen
        }
        model.addAttribute("user", user);
        return "profile";
    }


    // RedirectAttributes redirectAttributes, ermöglichen es Attribute über einen redirect zu übergeben
    // Verarbeiten einer POST-Anfrage, um Profilinformationen zu ändern
    @PostMapping("/profile/{userID}/update")
    public String update(@PathVariable int userID,
                         @RequestParam(defaultValue = "") String new_username,
                         @RequestParam(defaultValue = "") String new_mail,
                         Model model, RedirectAttributes redirectAttributes) {
        boolean hasChanges = false;
        User user = userRepository.findByUID(userID);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Benutzer nicht gefunden");
            return "redirect:/profile/" + userID;
        }

        // Prüfen, ob der neue Benutzername leer ist
        if (new_username.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Der Benutzername darf nicht leer sein");
        }
        // Benutzername aktualisieren, wenn er sich geändert hat
        else if (!user.getUsername().equals(new_username)) {
            user.setUsername(new_username);
            hasChanges = true;
        }

        // E-Mail aktualisieren, wenn sie sich geändert hat
        if (!new_mail.isEmpty() && !user.getEmail().equals(new_mail)) {
            user.setEmail(new_mail);
            hasChanges = true;
        }

        // Wenn Änderungen vorhanden sind, speichern
        if (hasChanges) {
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Profil erfolgreich aktualisiert");
        }

        // Profilseite nach erfolgreicher Aktualisierung neu laden
        return "redirect:/profile/" + userID;
    }
}
