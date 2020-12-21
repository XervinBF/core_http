package org.xbf.addons.core_http;

import java.awt.Color;

import org.xbf.core.XBF;
import org.xbf.core.XVI;
import org.xbf.core.Messages.Response;
import org.xbf.core.Messages.RichResponse;
import org.xbf.core.Plugins.DependsOn;
import org.xbf.core.Plugins.PluginVersion;
import org.xbf.core.Plugins.XPlugin;
import org.xbf.core.Plugins.XervinJavaPlugin;

@XPlugin(name="core_http", displayname="XBF Core HTTP Server", description="An HTTP Server with addon support for XBF")
@PluginVersion(currentVersion="1.0.0")
@DependsOn(pluginName="xbf", minimumVersion="0.0.14")
public class HttpServerPlugin extends XervinJavaPlugin {

	public static HSPConfig config;
	
	@Override
	public void register() {
		config = getConfig(HSPConfig.class);
		XBF.registerService(ApiService.class);
	}
	
	
}
