package itu.janettholst;

import java.rmi.RemoteException;

import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.EntityListener;
import dk.pervasive.jcaf.entity.Person;
import dk.pervasive.jcaf.impl.RemoteEntityListenerImpl;
import dk.pervasive.jcaf.util.AbstractContextClient;

public class Listener extends AbstractContextClient implements EntityListener {
	
	private RemoteEntityListenerImpl listener;
	public String location = "";
	
	public Listener(String service_uri) {
		super(service_uri);
		try {
			listener = new RemoteEntityListenerImpl();
			listener.addEntityListener(this);
		} catch (RemoteException ignored) {}
		
		try {
        	getContextService().addEntityListener(listener, Person.class);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public String test() throws RemoteException {
		location = String.valueOf(getContextService().getEntity("http://pit.itu.dk:7331/location-of/18002D9E5518"));
		return location;
	}
	
	public void contextChanged(ContextEvent event) {
		//Reading the location item id. I dont know if the best way is to pass the information as Location.id, 
		System.out.println("change occured");
		System.out.println("Current location: " + event.getItem().getId().toString());
		
	}

	@Override
	public void run() {
		
	}

}
