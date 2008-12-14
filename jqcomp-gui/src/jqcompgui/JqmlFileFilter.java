package jqcompgui;

import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author rob
 */
public class JqmlFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jqml");
    }

    public String getDescription() {
        return "(*.jqml) jqcomp quantum circuit xml file";
    }
}
