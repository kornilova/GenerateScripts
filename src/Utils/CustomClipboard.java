package Utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 10.04.13
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class CustomClipboard {


    public static void pushInClipboard(String str)
    {
        Clipboard buffer = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(str);
        buffer.setContents(transferable, null);
    }

}
