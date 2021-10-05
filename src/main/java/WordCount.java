
public class WordCount{
    public final String word;
    public final int count;

    WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" + "word='" + word + '\'' + ", count=" + count + '}' + "\r\n";
    }
}