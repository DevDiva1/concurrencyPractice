import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Problem Statement:
 * Design a data structure to find & print k most frequent words from a file
 * Approach 2 : Using a hashmap and min-heap
 * <p>
 * Algorithm
 * Stream the words into a HashMap & tracking Top K occurring Words Using Binary Min Heap
 * Check if the heap size if less than K - then add the new word count to min heap. Otherwise
 * Check if the peek element (that is minimum value in binary min heap) is less than the new word count,
 * if it is, then remove the existing number and insert the new word count into min heap.
 **/

public class WordCountUsingMaxHeap {

    public static PriorityQueue<WordCount> buildMapAndHeap(String filepath, int topFreq) {
        Map<String, Integer> wordMap = new HashMap();
        PriorityQueue<WordCount> minHeap = new PriorityQueue<>(Comparator.comparingInt(wc -> wc.count));

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] words = line.toLowerCase().replace(",", "").split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (wordMap.get(words[i]) == null) {
                        wordMap.put(words[i], 1);
                        updateMinHeap(minHeap, new WordCount(words[i], 1), topFreq);
                    } else {
                        int newValue = Integer.valueOf(String.valueOf(wordMap.get(words[i])));
                        newValue++;
                        wordMap.put(words[i], newValue);
                        updateMinHeap(minHeap, new WordCount(words[i], newValue), topFreq);
                    }
                }
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException exception) {
            System.err.format("IOException: %s%n", exception);
        }
        return minHeap;
    }

    public static void updateMinHeap(PriorityQueue<WordCount> minHeap, WordCount data, int maxSize) {
        if (minHeap.size() < maxSize) {
            minHeap.offer(data);
        } else if (minHeap.peek().count < data.count) {
            minHeap.poll();
            minHeap.offer(data);
        }
    }

    static class WordCount {
        protected final String word;
        protected final int count;

        WordCount(String word, int count) {
            this.word = word;
            this.count = count;
        }
        @Override
        public String toString() {
            return "{" + "word='" + word + '\'' + ", count=" + count + '}'+"\r\n";
        }
    }

    public static void main(String[] args) {
        String filepath = "src/main/resources/testFile1.txt";
        PriorityQueue<WordCount> minHeap = buildMapAndHeap(filepath, 3);
        System.out.println(minHeap);
    }
}

