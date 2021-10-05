import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapHelper {
    public static List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list =
                map.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .collect(Collectors.toList());
        return list;
    }

    public static List<Map.Entry<String, Integer>> printTopKFreqWords(List<Map.Entry<String, Integer>> sortedList, int topKfreq) {
        if (topKfreq > sortedList.size()) {
            topKfreq = sortedList.size();
        }
        List<Map.Entry<String, Integer>> list = sortedList.stream().limit(topKfreq)
                .collect(Collectors.toList());

        for (int i = 0; i < topKfreq; i++) {
            System.out.println(list.get(i));
        }

        return list;
    }
}
