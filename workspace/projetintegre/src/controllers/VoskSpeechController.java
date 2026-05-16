package controllers;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Contrôleur optimisé pour la reconnaissance vocale.
 * Utilise une grammaire Vosk + un filtre de sécurité Java.
 */
public class VoskSpeechController {

    private static Model model;
    private static boolean isListening = false;

    // Liste blanche des mots autorisés pour éviter les hallucinations du modèle
    private static final List<String> ALLOWED_COMMANDS = Arrays.asList(
            "admin", "root", "login", "next", "password", "exit", "connect", "username", "send"
    );

    public static void initModel() {
        try {
            String base = System.getProperty("user.dir");
            String folderName = "vosk-model-small-en-us-0.15";

            // Vu ton screen, le modèle est dans le dossier 'lib' à la racine du projet
            File modelDir = new File(base, "lib" + File.separator + folderName);

            if (!modelDir.exists()) {
                // Sécurité : on tente aussi dans src/projetintegre/ressources au cas où
                modelDir = new File(base, "src/projetintegre/ressources/" + folderName);
            }

            if (!modelDir.exists()) {
                System.err.println("[VOSK] ERREUR : Modèle introuvable à : " + modelDir.getAbsolutePath());
                return;
            }

            System.out.println("[VOSK] Chargement du modèle depuis : " + modelDir.getAbsolutePath());
            model = new Model(modelDir.getAbsolutePath());
            System.out.println("[VOSK] Modèle chargé avec succès.");

        } catch (Exception e) {
            System.err.println("[VOSK] Échec du chargement : " + e.getMessage());
        }
    }

    public static void startListening(SpeechResultListener listener) {
        if (isListening || model == null) return;
        isListening = true;

        new Thread(() -> {
            // Format standard 16kHz Mono
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
                line.open(format);
                line.start();

                // On définit la grammaire JSON pour Vosk
                String grammar = "[\"admin\", \"root\", \"login\", \"next\", \"password\", \"exit\", \"username\", \"send\", \"connect\",\"[unk]\"]";

                try (Recognizer recognizer = new Recognizer(model, 16000, grammar)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    System.out.println("[VOSK] Écoute active (Mode Commande Strict)...");

                    while (isListening) {
                        bytesRead = line.read(buffer, 0, buffer.length);
                        if (bytesRead > 0) {
                            if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                                // Récupération du résultat après une pause
                                String resultJson = recognizer.getResult();
                                String text = parseVoskJson(resultJson).trim().toLowerCase();

                                // DOUBLE VÉRIFICATION : Vosk Grammar + Java List
                                if (!text.isEmpty() && ALLOWED_COMMANDS.contains(text)) {
                                    System.out.println("[VOSK] Commande validée : " + text);
                                    listener.onResult(text);
                                } else {
                                    // Optionnel : afficher ce qui a été rejeté pour le débug
                                    if(!text.equals("[unk]") && !text.isEmpty()) {
                                        System.out.println("[VOSK] Mot hors dictionnaire rejeté : " + text);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("[VOSK] Erreur micro : " + e.getMessage());
            } finally {
                isListening = false;
            }
        }).start();
    }

    public static void stopListening() {
        isListening = false;
    }

    private static String parseVoskJson(String json) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json);
            return (String) obj.get("text");
        } catch (Exception e) {
            return "";
        }
    }

    public interface SpeechResultListener {
        void onResult(String text);
    }
}