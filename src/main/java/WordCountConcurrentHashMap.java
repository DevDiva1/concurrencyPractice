import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class WordCountConcurrentHashMap implements Callable<ConcurrentHashMap<String, Integer>> {
    final String fileName;
    private volatile ConcurrentHashMap<String, Integer> map;

    public static Integer addWord(String word, ConcurrentHashMap<String, Integer> map) {
        //compute is atomic operations in the ConcurrentHashMap class
        return map.compute(word, (k, v) -> v == null ? 1 : v + 1);
    }

    WordCountConcurrentHashMap(String fileName, ConcurrentHashMap<String, Integer> map) {
        this.fileName = fileName;
        this.map = map;
    }

    @Override
    public ConcurrentHashMap<String, Integer> call() throws Exception {
        //Read files in different threads line by line
        Stream<String> stream = Files.lines(Paths.get(this.fileName));
        stream.forEach(
                line -> {
                    String data[] = line.replace(",", "").split(" ");
                    for (int i = 0; i < data.length; i++) {
                        addWord(data[i], map);
                    }
                }
        );
        return map;
    }

    public static PriorityQueue<WordCount> getFrequentlyUsedWords(ConcurrentHashMap<String, Integer> resultMap, int topK) {
        //Sort using Priority Queue
        TopKOccurrence topKOccurrence = new TopKOccurrence(topK);
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            topKOccurrence.add(new WordCount(entry.getKey(),entry.getValue()));
        }
        System.out.println(topKOccurrence);
        return topKOccurrence.heap;
    }
}

