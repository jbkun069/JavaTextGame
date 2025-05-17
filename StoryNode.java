import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the story with text and possible choices.
 */
public class StoryNode {
    private final String text;
    private final String[] options;
    private final String[] optionsText;
    private final int[] healthDeltas;

    private StoryNode(Builder builder) {
        this.text = builder.text;
        this.options = builder.options.toArray(new String[0]);
        this.optionsText = builder.optionsText.toArray(new String[0]);
        this.healthDeltas = builder.healthDeltas.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Gets the narrative text of the node.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the array of next node IDs.
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Gets the array of option descriptions.
     */
    public String[] getOptionsText() {
        return optionsText;
    }

    /**
     * Gets the array of health changes for each option.
     */
    public int[] getHealthDeltas() {
        return healthDeltas;
    }

    /**
     * Checks if this node is an ending (no further options).
     */
    public boolean isEnding() {
        return options.length == 0;
    }

    /**
     * Builder class for constructing StoryNode instances.
     */
    public static class Builder {
        private String text;
        private final List<String> options;
        private final List<String> optionsText;
        private final List<Integer> healthDeltas;

        public Builder() {
            this.options = new ArrayList<>();
            this.optionsText = new ArrayList<>();
            this.healthDeltas = new ArrayList<>();
        }

        /**
         * Sets the narrative text.
         */
        public Builder text(String text) {
            if (text == null) throw new IllegalArgumentException("Text cannot be null");
            this.text = text;
            return this;
        }

        /**
         * Adds an option with its ID, description, and health change.
         */
        public Builder option(String nodeId, String optionText, int healthDelta) {
            if (nodeId == null || optionText == null) {
                throw new IllegalArgumentException("Option ID and text cannot be null");
            }
            this.options.add(nodeId);
            this.optionsText.add(optionText);
            this.healthDeltas.add(healthDelta);
            return this;
        }

        /**
         * Builds the StoryNode instance.
         */
        public StoryNode build() {
            if (options.size() != optionsText.size() || options.size() != healthDeltas.size()) {
                throw new IllegalArgumentException("Options, optionsText, and healthDeltas must have the same length");
            }
            return new StoryNode(this);
        }
    }
}