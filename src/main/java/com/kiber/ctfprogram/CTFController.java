package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.util.UUID;
import java.util.prefs.Preferences;





public class CTFController {

    @FXML
    private TaskController task1;
    @FXML
    private TaskController task2;
    @FXML
    private TaskController task3;
    @FXML
    private TaskController task4;
    @FXML
    private TaskController task5;
    @FXML
    private TaskController task6;
    @FXML
    private TaskController task7;
    @FXML
    private TaskController task8;
    @FXML
    private TaskController task9;
    @FXML
    private TaskController task10;

    @FXML
    private TextArea terminal;
    @FXML
    private TextField terminalEntry;
    @FXML
    private TabPane tabPane;

    private TaskController[] tasks;
    private ByteBuffer task9FlagInMemory;

    private long appStartNano;

    private static String formatDuration(long durationMs) {
        long totalSeconds = durationMs / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    private static final Preferences PREFS =
            Preferences.userRoot().node("com/kiber/ctfprogram");


    private boolean submissionInProgress = false;
    private UUID clientId;




    private static final String PREF_KEY_SUBMITTED = "submitted";
    private static final String PREF_KEY_CLIENT_ID = "client_id";

    private boolean hasSubmitted() {
        return PREFS.getBoolean(PREF_KEY_SUBMITTED, false);
    }

    private void markSubmitted() {
        PREFS.putBoolean(PREF_KEY_SUBMITTED, true);
        try { PREFS.flush(); } catch (Exception ignored) {}
    }

    private UUID getOrCreateClientId() {
        String existing = PREFS.get(PREF_KEY_CLIENT_ID, null);
        if (existing != null) {
            try { return UUID.fromString(existing); } catch (Exception ignored) {}
        }
        UUID id = UUID.randomUUID();
        PREFS.put(PREF_KEY_CLIENT_ID, id.toString());
        try { PREFS.flush(); } catch (Exception ignored) {}
        return id;
    }




    String[] titles = {
            "Task 1: Hidden in plain sight",
            "Task 2: Straight from the source",
            "Task 3: Beyond /help",
            "Task 4: Back to the Beginning",
            "Task 5: Packed Keepsake(task5.png)",
            "Task 6: Dog With Extra Baggage(task6.jpg)",
            "Task 7: Rooted twice, a Shared exponent, and an Awaited cipher",
            "Task 8: Read between the words(task8.txt)",
            "Task 9: Memory Surgery",
            "Task 10: elzzup eht fo eceip tsal ehT"
    };

    String[] contents = {
            "",
            "",
            "",
            "",
            "",
            "",
            "-304217013558624041351027872758584082997\n\n" + "-260715660134608413191060557032778444511\n\n" + "-65537\n\n" + "-50199638024253326945249950126264517520117620969902286825861374873874353011347",
            "",
            "",
            ""
    };

    String[] hints = {
            "It's dark in here.",
            "Look where clutter is filtered out.",
            "Read what the code says, not what the UI says.",
            "The beginning contains something you weren’t meant to see… unless you look differently.",
            "Names lie. Headers don’t. 50 4B says more than .png.",
            "There’s more here than RGB. Extract what the file is carrying.",
            "If you can rebuild the private key from what you’re given, the message will reveal itself.",
            "Not all characters in this file are visible.",
            "Access the memory. Look out for fragments",
            "Did you find all the fragments? Put them together how the task says"
    };

    String[] hashes = {
            "b9de59896c87e0d5a8ee2e299d2680d24f971ce4e195de231b002a55aa4d3c1c", "1a2891163e1bce944194712be2600bbe80f7b082f14e51833e6ea6d880b17a05",
            "8d2d4c3a2652c92afb982f854522a42d80d1c5d11ca4fd1f31b4bf7d4f7036c7", "352213d0d991291eed232847a798751a90036634d851b258304bade32f1f909d",
            "049b32783ea68eb88144e826a2b0b24456582e97a7fcdc4d6b9b139b533e8e58", "b448e076565f3f137d77fdfe91eb9256d9de5c7bcc85b4c6a94ee7aa71c4e835",
            "317f315158e151efa9f64da4d907a44e899b1987b2a2b99197dd8300a9a6ee2e", "74721c3000cb6681a9cd0721475141963a5974704aad9b2540c8bd98785a8c9c",
            "2cf62b1f4fc5d4d5faf8cd6350ecc64fdc7a75c7a85389903c90e293b4d549e2", "2218d2d36ebd4d1d36d8a92db3385b29872c6a5116407a3402ee5680fab0a5b3"
    };

    private static final int[] PUMPKIN_ENC = {
            113, 108, 102, 30, 109, 119, 16, 70, 27, 94, 94, 70, 79, 117, 90, 95, 71, 90, 65, 27, 68
    };


    private String getPumpkinFlag() {
        StringBuilder sb = new StringBuilder();
        for (int x : PUMPKIN_ENC) {
            sb.append((char) (x ^ 42)); // reverse the XOR
        }
        return sb.toString();
    }

    private void prepareTask9Flag() {
        if (task9FlagInMemory != null) return;

        String anchor = "__TASK9_FLAG_MEMORY_ANCHOR__";

        try (InputStream in = getClass().getResourceAsStream(
                "/com/kiber/ctfprogram/taskFiles/task9.bin")) {

            if (in == null) {
                logMessage("Task 9 flag resource missing.");
                return;
            }

            byte[] enc = in.readAllBytes();
            byte[] dec = new byte[enc.length];
            byte key = 0x37;

            for (int i = 0; i < enc.length; i++) {
                dec[i] = (byte) (enc[i] ^ (key ^ i));
            }

            String flag = new String(dec, StandardCharsets.UTF_8);

            String anchorPlusFlag = anchor + flag;

            byte[] combinedBytes = anchorPlusFlag.getBytes(StandardCharsets.UTF_8);
            task9FlagInMemory = ByteBuffer.wrap(combinedBytes);


        } catch (IOException e) {
            logMessage("Error loading Task 9 flag: " + e.getMessage());
        }
    }


    private void handlePumpkin() {
        logMessage(getPumpkinFlag() + " 6e 6e \n");
    }


    @FXML
    public void initialize() {


        appStartNano = System.nanoTime();
        clientId = getOrCreateClientId();

        tasks = new TaskController[]{
                task1, task2, task3, task4, task5,
                task6, task7, task8, task9, task10
        };


        for (TaskController t : tasks) {
            t.setParent(this);
        }

        for (int i = 0; i < tasks.length; ++i) {
            tasks[i].setTask(i, titles[i], contents[i], hints[i], hashes[i]);
        }

        prepareTask9Flag();

        terminalEntry.setOnAction(e -> {
            String cmd = terminalEntry.getText().trim();
            processCommand(cmd);
            terminalEntry.clear();
        });

        logMessage("Client ID: " + clientId);

    }


    public void logMessage(String message) {
        terminal.appendText(message + "\n");
    }

    public void processCommand(String command) {
        logMessage(">" + command + "\n");
        switch (command) {
            case "/completed": {
                StringBuilder sb = new StringBuilder("Completed tasks:\n");
                boolean any = false;
                for (TaskController t : tasks) {
                    if (t.isCompleted()) {
                        any = true;
                        sb.append(" - ").append(t.getTitle()).append("\n");
                    }
                }
                if (!any) {
                    sb.append(" (none yet)");
                }
                logMessage(sb.toString());
                break;
            }

            case "/reset": {
                for (TaskController t : tasks) {
                    t.reset();
                }
                logMessage("All tasks have been reset.");
                break;
            }

            case "/help": {
                StringBuilder sb = new StringBuilder();
                sb.append("Commands:\n")
                        .append(" /completed - list completed tasks\n")
                        .append(" /reset     - reset all tasks\n")
                        .append(" /help      - show this help\n")
                        .append(" /finalize  - check if all tasks are done and move to registration\n")
                        .append(" /leaderboard - check the top 10 fastest times (unlocked only after completing)");

                logMessage(sb.toString());
                break;
            }

            case "/pumpkin": {
                handlePumpkin();
                break;
            }

            case "/leaderboard": {
                if (!hasSubmitted()) {
                    logMessage("Submit first to unlock leaderboard (/finalize).");
                } else {
                    showLeaderboard();
                }
                break;
            }


            case "/finalize": {
                boolean allDone = true;
                StringBuilder sb = new StringBuilder();
                for (TaskController t : tasks) {
                    if (!t.isCompleted()) {
                        allDone = false;
                        sb.append("Not completed: ").append(t.getTitle()).append("\n");
                    }
                }

                if (!allDone) {
                    logMessage(sb.toString());
                    break;
                }

                if (hasSubmitted()) {
                    logMessage("You already submitted. Use /leaderboard");
                    break;
                }

                if (submissionInProgress) {
                    logMessage("Submission already in progress...");
                    break;
                }

                logMessage("All tasks completed! Valio!");
                initiateRegistration();
                break;
            }


            default:
                logMessage("Unknown command. Try /help");
        }
    }

    private void showLeaderboard() {
        String SUPABASE_RPC_URL =
                "https://axcmjoxpgpaoyddhwgjk.supabase.co/rest/v1/rpc/get_leaderboard";

        String SUPABASE_KEY =
                "sb_publishable_sWhVUoKsl57cwkdxfCkKkg_D2OZtNuU";

        String body = "{\"limit_count\":10}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_RPC_URL))
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> javafx.application.Platform.runLater(() -> {
                    int code = resp.statusCode();
                    if (code < 200 || code >= 300) {
                        logMessage("Leaderboard failed (code " + code + ")");
                        return;
                    }

                    String json = resp.body();
                    if (json == null || json.isBlank() || json.equals("[]")) {
                        logMessage("Leaderboard is empty.");
                        return;
                    }

                    logMessage("=== Top 10 (fastest) ===");

                    java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                            "\"username\"\\s*:\\s*\"(.*?)\".*?\"duration_ms\"\\s*:\\s*(\\d+)",
                            java.util.regex.Pattern.DOTALL
                    );

                    java.util.regex.Matcher m = p.matcher(json);

                    int rank = 1;
                    while (m.find() && rank <= 10) {
                        String username = m.group(1);
                        long durationMs = Long.parseLong(m.group(2));
                        logMessage(String.format("%2d) %-20s %s",
                                rank, username, formatDuration(durationMs)));
                        rank++;
                    }

                    if (rank == 1) {
                        logMessage("Could not parse leaderboard response.");
                    }
                }))
                .exceptionally(ex -> {
                    javafx.application.Platform.runLater(() ->
                            logMessage("Leaderboard failed: " + ex.getMessage()));
                    return null;
                });
    }


    private void initiateRegistration() {
        if (hasSubmitted()) {
            logMessage("You already submitted. Use /leaderboard");
            return;
        }
        if (submissionInProgress) {
            logMessage("Submission already in progress...");
            return;
        }
        submissionInProgress = true;

        long durationMs = (System.nanoTime() - appStartNano) / 1_000_000;

        TextInputDialog userDialog = new TextInputDialog();
        userDialog.setTitle("CTF Submission");
        userDialog.setHeaderText("Enter username");
        userDialog.setContentText("Username:");

        var userResult = userDialog.showAndWait();
        if (userResult.isEmpty() || userResult.get().trim().isEmpty()) {
            submissionInProgress = false;
            logMessage("Submission cancelled.");
            return;
        }
        String username = userResult.get().trim();

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("CTF Submission");
        emailDialog.setHeaderText("Enter email (optional)");
        emailDialog.setContentText("Email:");

        String email = emailDialog.showAndWait().map(String::trim).orElse("");

        String safeUsername = username.replace("\\", "\\\\").replace("\"", "\\\"");
        String safeEmail = email.replace("\\", "\\\\").replace("\"", "\\\"");

        String json = String.format("""
    {
      "client_id": "%s",
      "username": "%s",
      "email": "%s",
      "duration_ms": %d
    }
    """, clientId, safeUsername, safeEmail, durationMs);

        String SUPABASE_URL =
                "https://axcmjoxpgpaoyddhwgjk.supabase.co/rest/v1/ctf_submissions";

        String SUPABASE_KEY =
                "sb_publishable_sWhVUoKsl57cwkdxfCkKkg_D2OZtNuU";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL))
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> javafx.application.Platform.runLater(() -> {
                    submissionInProgress = false;

                    int code = resp.statusCode();

                    if (code >= 200 && code < 300) {
                        markSubmitted();
                        logMessage("Submission successful!");
                        logMessage("Your time: " + formatDuration(durationMs));
                        logMessage("Use /leaderboard to view Top 10.");
                        return;
                    }

                    if (code == 409) {
                        markSubmitted();
                        logMessage("Already submitted from this installation.");
                        logMessage("Use /leaderboard to view Top 10.");
                        return;
                    }

                    if (code == 401 || code == 403) {
                        logMessage("Submission rejected by server (permissions/RLS).");
                        return;
                    }

                    logMessage("Submission failed (code " + code + ").");
                }))
                .exceptionally(ex -> {
                    javafx.application.Platform.runLater(() -> {
                        submissionInProgress = false;
                        logMessage("Submission failed: " + ex.getMessage());
                    });
                    return null;
                });
    }


}

