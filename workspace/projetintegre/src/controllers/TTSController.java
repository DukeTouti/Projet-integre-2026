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

                String resBase = base + File.separator + "workspace" + File.separator + "projetintegre" +
                        File.separator + "src" + File.separator + "ressources";

                String path;
                if (os.contains("win")) {
                    path = resBase + File.separator + "audiologinwindows" + File.separator + fileName.replace(".mp3", ".wav");
                } else {
                    path = resBase + File.separator + "audio" + File.separator + "login" + File.separator + fileName;
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