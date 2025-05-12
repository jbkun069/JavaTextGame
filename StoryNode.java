public class StoryNode {
    private final String text;
    private final String[] options;
    private final String[] optionsText;

    public StoryNode(String text, String[] options, String[] optionsText) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null");
        if ((options == null) != (optionsText == null)) {
            throw new IllegalArgumentException("Options and optionsText must both be null or non-null");
        }
        if (options != null && options.length != optionsText.length) {
            throw new IllegalArgumentException("Options and optionsText must have the same length");
        }
        this.text = text;
        this.options = options;
        this.optionsText = optionsText;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getOptionsText() {
        return optionsText;
    }

    public boolean isEnding() {
        return options == null;
    }
}