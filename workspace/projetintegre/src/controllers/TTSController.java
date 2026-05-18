package controllers;

import java.io.File;

public class TTSController {

	private static boolean ttsEnabled = true;
	private static Process currentAudio = null;

	public static boolean isTtsEnabled() {
		return ttsEnabled;
	}

	public static void toggleTTS() {
		ttsEnabled = !ttsEnabled;
		if (!isTtsEnabled() && currentAudio != null) {
			currentAudio.destroy();
		}
	}

	//sous-dossier par defaut : audioLoginView
	public static void playSound(String fileName) {
		playSound("audioLoginView", fileName);
	}


	public static void playSound(String subfolder, String fileName) {
		if (!ttsEnabled)
			return;

		new Thread(() -> {
			try {
				if (currentAudio != null && currentAudio.isAlive()) {
					currentAudio.destroy();
				}

				String base = System.getProperty("user.dir");
				String os   = System.getProperty("os.name").toLowerCase();

				String resBase = base + File.separator + "src" + File.separator + "ressources";

				String path;
				if (os.contains("win")) {
					path = resBase + File.separator + subfolder + File.separator
							+ fileName.replace(".mp3", ".wav");
				} else {
					path = resBase + File.separator + subfolder + File.separator + fileName;
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