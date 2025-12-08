package com.kiber.ctfprogram;

import javafx.fxml.FXML;
import javafx.scene.control.*;
public class CTFController {

    @FXML private TaskController task1;
    @FXML private TaskController task2;
    @FXML private TaskController task3;
    @FXML private TaskController task4;
    @FXML private TaskController task5;
    @FXML private TaskController task6;
    @FXML private TaskController task7;
    @FXML private TaskController task8;
    @FXML private TaskController task9;
    @FXML private TaskController task10;

    @FXML private TextArea terminal;
    @FXML private TextField terminalEntry;
    @FXML private TabPane tabPane;
    public int task9Val = 0;

    private TaskController[] tasks;

    String[] titles = {
            "Task 1: Hidden in plain sight",
            "Task 2: Straight from the source",
            "Task 3: Beyond /help",
            "Task 4: Back to the Beginning",
            "Task 5: Packed Keepsake(task5.png)",
            "Task 6: Dog With Extra Baggage(task6.jpg)",
            "Task 7: Wrong charset(task7.txt)",
            "Task 8: Retrograde Signature Archive",
            "Task 9: Memory Surgery",
            "Task 10: 63 elzzup eht fo eceip tsal ehT"
    };

    String[] contents = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "1012246681\n" + "101\n" + "445449751 862257758 612944151 137404964 513660317 49128542 595042419 718422220",
            "Current value: " + task9Val,
            ""
    };

    String[] hints = {
            "It's dark in here.",
            "Look where clutter is filtered out.",
            "Read what the code says, not what the UI says.",
            "The beginning contains something you weren’t meant to see… unless you look differently.",
            "Names lie. Headers don’t. 50 4B says more than .png.",
            "There’s more here than RGB. Extract what the file is carrying.",
            "If it looks like nonsense, you're probably reading it in the wrong encoding.",
            "One large modulus, one exponent, and many blocks.",
            "Watching the program isn’t enough. Change it.",
            "Did you find all the fragments? Put them together how the task says"
    };

    String[] hashes = {
            "b9de59896c87e0d5a8ee2e299d2680d24f971ce4e195de231b002a55aa4d3c1c", "1a2891163e1bce944194712be2600bbe80f7b082f14e51833e6ea6d880b17a05",
            "8d2d4c3a2652c92afb982f854522a42d80d1c5d11ca4fd1f31b4bf7d4f7036c7", "352213d0d991291eed232847a798751a90036634d851b258304bade32f1f909d",
            "049b32783ea68eb88144e826a2b0b24456582e97a7fcdc4d6b9b139b533e8e58", "b448e076565f3f137d77fdfe91eb9256d9de5c7bcc85b4c6a94ee7aa71c4e835",
            "c199e014a3e6cd43abe993c8c4464fbab639ee1c781da7e3c35ce2b86c718355", "317f315158e151efa9f64da4d907a44e899b1987b2a2b99197dd8300a9a6ee2e",
            "FLAG9", "2218d2d36ebd4d1d36d8a92db3385b29872c6a5116407a3402ee5680fab0a5b3"
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

                                                                                                                                                                                        private void handlePumpkin() {
                                                                                                                                                                                            logMessage(getPumpkinFlag() + " 6e 6e \n");
                                                                                                                                                                                        }


    @FXML
    public void initialize() {

        tasks = new TaskController[] {
                task1, task2, task3, task4, task5,
                task6, task7, task8, task9, task10
        };


        for (TaskController t : tasks) {
            t.setParent(this);
        }

        for (int i = 0; i < tasks.length; ++i) {
            tasks[i].setTask(i, titles[i],contents[i], hints[i], hashes[i]);
        }

        terminalEntry.setOnAction(e -> {
            String cmd = terminalEntry.getText().trim();
            processCommand(cmd);
            terminalEntry.clear();
        });
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
                        .append(" /finalize  - check if all tasks are done and move to registration\n");
                logMessage(sb.toString());
                break;
            }

            case "/pumpkin": {
                handlePumpkin();
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
                if (allDone) {
                    logMessage("All tasks completed! Valio!");
                    initiateRegistration();
                } else {
                    logMessage(sb.toString());
                }
                break;
            }

            default:
                logMessage("Unknown command. Try /help");
        }
    }

    private void initiateRegistration() {
    }
}
