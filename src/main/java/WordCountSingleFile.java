/**
 * Problem Statement:
 * Design a data structure to find & print k most frequent words from a file
 * <p>
 * Proposed Solution #1:
 * Assumption: All words in the file can be accommodated in the memory.
 * <p>
 * Approach 1: Utilizing HashMap, Java 8 imperative style of programming using lambda, 
 * streams and new java.util.Comparator & Map Entry class
 * sorting the Hashmap by value & storing in a list
 * Finally, traverse through the treeMap and return the k words with maximum counts.
 * <p>
 * Pros: Simple solution & straightforward to implement
 * Cons:
 * 1) Not efficient
 * 2) The HashMap data structure is not synchronized.
 * If a map is accessed by multiple threads,
 * concurrently and at least one of the threads modifies the map structurally,
 * it must be synchronized externally.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordCountSingleFile {

    public static Map<String, Integer> buildMap(String filepath) {
        Map<String, Integer> wordMap = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] words = line.toLowerCase().replace(",", "").split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (wordMap.get(words[i]) == null) {
                        wordMap.put(words[i], 1);
                    } else {
                        int newValue = Integer.valueOf(String.valueOf(wordMap.get(words[i])));
                        newValue++;
                        wordMap.put(words[i], newValue);
                    }
                }
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException exception) {
            System.err.format("IOException: %s%n", exception);
        }
        return wordMap;
    }
    
    // Driver program to test above functions
    public static void main(String[] args) {
        String filepath = "src/main/resources/testFile2.txt";
        final int topKOccurrence = 2;
        //To measure elapsed time with nanosecond precision
        long startTime = System.nanoTime();
        
        Map<String, Integer> map = buildMap(filepath);
        List<Map.Entry<String, Integer>> sortedList = HashMapHelper.sortMap(map);
        HashMapHelper.printTopKFreqWords(sortedList, topKOccurrence);
    }
}

