import java.util.HashMap;
import java.util.Scanner;

public class Game {
    private static final HashMap<String, StoryNode> story = new HashMap<>();
    private static int health = 100;

    public static void main(String[] args) {
        initializeStory();
        try (Scanner scan = new Scanner(System.in)) {
            playGame("start", scan);
        }
    }

    private static void initializeStory() {
        // Start
        story.put("start", new StoryNode(
                "You wake up in a dark forest.",
                new String[]{"look_around", "call_help"},
                new String[]{"1) Look around", "2) Call for help"}
        ));

        // Path 1
        story.put("look_around", new StoryNode(
                "You see berries. Some look poisonous.",
                new String[]{"eat_berries", "ignore_berries"},
                new String[]{"1) Eat the berries", "2) Ignore them"}
        ));

        // Path 2
        story.put("call_help", new StoryNode(
                "A wolf appears!",
                new String[]{"fight_wolf", "run_away"},
                new String[]{"1) Fight the wolf", "2) Run away"}
        ));

        // Additional Nodes
        story.put("ignore_berries", new StoryNode(
                "You walk past the berries and find a safe path.",
                new String[]{"good_ending"},
                new String[]{"1) Continue"}
        ));

        story.put("fight_wolf", new StoryNode(
                "You fight bravely but get injured.",
                new String[]{"good_ending"},
                new String[]{"1) Continue"}
        ));

        story.put("run_away", new StoryNode(
                "You escape the wolf unharmed.",
                new String[]{"good_ending"},
                new String[]{"1) Continue"}
        ));

        // Endings
        story.put("eat_berries", new StoryNode(
                "The berries were poisonous! GAME OVER",
                null, null
        ));

        story.put("good_ending", new StoryNode(
                "You escaped safely! YOU WIN",
                null, null
        ));

        // Validate story nodes
        validateStoryNodes();
    }

    private static void validateStoryNodes() {
        for (StoryNode node : story.values()) {
            if (!node.isEnding()) {
                for (String nextNode : node.getOptions()) {
                    if (!story.containsKey(nextNode)) {
                        throw new IllegalStateException("Missing story node: " + nextNode);
                    }
                }
            }
        }
    }

    private static void playGame(String nodeId, Scanner scan) {
        StoryNode node = story.get(nodeId);
        System.out.println("\n" + node.getText() + " Health: " + health);

        if (node.isEnding()) {
            System.out.println("Final Health: " + health);
            return;
        }

        for (String option : node.getOptionsText()) {
            System.out.println(option);
        }

        int choice = getValidChoice(node.getOptions().length, scan);
        String nextNode = node.getOptions()[choice - 1];

        // Handle health changes
        if (nextNode.equals("eat_berries")) health -= 20;
        if (nextNode.equals("fight_wolf")) health -= 30;

        if (health <= 0) {
            System.out.println("\nYour health dropped to 0! GAME OVER");
            System.out.println("Final Health: " + health);
            return;
        }

        playGame(nextNode, scan);
    }

    private static int getValidChoice(int max, Scanner scan) {
        while (true) {
            System.out.print("Choose (1-" + max + "): ");
            if (scan.hasNextInt()) {
                int choice = scan.nextInt();
                if (choice >= 1 && choice <= max) {
                    scan.nextLine(); // Clear buffer
                    return choice;
                }
            }
            System.out.println("Invalid input! Please enter a number between 1 and " + max + ".");
            scan.nextLine(); // Clear invalid input
        }
    }
}