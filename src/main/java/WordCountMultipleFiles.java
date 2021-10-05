import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Main driver program to submit list of files to executor service.
public class WordCountMultipleFiles {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<FileReader> list = new ArrayList<>();
        List<Future<Map<String, Integer>>> futureResult = new ArrayList<>();
        Map<String, Integer> wordMap = new HashMap<>();

        //adding files to list
        String folderPath = "src/main/resources/";
        File file = new File(folderPath);
        File[] listOfFiles = {};
        if (!file.isFile()) {
            listOfFiles = file.listFiles();
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            list.add(new FileReader(listOfFiles[i].getAbsolutePath()));
        }

        //submitting task with callable
        for (int i = 0; i < list.size(); i++) {
            futureResult.add(executorService.submit(list.get(i)));
        }

        //calculating result
        for (int i = 0; i < futureResult.size(); i++) {
            Map<String, Integer> temp = futureResult.get(i).get();
            temp.forEach(
                    (k, v) -> {
                        if (wordMap.containsKey(k)) {
                            wordMap.put(k, wordMap.get(k) + v);
                        } else {
                            wordMap.put(k, v);
                        }
                    }
            );
        }

        System.out.println(wordMap);
        
        List<Map.Entry<String, Integer>> sortedList = HashMapHelper.sortMap(wordMap);
        HashMapHelper.printTopKFreqWords(sortedList, 2);
        executorService.shutdown();
    }
}

