package my.hazelcaststudy.event;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;

public class EventSample {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyListener myListener = new MyListener();
				HazelcastInstance instance = getInstance();
				IQueue queue = instance.getQueue("default");
				ISet set = instance.getSet("default");
				IMap map = instance.getMap("default");

				//listen for all added/updated/removed entries
				queue.addItemListener(myListener, true);
				set.addItemListener(myListener, true);
				map.addEntryListener(myListener, true);
				//listen for an entry with specific key
				map.addEntryListener(myListener, "keyobj", true);
			}
		}).start();

		Thread.sleep(5000);

		new Thread(new Runnable() {
			@Override
			public void run() {
				HazelcastInstance instance = getInstance();
				IQueue queue = instance.getQueue("default");
				ISet set = instance.getSet("default");
				IMap map = instance.getMap("default");

				try {
					queue.put("abc");
					set.add("abc");
					map.put("mykey", "myvalue");
				} catch (InterruptedException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			}
		}).start();
	}

	private static HazelcastInstance getInstance() {
		Config cfg = new Config();
		cfg.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		cfg.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
		cfg.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost");
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		return instance;
	}
}
