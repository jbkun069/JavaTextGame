public class StoryNode {
    String text;
    String[] options;
    String[] optionsText;

    public StoryNode(String text, String[] options, String[] optionsText) {
        this.text = text;
        this.options = options;
        this.optionsText = optionsText;
    }

    public boolean isEnding() {
        return options == null;
    }
}