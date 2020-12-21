package org.xbf.addons.core_http;

import org.xbf.core.Plugins.Service;
import org.xbf.core.Plugins.XService;

@XService(name = "apiserver", multipleInstances = false)
public class ApiService extends Service {

	public static APIServer server;
	
	@Override
	public void onStart() {
		server = new APIServer();
	}
	
	@Override
	public void onStop() {
		server.stop();
	}
	
	
	
}
