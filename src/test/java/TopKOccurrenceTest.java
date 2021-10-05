import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TopKOccurrenceTest {
    @Test
    public void givenHeapSize1_whenAdded_thenTopKFromMinHeap() {
        //given
        final int maxHeapSize = 1;
        TopKOccurrence top1FrequentWords = new TopKOccurrence(maxHeapSize);

        //action
        top1FrequentWords.add(new WordCount("fintech", 6));
        top1FrequentWords.add(new WordCount("female", 7));
        top1FrequentWords.add(new WordCount("fierce", 5));

        //assert
        assertEquals("female",top1FrequentWords.heap.poll().word);
    }

    @Test
    public void givenHeapSize2_whenAdded_thenTopKFromMinHeap() {

        //given
        final int maxHeapSize = 2;
        TopKOccurrence top2FrequentWords = new TopKOccurrence(maxHeapSize);

        //action
        top2FrequentWords.add(new WordCount("fintech", 6));
        top2FrequentWords.add(new WordCount("female", 7));
        top2FrequentWords.add(new WordCount("fierce", 8));

        //assert
        assertEquals("female",top2FrequentWords.heap.poll().word);
        assertEquals("fierce",top2FrequentWords.heap.poll().word);
    }

}