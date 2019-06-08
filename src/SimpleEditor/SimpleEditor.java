package SimpleEditor;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;

/**
 * The main class for running the simple editor
 * in the whole project
 *
 * @author Adam
 */
public class SimpleEditor {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try
        {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            UIManager.put("RootPane.setupButtonVisible", false);
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        new UI().setVisible(true);
    }
}
