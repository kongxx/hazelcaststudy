package my.hazelcaststudy.execution;

import com.hazelcast.config.Config;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class DistributedExecutionServer {

	public static void main(String[] args) throws Exception {
		HazelcastInstance instance = getInstance();
		Cluster cluster = instance.getCluster();
		System.setProperty("myaddress", cluster.getLocalMember().getInetSocketAddress().toString());
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
