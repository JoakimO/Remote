package se.newbie.remote.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, String, InputStream> {
	private static final String TAG = "HttpRequestTask";
	
	private String credentials = null;
	private HttpRequestTaskHandler handler;
	
	public void setCredentials(String user, String password) {
		credentials = Base64.encodeToString((user + ":" + password).getBytes(), 0);
	}
	
	public void setHttpRequestTaskHandler(HttpRequestTaskHandler handler) {
		this.handler = handler;
	}
	
	@Override
    protected InputStream doInBackground(String... uri) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        
        InputStream in = null;
        try {
        	Log.v(TAG, "Request: " + uri[0]);
        	
        	HttpGet httpGet = new HttpGet(uri[0]);        	
        	if (credentials != null) {
        		httpGet.addHeader("Authorization","Basic " + this.credentials);
        	}

            response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                //in = response.getEntity().getContent();
            	ByteArrayOutputStream out = new ByteArrayOutputStream();
            	response.getEntity().writeTo(out);
                out.close();
                in = new ByteArrayInputStream(out.toByteArray());
                in.close();

            } else { 
                response.getEntity().getContent().close();
                Log.v(TAG, statusLine.getReasonPhrase()); 
                
                if (handler != null) {
                	//handler.onFailure(response);
                }
            }
        } catch (ClientProtocolException e) {
        	Log.e(TAG, e.getMessage());
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage());
        }
        return in;
    }

    @Override
    protected void onPostExecute(InputStream result) {
        if (handler != null) {
        	handler.onSuccess(result);
        }
        super.onPostExecute(result);

    }

}
