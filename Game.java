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
                .option("look_around", "Look around", 0)
                .option("call_help", "Call for help", 0)
                .build());

        STORY.put("look_around", new StoryNode.Builder()
                .text("You see berries. Some look poisonous.")
                .option("eat_berries", "Eat the berries", -20)
                .option("ignore_berries", "Ignore them", 0)
                .build());

        STORY.put("call_help", new StoryNode.Builder()
                .text("A wolf appears!")
                .option("fight_wolf", "Fight the wolf", -50)
                .option("run_away", "Run away", 0)
                .build());

        STORY.put("ignore_berries", new StoryNode.Builder()
                .text("You walk past the berries and find a safe path.")
                .option("good_ending", "Continue", 0)
                .build());

        STORY.put("fight_wolf", new StoryNode.Builder()
                .text("You fight bravely but get injured.")
                .option("good_ending", "Continue", 0)
                .build());

        STORY.put("run_away", new StoryNode.Builder()
                .text("You escape the wolf unharmed.")
                .option("good_ending", "Continue", 0)
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

        // Display options with dynamically generated numbers
        String[] optionsText = node.getOptionsText();
        for (int i = 0; i < optionsText.length; i++) {
            System.out.println((i + 1) + ") " + optionsText[i]);
        }

        int choice = getValidChoice(optionsText.length, scanner);
        String nextNode = node.getOptions()[choice - 1];
        int healthDelta = node.getHealthDeltas()[choice - 1];
        gameState.modifyHealth(healthDelta);

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
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please enter a number between 1 and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
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