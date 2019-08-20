package org.unyde.mapintegrationlib.InternalNavigation.io;

import java.io.*;

public final class IOUtils {

    public static byte[] read(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] data = read(fis);
        fis.close();
        return data;
    }

    public static byte[] read(InputStream is) throws IOException {
        byte[] isData = new byte[512];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        while ((nRead = is.read(isData, 0, isData.length)) != -1) {
            buffer.write(isData, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
