package my.hazelcaststudy.execution;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.DistributedTask;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.MultiTask;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class DistributedExecutionClient {
	public static void main(String[] args) throws Exception {
		echoOnTheMember();
		echoOnSomewhere();
		echoOnMembers();
	}

	private static void echoOnTheMember() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress("localhost:5701");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		Set<Member> members = client.getCluster().getMembers();
		//System.out.println(members);

		for (Member member : members) {
			FutureTask<String> task = new DistributedTask<String>(new Echo("hello world"), member);
			ExecutorService executorService = client.getExecutorService();
			executorService.execute(task);
			String result = task.get();
			System.out.println(result);
		}
	}

	private static void echoOnSomewhere() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress("localhost:5701");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

		ExecutorService executorService = client.getExecutorService();
		Future<String> task = executorService.submit(new Echo("hello world"));
		String result = task.get();
		System.out.println(result);
	}

	private static void echoOnMembers() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress("localhost:5701");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		Set<Member> members = client.getCluster().getMembers();

		MultiTask<String> task = new MultiTask<String>(new Echo("hello world"), members);
		ExecutorService executorService = client.getExecutorService();
		executorService.execute(task);
		Collection<String> results = task.get();
		System.out.println(results);
	}
}
