package protocols_development;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {

	public static void main(String[] args) {
		Map<String, String> m = new ConcurrentHashMap<>();
		m.put("a", "a");
		m.put("b", "b");
		m.put("c", "c");
		m.put("d", "d");
		m.put("e", "e");
		m.put("f", "f");
		for (Map.Entry<String, String> e : m.entrySet()) {
			if(e.getValue().equals("b")){
				m.remove(e.getKey());
			}
			System.out.println(m.size());
		}

	}

}
