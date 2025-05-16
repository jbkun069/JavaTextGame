import java.util.HashMap;
import java.util.Scanner;

/**
 * Main class for the text adventure game, managing game flow and state.
 */
public class Game {
    private static final HashMap<String, StoryNode> STORY = new HashMap<>();
    private static final int INITIAL_HEALTH = 100;

    /**
     * Entry point for the game.
     */
    public static void main(String[] args) {
        GameState gameState = new GameState(INITIAL_HEALTH);
        initializeStory();
        try (Scanner scanner = new Scanner(System.in)) {
            playGame("start", scanner, gameState);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Initializes the story nodes with predefined paths and outcomes.
     */
    private static void initializeStory() {
        STORY.put("start", new StoryNode.Builder()
                .text("You wake up in a dark forest.")
                .option("look_around", "1) Look around")
                .option("call_help", "2) Call for help")
                .build());

        STORY.put("look_around", new StoryNode.Builder()
                .text("You see berries. Some look poisonous.")
                .option("eat_berries", "1) Eat the berries")
                .option("ignore_berries", "2) Ignore them")
                .build());

        STORY.put("call_help", new StoryNode.Builder()
                .text("A wolf appears!")
                .option("fight_wolf", "1) Fight the wolf")
                .option("run_away", "2) Run away")
                .build());

        STORY.put("ignore_berries", new StoryNode.Builder()
                .text("You walk past the berries and find a safe path.")
                .option("good_ending", "1) Continue")
                .build());

        STORY.put("fight_wolf", new StoryNode.Builder()
                .text("You fight bravely but get injured.")
                .option("good_ending", "1) Continue")
                .build());

        STORY.put("run_away", new StoryNode.Builder()
                .text("You escape the wolf unharmed.")
                .option("good_ending", "1) Continue")
                .build());

        STORY.put("eat_berries", new StoryNode.Builder()
                .text("The berries were poisonous! GAME OVER")
                .build());

        STORY.put("good_ending", new StoryNode.Builder()
                .text("You escaped safely! YOU WIN")
                .build());

        validateStoryNodes();
    }

    /**
     * Validates that all story nodes reference existing nodes.
     */
    private static void validateStoryNodes() {
        for (StoryNode node : STORY.values()) {
            if (!node.isEnding()) {
                for (String nextNode : node.getOptions()) {
                    if (!STORY.containsKey(nextNode)) {
                        throw new IllegalStateException("Missing story node: " + nextNode);
                    }
                }
            }
        }
    }

    /**
     * Manages the game loop, displaying nodes and handling player choices.
     */
    private static void playGame(String nodeId, Scanner scanner, GameState gameState) {
        StoryNode node = STORY.get(nodeId);
        System.out.println("\n" + node.getText());

        if (node.isEnding()) {
            System.out.println("Final Health: " + gameState.getHealth());
            return;
        }

        for (String option : node.getOptionsText()) {
            System.out.println(option);
        }

        int choice = getValidChoice(node.getOptions().length, scanner);
        String nextNode = node.getOptions()[choice - 1];

        // Apply health changes
        if (nextNode.equals("eat_berries")) {
            gameState.modifyHealth(-15);
        } else if (nextNode.equals("fight_wolf")) {
            gameState.modifyHealth(-50);
        }

        if (gameState.getHealth() <= 0) {
            System.out.println("\nYour health dropped to 0! GAME OVER");
            System.out.println("Final Health: " + gameState.getHealth());
            return;
        }

        playGame(nextNode, scanner, gameState);
    }

    /**
     * Retrieves a valid player choice within the specified range.
     */
    private static int getValidChoice(int max, Scanner scanner) {
        while (true) {
            System.out.print("Choose (1-" + max + "): ");
            try {
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    if (choice >= 1 && choice <= max) {
                        scanner.nextLine(); // Clear buffer
                        return choice;
                    }
                }
                System.out.println("Invalid input! Please enter a number between 1 and " + max + ".");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Manages the player's game state, such as health.
     */
    private static class GameState {
        private int health;

        GameState(int initialHealth) {
            this.health = initialHealth;
        }

        int getHealth() {
            return health;
        }

        void modifyHealth(int delta) {
            this.health = Math.max(0, health + delta);
        }
    }
}