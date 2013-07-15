package my.hazelcaststudy.transaction;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Transaction;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class TransactionSample {

	public static void main(String[] args) throws Exception {
		HazelcastInstance hz = getInstance();
		Queue queue = hz.getQueue("myqueue");
		Set set = hz.getSet("myset");
		Map map = hz.getMap("mymap");

		Transaction tx = hz.getTransaction();
		tx.begin();
		try {
			// ...
			tx.commit();
		} catch (Throwable t) {
			tx.rollback();
		}
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
