package controllers;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;

public class VoskSpeechController {

	private static Model model;
	private static boolean isListening = false;

	public static void initModel() {
		try {
			String base = System.getProperty("user.dir");
			String folderName = "vosk-model-small-en-us-0.15";
			
			File projectDir = new File(base);
			File workspaceDir = projectDir.getParentFile(); 

			File modelDir = new File(workspaceDir, "lib" + File.separator + folderName);

			if (!modelDir.exists()) {
				System.err.println("[VOSK] ERREUR CRITIQUE : Le dossier n'est pas là.");
				System.err.println("[VOSK] Chemin absolu attendu : " + modelDir.getAbsolutePath());
				return;
			}

			System.out.println("[VOSK] Chargement du modèle depuis : " + modelDir.getAbsolutePath());
			model = new Model(modelDir.getAbsolutePath());
			System.out.println("[VOSK] Modèle chargé avec succès !");

		} catch (Exception e) {
			System.err.println("[VOSK] Échec du chargement : " + e.getMessage());
		}
	}

	public static void startListening(SpeechResultListener listener) {
		if (isListening || model == null)
			return;
		isListening = true;

		new Thread(() -> {
			AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
				line.open(format);
				line.start();
				
				// 1. Définition de la grammaire restreinte au format JSON array
				// Tu peux y ajouter "connect" ou "send" si ton interface en a besoin
				String grammar = "[\"root\", \"user\", \"username\", \"password\", \"mot de passe\", \"send\", \"john\", \"[unk]\"]";

				System.out.println("[VOSK] Écoute active (Mode Grammaire Restreinte)...");

				// 2. On passe la grammaire au constructeur du Recognizer
				try (Recognizer recognizer = new Recognizer(model, 16000, grammar)) {
					byte[] buffer = new byte[4096];
					int bytesRead;

					while (isListening) {
						bytesRead = line.read(buffer, 0, buffer.length);
						if (bytesRead > 0) {
							if (recognizer.acceptWaveForm(buffer, bytesRead)) {
								String resultJson = recognizer.getResult();
								String text = parseVoskJson(resultJson).trim().toLowerCase();

								// On envoie le texte uniquement s'il est valide et reconnu
								if (!text.isEmpty() && !text.equals("[unk]")) {
									System.out.println("[VOSK] Texte validé : " + text);
									listener.onResult(text);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.err.println("[VOSK] Erreur micro : " + e.getMessage());
			} catch (Throwable t) {
				System.err.println("[VOSK] Erreur native : " + t.getMessage());
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