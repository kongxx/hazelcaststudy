package my.hazelcaststudy.execution;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class Echo implements Callable<String>, Serializable {
	String input = null;

	public Echo() {
	}

	public Echo(String input) {
		this.input = input;
	}

	public String call() {
		System.out.println("Run at " + System.getProperty("myaddress"));
		return System.getProperty("myaddress") + ": " + input;
	}
}