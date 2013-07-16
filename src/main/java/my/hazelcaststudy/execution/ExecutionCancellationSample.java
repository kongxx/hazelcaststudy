package my.hazelcaststudy.execution;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;

import java.io.Serializable;
import java.util.concurrent.*;

public class ExecutionCancellationSample {

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
		Future<String> task = executorService.submit(new MyEcho("hello world"));

		try {
			String result = task.get(5, TimeUnit.SECONDS);
			System.out.println(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.out.println("cancel task");
			task.cancel(true);
		}
	}

	public static class MyEcho implements Callable<String>, Serializable {
		String input = null;

		public MyEcho() {
		}

		public MyEcho(String input) {
			this.input = input;
		}

		public String call() {
			System.out.println("before sleep on server");
			try {
				Thread.sleep(10 * 1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("after sleep on server");
			return input;
		}
	}
}
