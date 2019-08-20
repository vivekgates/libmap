package org.unyde.mapintegrationlib.InternalNavigation.android.content;


import org.unyde.mapintegrationlib.InternalNavigation.android.AndroidURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;


/**
 * Android's content url handler
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(final URL url) {
        return new AndroidURLConnection(url);
    }

}