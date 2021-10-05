import java.util.Comparator;
import java.util.PriorityQueue;

public class TopKOccurrence {

    public final PriorityQueue<WordCount> heap;
    public final int maxSize;

    public TopKOccurrence(int maxSize) {
        this.maxSize = maxSize;
        //Max-Heap
        this.heap = new PriorityQueue<>(Comparator.comparingInt((WordCount wc) -> wc.count));
    }

    public void add(WordCount data) {
        if (heap.size() < maxSize) {
            heap.offer(data);
        } else if (heap.peek().count < data.count) {
            heap.poll();
            heap.offer(data);
        }
    }

    @Override
    public String toString() {
        return "TopOccurrence{" + "minHeap=" + heap + ", maxSize=" + maxSize + '}';
    }
}