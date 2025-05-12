import java.util.HashMap;
import java.util.Scanner;

public class Game {
    private static final HashMap<String, StoryNode> story = new HashMap<>();
    private static int health = 100;
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        initializeStory();
        playGame("start");
    }

    private static void initializeStory() {
        // Start
        story.put("start", new StoryNode(
                "You wake up in a dark forest. Health: " + health,
                new String[]{"look_around", "call_help"},
                new String[]{"1) Look around", "2) Call for help"}
        ));

        // Path 1
        story.put("look_around", new StoryNode(
                "You see berries. Some look poisonous. Health: " + health,
                new String[]{"eat_berries", "ignore_berries"},
                new String[]{"1) Eat the berries (-20 health)", "2) Ignore them"}
        ));

        // Path 2
        story.put("call_help", new StoryNode(
                "A wolf appears! Health: " + health,
                new String[]{"fight_wolf", "run_away"},
                new String[]{"1) Fight the wolf (-30 health)", "2) Run away"}
        ));

        // Endings
        story.put("eat_berries", new StoryNode(
                "The berries were poisonous! Final Health: " + (health-20) + "\nGAME OVER",
                null, null
        ));

        story.put("good_ending", new StoryNode(
                "You escaped safely! Final Health: " + health + "\nYOU WIN",
                null, null
        ));
    }

    private static void playGame(String nodeId) {
        StoryNode node = story.get(nodeId);
        System.out.println("\n" + node.text);

        if (node.isEnding()) return;

        for (String option : node.optionsText) {
            System.out.println(option);
        }

        int choice = getValidChoice(node.options.length);
        String nextNode = node.options[choice-1];

        // Handle health changes
        if (nextNode.equals("eat_berries")) health -= 20;
        if (nextNode.equals("fight_wolf")) health -= 30;

        playGame(nextNode);
    }

    private static int getValidChoice(int max) {
        while (true) {
            System.out.print("Choose: ");
            if (scan.hasNextInt()) {
                int choice = scan.nextInt();
                if (choice >= 1 && choice <= max) return choice;
            }
            System.out.println("Invalid input!");
            scan.next();
        }
    }
}