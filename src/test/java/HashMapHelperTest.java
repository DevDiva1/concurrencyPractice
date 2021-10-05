import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HashMapHelperTest {
    @Test
    void givenMap_whenSortingByValues_thenSortedMap() {
        Map<String,Integer> map
                = new HashMap<>();
        map.put("early", 7);
        map.put("wise", 1);
        map.put("makes", 3);
        map.put("healthy", 2);
        map.put("girl", 10);


        //action
        List result = HashMapHelper.sortMap(map);

        //assert
        assertEquals("girl=10", result.get(0).toString());
        assertEquals("early=7", result.get(1).toString());
        assertEquals("makes=3", result.get(2).toString());
        assertEquals("healthy=2", result.get(3).toString());
        assertEquals("wise=1", result.get(4).toString());
    }
}