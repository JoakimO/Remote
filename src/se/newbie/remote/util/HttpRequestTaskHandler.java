package se.newbie.remote.util;

import java.io.InputStream;

public interface HttpRequestTaskHandler {
	public void onSuccess(InputStream out);
}
