package my.hazelcaststudy.execution;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.DistributedTask;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.Serializable;
import java.util.concurrent.*;

public class ExecutionCallbackSample {

	public static void main(String[] args) throws Exception {
		startServer();
		Thread.sleep(5000);
		startClient();
	}

	private static void startServer() {
		Config cfg = new Config();
		cfg.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		cfg.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
		cfg.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost");
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
	}

	private static void startClient() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress("localhost:5701");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		ExecutorService executorService = client.getExecutorService();
		DistributedTask<String> task = new DistributedTask<String>(new MyEcho("hello world"));
		task.setExecutionCallback(new ExecutionCallback<String>() {
			@Override
			public void done(Future<String> future) {
				try {
					if (!future.isCancelled()) {
						System.out.println("result: " + future.get());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		executorService.execute(task);
	}

	public static class MyEcho implements Callable<String>, Serializable {
		String input = null;

		public MyEcho() {
		}

		public MyEcho(String input) {
			this.input = input;
		}

		public String call() {
			return input;
		}
	}
}
