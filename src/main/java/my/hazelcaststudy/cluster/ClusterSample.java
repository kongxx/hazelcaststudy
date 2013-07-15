package my.hazelcaststudy.cluster;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;

import java.util.Set;

public class ClusterSample {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final HazelcastInstance instance = getInstance();
				final Cluster cluster = instance.getCluster();
				cluster.addMembershipListener(new MembershipListener() {
					@Override
					public void memberAdded(MembershipEvent membersipEvent) {
						System.out.println("MemberAdded " + membersipEvent);

						System.out.println("========================================");
						Member localMember = cluster.getLocalMember();
						System.out.println("my inetAddress= " + localMember.getInetSocketAddress().getAddress());

						Set<Member> members = cluster.getMembers();
						for (Member member : members) {
							System.out.println("isLocalMember " + member.localMember());
							System.out.println("member.inetaddress " + member.getInetSocketAddress().getAddress());
							System.out.println("member.port " + member.getPort());
						}
						System.out.println("========================================");
					}

					@Override
					public void memberRemoved(MembershipEvent membersipEvent) {
						System.out.println("MemberRemoved " + membersipEvent);
					}
				});
			}
		}).start();

		Thread.sleep(5000);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HazelcastInstance instance = getInstance();
			}
		}).start();

		Thread.sleep(5000);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HazelcastInstance instance = getInstance();
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
