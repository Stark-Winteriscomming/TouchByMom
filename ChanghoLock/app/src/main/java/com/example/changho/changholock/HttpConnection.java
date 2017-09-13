package com.example.changho.changholock;

/**
 * Created by Changho on 2016-10-20.
 */

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class HttpConnection {
    private HttpURLConnection conn = null;

    public HttpConnection(String url) throws MalformedURLException {
        URL serverURL =  new URL(url);
        try {
            conn = (HttpURLConnection) serverURL.openConnection();
        }catch(Exception e){e.printStackTrace();}
    }
    public HttpURLConnection getConn() {
        return conn;
    }

}
