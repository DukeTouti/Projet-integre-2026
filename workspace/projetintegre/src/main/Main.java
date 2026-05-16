package main;

import javax.swing.SwingUtilities;
import controllers.VoskSpeechController;
import views.LoginView;


public class Main {


    public static void main(String[] args) {
        System.out.println("[MAIN] Démarrage de l'application...");


        new Thread(() -> {
            System.out.println("[MAIN] Initialisation du modèle Vosk en tâche de fond...");
            VoskSpeechController.initModel();
        }).start();


        SwingUtilities.invokeLater(() -> {
            System.out.println("[MAIN] Ouverture de l'écran de connexion...");
            new LoginView();
        });
    }
}