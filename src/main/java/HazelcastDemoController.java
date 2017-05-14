import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ReplicatedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peiji on 14/5/2017.
 */

@RestController()
public class HazelcastDemoController {

    @Autowired
    HazelcastInstance hazelcastInstance;

    @GetMapping("/data")
    public String getTestData() {
        return "test string";
    }

    @RequestMapping("/test")
    public List<String> getData() {
        List<String> results = new ArrayList<>();

        for(int i=0;i<5;i++) {
            results.add("test"+i);
        }

        IMap<String,String> map = hazelcastInstance.getMap("test");

        System.out.println("Map size = " + map.size());

        for(ReplicatedMap.Entry<String, String> entry: map.entrySet()) {
            System.out.println(entry.getValue());
            results.add(entry.getValue());

        }

        return results;
    }
}
