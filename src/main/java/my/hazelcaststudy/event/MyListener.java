package my.hazelcaststudy.event;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;

public class MyListener implements ItemListener, EntryListener {

	@Override
	public void itemAdded(ItemEvent itemEvent) {
		System.out.println("Item added: " + itemEvent.getItem());
	}

	@Override
	public void itemRemoved(ItemEvent itemEvent) {
		System.out.println("Item removed: " + itemEvent.getItem());
	}

	@Override
	public void entryAdded(EntryEvent event) {
		System.out.println("Entry added: key=" + event.getKey() + ", value=" + event.getValue());
	}

	@Override
	public void entryRemoved(EntryEvent event) {
		System.out.println("Entry removed: key=" + event.getKey() + ", value=" + event.getValue());
	}

	@Override
	public void entryUpdated(EntryEvent event) {
		System.out.println("Entry update: key=" + event.getKey() + ", value=" + event.getValue());
	}

	@Override
	public void entryEvicted(EntryEvent event) {
		System.out.println("Entry evicted: key=" + event.getKey() + ", value=" + event.getValue());
	}

}