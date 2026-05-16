package controllers;

import java.io.File;

public class TTSController {


    // Partage par toutes les vues (Login, Admin, PSH, avec le extends) dont on utilise private
    private static boolean ttsEnabled = true;
    private static Process currentAudio = null; //Au depart, rine

    //ON check si le user veut la saisit ou pas
    public static boolean isTtsEnabled() { return ttsEnabled; }


    public static void toggleTTS(){
        ttsEnabled = !ttsEnabled;//ON switch
        if (!isTtsEnabled() && currentAudio != null) { //currentAudio != null, donc question : processus audio a deja ete lance ?
            currentAudio.destroy();
        }
    }


    //WHILE BOUCLE tans que isTtsEnabled renvoie True/1
    public static void playSound(String fileName) {
        if (!ttsEnabled) return;

        new Thread(() -> {
            try {
                if (currentAudio != null && currentAudio.isAlive()) {
                    currentAudio.destroy();
                }

                String base = System.getProperty("user.dir");
                String os = System.getProperty("os.name").toLowerCase();

                // On définit le dossier racine des audios de login
                String resPath = base + File.separator + "src" + File.separator + "projetintegre" +
                        File.separator + "ressources" + File.separator + "audio" + File.separator + "login";

                String path;
                if (os.contains("win")) {
                    // Si tu es sur Windows, il cherche dans un sous-dossier spécifique ou convertit en .wav
                    path = resPath + File.separator + "audiologinwindows" + File.separator + fileName.replace(".mp3", ".wav");
                } else {
                    // SUR LINUX (Ton cas actuel) : On tape directement dans /login/
                    path = resPath + File.separator + fileName;
                }

                File f = new File(path);
                if (!f.exists()) {
                    System.err.println("[AUDIO] Fichier introuvable : " + path);
                    return;
                }

                if (os.contains("linux")) {
                    currentAudio = new ProcessBuilder("mpg123", "-q", path).inheritIO().start();
                } else if (os.contains("win")) {
                    currentAudio = new ProcessBuilder("powershell", "-c",
                            "(New-Object Media.SoundPlayer '" + path + "').PlaySync()").start();
                } else {
                    currentAudio = new ProcessBuilder("afplay", path).start();
                }

                currentAudio.waitFor();

            } catch (Exception e) {
                System.err.println("Erreur système audio : " + e.getMessage());
            }
        }).start();
    }
}