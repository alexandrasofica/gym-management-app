package service;

import model.FitnessGoal;
import model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Clasa reprezintă serviciul care comunică cu un AI real
 * trimite datele utilizatorului + obiectivele fitness
 * primește plan de dietă + antrenament generat de AI
 */
public class AiPlanService {
	/**
	 * Adresa de bază a API-ului HuggingFace Router
	 */
    private static final String HF_BASE_URL = "https://router.huggingface.co/v1";
    private static final String CHAT_COMPLETIONS_URL = HF_BASE_URL + "/chat/completions";

    /**
     * Modelul AI folosit
     * rulează pe HuggingFace Inference
     */
    private static final String MODEL = "HuggingFaceTB/SmolLM3-3B:hf-inference"; 
    /**
     * folosit pentru a trimite request-uri către AI
     */
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();
    /**
     * Primește datele utilizatorului
     * Apelează AI-ul
     * @param user
     * @param goal
     * @param abdomen
     * @param foodPreferences
     * @param allergiesIgnored
     * @return un text String cu planul generat
     * @throws IOException
     * @throws InterruptedException
     */
    public String generateAiPlan(User user,
                                 FitnessGoal goal,
                                 boolean abdomen,
                                 String foodPreferences,
                                 String allergiesIgnored) throws IOException, InterruptedException {

        String hfToken = System.getenv("HF_API_KEY");
        if (hfToken == null || hfToken.isBlank()) {
            throw new IllegalStateException("Lipsește HF_API_KEY în Environment Variables.");
        }
        /**
         * Construiește textul pentru AI
         */
        String prompt = buildPrompt(goal, abdomen, foodPreferences);
        /**
         * Creez un request JSON
         */
        String bodyJson = "{"
                + "\"model\":\"" + escapeJson(MODEL) + "\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\"" + escapeJson(prompt) + "\"}],"
                + "\"temperature\":0.7,"
                + "\"max_tokens\":800"
                + "}";
        /**
         * Trimite JSON-UL către API-UL AI
         */
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(CHAT_COMPLETIONS_URL))
                .timeout(Duration.ofSeconds(90))
                .header("Authorization", "Bearer " + hfToken)
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson, StandardCharsets.UTF_8))
                .build();
        /**
         * Așteaptă răspunsul de la server
         * Răspunsul este citit ca string (JSON text)
         */
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        /**
         * Pentru debugging, afișează:
         * codul HTTP (200, 401, 500 etc.)
         * conținutul răspunsului (JSON-ul complet)
         */
        System.out.println("HF ROUTER STATUS = " + resp.statusCode());
        System.out.println("HF ROUTER BODY = " + resp.body());
        
        /**
         * verifică dacă request-ul a eșuat
         */
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
            throw new IOException("HF Router error. HTTP " + resp.statusCode() + " Body: " + resp.body());
        }
        /**
         * extrage textul generat de AI 
         */
        String content = extractAssistantContent(resp.body());
        /**
         * Verificare finală
         */
        if (content == null || content.isBlank()) {
            throw new IOException("Nu am putut extrage message.content. Body: " + resp.body());
        }

        return content.trim();
    }
    /**
     * Construiește textul care este trimis AI-ului
     * @param goal 
     * @param abdomen 
     * @param foodPreferences
     * @return planul personalizat de dietă și abdomen
     */
    private String buildPrompt(FitnessGoal goal, boolean abdomen, String foodPreferences) {
        String prefs = (foodPreferences == null || foodPreferences.isBlank()) ? "N/A" : foodPreferences;

        return "Creează un plan personalizat de dietă și antrenament. Scrie în Română\n\n"
                + "Date:\n"
                + "- Greutate curentă: " + goal.getCurrentWeight() + " kg\n"
                + "- Greutate țintă: " + goal.getTargetWeight() + " kg\n"
                + "- Înălțime: " + goal.getHeight() + " cm\n"
                + "- Nivel activitate: " + goal.getActivityLevel() + "\n"
                + "- Obiectiv: " + goal.getGoalType() + "\n"
                + "- Abdomen inclus: " + (abdomen ? "DA" : "NU") + "\n"
                + "- Preferințe alimentare: " + prefs + "\n\n"
                + "Returnează structurat, cu titluri:\n"
                + "1) PLAN DIETĂ (1 zi)\n"
                + "2) PLAN ANTRENAMENT (7 zile)\n"
                + "3) OBIECTIVE SĂPTĂMÂNALE (3-5 SMART)\n";
    }

    /**
     * Extrage textul generat de AI din răspunsul JSON
     * @param json
     * @return textul generat de AI
     */
    private String extractAssistantContent(String json) {
        Pattern p = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (!m.find()) return null;
        return unescapeJson(m.group(1));
    }
    /**
     * Metodă pentru transformarea textului
     * @param s
     * @return textul transformat compatibil cu JSON
     */
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
    /**
     * Metodă care transformă textul în format normal
     * @param s
     * @return textul în format normal
     */
    private String unescapeJson(String s) {
        return s.replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\r", "\r")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
