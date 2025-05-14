import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the story with text and possible choices.
 */
public class StoryNode {
    private final String text;
    private final String[] options;
    private final String[] optionsText;

    private StoryNode(Builder builder) {
        this.text = builder.text;
        this.options = builder.options.toArray(new String[0]);
        this.optionsText = builder.optionsText.toArray(new String[0]);
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
        private final List<String> options; // Changed from List<eString>
        private final List<String> optionsText;

        public Builder() {
            this.options = new ArrayList<>();
            this.optionsText = new ArrayList<>();
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
         * Adds an option with its ID and description.
         */
        public Builder option(String nodeId, String optionText) {
            if (nodeId == null || optionText == null) {
                throw new IllegalArgumentException("Option ID and text cannot be null");
            }
            this.options.add(nodeId);
            this.optionsText.add(optionText);
            return this;
        }

        /**
         * Builds the StoryNode instance.
         */
        public StoryNode build() {
            if (options.size() != optionsText.size()) {
                throw new IllegalArgumentException("Options and optionsText must have the same length");
            }
            return new StoryNode(this);
        }
    }
}