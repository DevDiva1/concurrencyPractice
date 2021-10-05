import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WordCountConcurrentHashMapTest {

    //Testing addWord functionality in non-concurrent environment
    @Test
    void givenMap_whenAdded_thenWordAndFrequencyGetsAdded() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        final String word = "asdf";
        WordCountConcurrentHashMap.addWord(word, map);
        assertEquals(1, map.get("asdf"));

        WordCountConcurrentHashMap.addWord(word, map);
        assertEquals(2, map.get("asdf"));

        WordCountConcurrentHashMap.addWord(word, map);
        assertNull(map.get("word_not_present"));
    }

    List<String> terms = Arrays.asList(
            "early to bed",
            "early to rise",
            "makes you healthy",
            "early to bed",
            "early to rise",
            "makes you healthy and wealthy",
            "early to bed",
            "early to rise",
            "makes you healthy, wealthy and wise",
            "so wake up early silly girl!");

    @Test
    void givenList_WhenAddedToHashMap_thenWordAndFrequencyGetsAdded() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        for (String word : terms) {
            String[] updatedWord = word.replace(",", "").split(" ");
            for (String w : updatedWord) {
                WordCountConcurrentHashMap.addWord(w, map);
            }
        }
        assertEquals(7, map.get("early"));
        assertEquals(6, map.get("to"));
    }

    public void testTopMostFrequentlyOccurringWord(int topK) {
        final int topKFrequentWords = topK;

        TopKOccurrence topOccurrence = new TopKOccurrence(topKFrequentWords);
        terms.stream() //Utilizes multi-core hardware
                .flatMap(s -> Arrays.asList(s.split(" ")).stream())
                .collect(Collectors.toConcurrentMap(w -> w.toLowerCase(), w -> 1, Integer::sum))
                .forEach((s, integer) -> topOccurrence.add(new WordCount(s, integer)));

        System.out.println(topOccurrence);
        assertEquals(topKFrequentWords, topOccurrence.heap.size(), "Priority Queue Size");
    }

    //Testing addWord functionality in concurrent environment
    private void testAddWordInMultiThreadedEnv(final int threadCount, int topK) throws InterruptedException, ExecutionException {
        //set up
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<WordCountConcurrentHashMap> fileLists = new ArrayList<>();
        Future<ConcurrentHashMap<String, Integer>> futureResult = null;
        ConcurrentHashMap<String, Integer> resultMap;
        //adding files to fileLists
        String folderPath = "src/main/resources/";
        File file = new File(folderPath);
        File[] listOfFiles = {};
        if (!file.isFile()) {
            listOfFiles = file.listFiles();
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            fileLists.add(new WordCountConcurrentHashMap(listOfFiles[i].getAbsolutePath()));
        }
        //submitting task with callable
        for (int i = 0; i < fileLists.size(); i++) {
            futureResult = executorService.submit(fileLists.get(i));
        }
        resultMap = futureResult.get();
        PriorityQueue<WordCount> freqTopKWords = WordCountConcurrentHashMap.getFrequentlyUsedWords(resultMap, topK);
        topK = topK > resultMap.size() ? resultMap.size() : topK;

        //assertions
        assertNotNull(resultMap);
        assertEquals(topK, freqTopKWords.size(), "Priority Queue Size is equal to requested topK words");

        //tear down
        resultMap.clear();
        freqTopKWords.clear();
        executorService.shutdown();
    }

    @Test
    public void test01() throws ExecutionException, InterruptedException {
        testAddWordInMultiThreadedEnv(1, 3);
    }

    @Test
    public void test02() throws InterruptedException, ExecutionException {
        testAddWordInMultiThreadedEnv(2, 20);
    }

    @Test
    public void test04() throws InterruptedException, ExecutionException {
        testAddWordInMultiThreadedEnv(4, 30);
    }

    @Test
    public void test08() throws InterruptedException, ExecutionException {
        testAddWordInMultiThreadedEnv(8, 100);
    }

    @Test
    public void testFrequentlyOccurringWord() {
        testTopMostFrequentlyOccurringWord(1);
    }

    @Test
    public void testTopKFrequentlyOccurringWord() {
        testTopMostFrequentlyOccurringWord(10);
    }
}