import java.util.PriorityQueue;

public class HeapHelper {
    public static void updateMinHeap(PriorityQueue<WordCount> minHeap, WordCount data, int maxSize) {
        if (minHeap.size() < maxSize) {
            minHeap.offer(data);
        } else if (Long.parseLong(String.valueOf(minHeap.peek().count)) < Long.parseLong(String.valueOf(data.count))) {
            minHeap.poll();
            minHeap.offer(data);
        }
    }
}
