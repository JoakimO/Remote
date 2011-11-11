package se.newbie.remote.boxee;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import se.newbie.remote.device.RemoteDeviceDetails;
import android.util.Log;
import android.util.Xml;

public class BoxeeRemoteDeviceDetails implements RemoteDeviceDetails {
	private static final String TAG = "BoxeeRemoteDeviceDetails";

	private String identifier;
	private String name;
	private String host;
	private String port;
	private String authRequired;
	
	public BoxeeRemoteDeviceDetails() {
	};
	
	public BoxeeRemoteDeviceDetails(String details) throws Exception {
		this.deserialize(details);
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAuthRequired() {
		return authRequired;
	}

	public void setAuthRequired(String authRequired) {
		this.authRequired = authRequired;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}	
	
	protected void populateFromDiscovery(String host, String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(new InputSource(new StringReader(xml)));
            Element root = dom.getDocumentElement();	
            
            String application = root.getAttribute("application");
            String command = root.getAttribute("cmd");
            
            if (command.equals("found") && application.equals("boxee")) {
            	setIdentifier(BoxeeRemoteDeviceDiscoverer.APPLICATION + "-" + root.getAttribute("name"));
            	setName(root.getAttribute("name"));
            	setHost(host);
            	setPort(root.getAttribute("httpPort"));
            	setAuthRequired(root.getAttribute("httpAuthRequired"));
            }
            
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} 
	}
	
	public String serialize() {
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", BoxeeRemoteDeviceDiscoverer.APPLICATION);
	        serializer.attribute("", "identifier", getIdentifier());
	        serializer.attribute("", "name", getName());
	        serializer.attribute("", "host", getHost());
	        serializer.attribute("", "port", getPort());
	        serializer.attribute("", "authRequired", getAuthRequired());
	        serializer.endTag("", BoxeeRemoteDeviceDiscoverer.APPLICATION);
	        serializer.endDocument();
	    } catch (Exception e) {
	        Log.e(TAG, "Not able to serialize:\n " + e.getMessage());
	    } 		
		return writer.toString();
	}
	
	public void deserialize(String details) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(new InputSource(new StringReader(details)));
        Element root = dom.getDocumentElement();	
        
        if (!root.getNodeName().equals(BoxeeRemoteDeviceDiscoverer.APPLICATION)) {
        	throw new NullPointerException();
        }
    	setIdentifier(root.getAttribute("identifier"));
    	setName(root.getAttribute("name"));
    	setHost(root.getAttribute("host"));
    	setPort(root.getAttribute("port"));
    	setAuthRequired(root.getAttribute("authRequired"));
	}
}
