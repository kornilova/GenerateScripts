package Utils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 28.12.12
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class CustomFile {

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            long count = 0;
            long size = source.size();
            while((count += destination.transferFrom(source, count, size-count))<size);
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
