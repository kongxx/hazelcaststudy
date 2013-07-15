package my.hazelcaststudy.lock;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.locks.Lock;

public class LockSample {
	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				test1();
			}
		}).start();

		Thread.sleep(5000);

		new Thread(new Runnable() {
			@Override
			public void run() {
				test2();
			}
		}).start();
	}

	private static void test1() {
		Lock lock = getInstance().getLock("mylock");
		lock.lock();
		try {
			System.out.println("test1");
			System.out.println("start sleep");
			Thread.sleep(30 * 1000);
			System.out.println("stop sleep");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private static void test2() {
		Lock lock = getInstance().getLock("mylock");
		lock.lock();
		try {
			System.out.println("test2");
		} finally {
			lock.unlock();
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
