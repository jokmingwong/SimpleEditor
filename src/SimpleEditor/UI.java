package SimpleEditor;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 此类用于展示全局UI
 *
 * @author pj
 */


public class UI extends JFrame implements ActionListener {
    //main page
    private final JTextArea textArea = new JTextArea("", 0, 0);
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu file = new JMenu("File"),
            edit = new JMenu("Edit"),
            search = new JMenu("Search"),
            setting = new JMenu("Setting"),
            about = new JMenu("About");
    private final JMenuItem newFile, openFile, saveFile, close, cut, copy, paste, clearFile, selectAll, quickFind,
            aboutMe, aboutSoftware, wordWrap, format,seting;
    private final JToolBar toolBar = new JToolBar();
    private final Nbutton openButton, saveButton, clearButton, searchButton, aboutMeButton, aboutButton, closeButton, boldButton, italicButton, undoButton, redoButton, formatButton;
    private final Action selectAllAction;
    private final Nbutton newButton,setButton;

    //icons
    private final ImageIcon titleIcon = new ImageIcon("icons/main.png");
    private final ImageIcon boldIcon = new ImageIcon("icons/bold.png");
    private final ImageIcon italicIcon = new ImageIcon("icons/italic.png");

    // setup icons - File Menu
    private final ImageIcon newIcon = new ImageIcon("icons/new.png");
    private final ImageIcon openIcon = new ImageIcon("icons/open.png");
    private final ImageIcon saveIcon = new ImageIcon("icons/save.png");
    private final ImageIcon closeIcon = new ImageIcon("icons/close.png");

    // setup icons - Edit Menu
    private final ImageIcon clearIcon = new ImageIcon("icons/clear.png");
    private final ImageIcon cutIcon = new ImageIcon("icons/cut.png");
    private final ImageIcon copyIcon = new ImageIcon("icons/copy.png");
    private final ImageIcon pasteIcon = new ImageIcon("icons/paste.png");
    private final ImageIcon selectAllIcon = new ImageIcon("icons/selectall.png");
    private final ImageIcon wordwrapIcon = new ImageIcon("icons/wordwrap.png");
    private final ImageIcon undoIcon = new ImageIcon("icons/undo.png");
    private final ImageIcon redoIcon = new ImageIcon("icons/redo.png");
    private final ImageIcon formatIcon = new ImageIcon("icons/format.png");

    // setup icons - Search Menu
    private final ImageIcon searchIcon = new ImageIcon("icons/search.png");

    // setup icons - Help Menu
    private final ImageIcon aboutMeIcon = new ImageIcon("icons/about_me.png");
    private final ImageIcon aboutIcon = new ImageIcon("icons/about.png");

    // setup icons - Setting Menu
    private final ImageIcon setIcon = new ImageIcon("icons/setting.png");

    //more function support
    private SupportedKeywords supportedKeywords = new SupportedKeywords();
    private HighLight highLight = new HighLight(new Color(255, 219, 138));
    private AutoComplete autocomplete;
    private boolean hasListener = false;
    private boolean canEdit = false;

    //bottom info
    private final JLabel lineCount = new JLabel("Lines:0");
    private final JLabel lengthCount = new JLabel("Length:0");
    private final JLabel nowLine = new JLabel("nowLine:0");
    private final JLabel nowCol = new JLabel("nowCol:0");
    private Font daultMenuFont = new Font("黑体", Font.PLAIN, 18);

    public UI() throws HeadlessException {

        /* page main consist */
        setTitle("SimpleEditor");
        setSize(1280, 720);
        setIconImage(titleIcon.getImage());
        setLocationRelativeTo(null);

        // menus
        file.setFont(daultMenuFont);
        edit.setFont(daultMenuFont);
        search.setFont(daultMenuFont);
        about.setFont(daultMenuFont);
        setting.setFont(daultMenuFont);
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(search);
        menuBar.add(setting);
        menuBar.add(about);
        menuBar.setBackground(Color.white);
        setJMenuBar(menuBar);


        //init textarea
        textArea.setFont(new Font("黑体", Font.PLAIN, 22));
        textArea.setTabSize(2);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        LineNumber lineNumber = new LineNumber();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setRowHeaderView(lineNumber);
        getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        getContentPane().add(panel);

        //tools
        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        close = new JMenuItem("Quit", closeIcon);
        clearFile = new JMenuItem("Clear", clearIcon);
        quickFind = new JMenuItem("Find and Replace", searchIcon);
        aboutMe = new JMenuItem("About Me", aboutMeIcon);
        aboutSoftware = new JMenuItem("About Software", aboutIcon);
        format = new JMenuItem("Format", formatIcon);
        seting = new JMenuItem("Setting",setIcon);

        //tools bar
        Dimension fualtDiv = new Dimension(10,16);
        Dimension bigDiv = new Dimension(36,28);
        this.add(toolBar, BorderLayout.NORTH);
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.white);
        toolBar.addSeparator(new Dimension(10,28));
        // used to create space between button groups
        //file  tool
        newButton = new Nbutton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        toolBar.add(newButton);
        toolBar.addSeparator(fualtDiv);

        openButton = new Nbutton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        toolBar.add(openButton);
        toolBar.addSeparator(fualtDiv);

        saveButton = new Nbutton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        toolBar.add(saveButton);
        toolBar.addSeparator(bigDiv);

        //edit tool
        boldButton = new Nbutton(boldIcon);
        boldButton.setToolTipText("Bold");
        boldButton.addActionListener(this);
        toolBar.add(boldButton);
        toolBar.addSeparator(fualtDiv);

        italicButton = new Nbutton(italicIcon);
        italicButton.setToolTipText("Italic");
        italicButton.addActionListener(this);
        toolBar.add(italicButton);
        toolBar.addSeparator(fualtDiv);

        clearButton = new Nbutton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        toolBar.add(clearButton);
        toolBar.addSeparator(fualtDiv);

        formatButton = new Nbutton(formatIcon);
        formatButton.setToolTipText("Format");
        formatButton.addActionListener(this);
        toolBar.add(formatButton);
        toolBar.addSeparator(fualtDiv);

        undoButton = new Nbutton(undoIcon);
        undoButton.setToolTipText("Undo");
        toolBar.add(undoButton);
        toolBar.addSeparator(fualtDiv);

        redoButton = new Nbutton(redoIcon);
        redoButton.setToolTipText("Redo");
        toolBar.add(redoButton);
        toolBar.addSeparator(fualtDiv);

        searchButton = new Nbutton(searchIcon);
        searchButton.setToolTipText("Search and Replace");
        searchButton.addActionListener(this);
        toolBar.add(searchButton);
        toolBar.addSeparator(bigDiv);

        // system tools
        setButton = new Nbutton(setIcon);
        setButton.setToolTipText("Setting");
        setButton.addActionListener(this);
        toolBar.add(setButton);
        toolBar.addSeparator(fualtDiv);

        closeButton = new Nbutton(closeIcon);
        closeButton.setToolTipText("Quit");
        closeButton.addActionListener(this);
        toolBar.add(closeButton);
        toolBar.addSeparator(bigDiv);

        // about tools
        aboutMeButton = new Nbutton(aboutMeIcon);
        aboutMeButton.setToolTipText("About Me");
        aboutMeButton.addActionListener(this);
        toolBar.add(aboutMeButton);
        toolBar.addSeparator(fualtDiv);

        aboutButton = new Nbutton(aboutIcon);
        aboutButton.setToolTipText("About NotePad PH");
        aboutButton.addActionListener(this);
        toolBar.add(aboutButton);
        toolBar.addSeparator(bigDiv);

        //bottom
        JPanel bottomInfo = new JPanel();
        bottomInfo.add(lineCount);
        bottomInfo.add(Box.createHorizontalStrut(60));
        bottomInfo.add(lengthCount);
        bottomInfo.add(Box.createHorizontalStrut(200));
        bottomInfo.add(nowLine);
        bottomInfo.add(Box.createHorizontalStrut(60));
        bottomInfo.add(nowCol);
        updateBottom(0, 0);
        bottomInfo.setBackground(new Color(220, 225, 223));
        bottomInfo.setSize(new Dimension(this.getWidth(), 8));
        panel.add(bottomInfo, BorderLayout.SOUTH);

        /* actions set */
        //file part
        // New File
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)); // Set a keyboard shortcut
        file.add(newFile); // Adding the file menu

        // Open File
        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        file.add(openFile);

        // Save File
        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        file.add(saveFile);

        // Close File
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        file.add(close);

        // edit part
        // Select All Text
        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text", new Integer(KeyEvent.VK_A),
                textArea);
        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        edit.add(selectAll);

        // Format Text
        format.addActionListener(this);
        format.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
        edit.add(format);

        // Clear File (Code)
        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        edit.add(clearFile);

        // Cut Text
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        edit.add(cut);

        // WordWrap
        wordWrap = new JMenuItem();
        wordWrap.setText("Word Wrap");
        wordWrap.setIcon(wordwrapIcon);
        wordWrap.setToolTipText("Word Wrap");

        //Short cut key or key stroke
        wordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        edit.add(wordWrap);

        // Copy Text
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        edit.add(copy);

        // Paste Text
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        edit.add(paste);

        // search part
        // Find Word
        quickFind.addActionListener(this);
        quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        search.add(quickFind);

        // setting part
        seting.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        setting.add(seting);

        // about part
        // About Me
        aboutMe.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        about.add(aboutMe);

        // About Software
        aboutSoftware.addActionListener(this);
        aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        about.add(aboutSoftware);



        /* support set */
        //listeners for support
        //highlight
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                canEdit = true;
                highLight.paint(textArea, supportedKeywords.getCppKeywords());
                highLight.paint(textArea, supportedKeywords.getJavaKeywords());
            }
        });
        // now lines show
        textArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                try {
                    //e.getDot() 获得插入符的位置。
                    int offset = e.getDot();
                    int row = textArea.getLineOfOffset(offset);
                    int column = e.getDot() - textArea.getLineStartOffset(row);
                    updateBottom(row, column);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //line wrap
        wordWrap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // If wrapping is false then after clicking on menuitem the word wrapping will be enabled
                if (textArea.getLineWrap() == false) {
                    /* Setting word wrapping to true */
                    textArea.setLineWrap(true);
                } else {
                    // else  if wrapping is true then after clicking on menuitem the word wrapping will be disabled
                    /* Setting word wrapping to false */
                    textArea.setLineWrap(false);
                }
            }
        });
        // undo and redo part
        Undo.undoManuInit(edit, undoIcon, redoIcon);
        Undo.UndoButtonInit(undoButton, redoButton, textArea);
        // autocomplete
        //AutoComplete autoComplete = new AutoComplete(this,new ArrayList<String>(Arrays.asList(supportedKeywords.getAll())));
    }

    private void updateBottom(int lineset, int clomnset) {
        lineCount.setText("Lines:" + textArea.getLineCount());
        lengthCount.setText("Length:" + textArea.getText().length());
        nowLine.setText("nowLine:" + lineset);
        nowCol.setText("nowCol:" + clomnset);

    }
	/**
	*此函数用于监听textArea的各种按钮
	*@param 各种动作
	*根据各种各样的操作对应各种响应
	*
	*/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile || e.getSource() == newButton) newFileFunction();
        else if (e.getSource() == openFile || e.getSource() == openButton) openFileFunction();
        else if (e.getSource() == saveFile || e.getSource() == saveButton) saveFile();
        else if (e.getSource() == close || e.getSource() == closeButton) closeFunction();
        else if (e.getSource() == boldButton) boldFontFunction();
        else if (e.getSource() == italicButton) italicFontFunction();
        else if (e.getSource() == clearFile || e.getSource() == clearButton) clearFileFunction();
        else if (e.getSource() == quickFind || e.getSource() == searchButton) new FindWord(textArea);
        else if (e.getSource() == format || e.getSource() == formatButton) Format.format(textArea);
        else if (e.getSource() == seting || e.getSource() == setButton) new Setting(textArea);
        else if (e.getSource() == aboutMe || e.getSource() == aboutMeButton) new About(this).me();
        else if (e.getSource() == aboutSoftware || e.getSource() == aboutButton) new About(this).software();

    }
	/**
	*此函数用于相应关闭动作
	*@param 无
	*关闭窗口，选择保存或退出
	*
	*/
    private void closeFunction() {
        if (canEdit) {
            Object[] options = {"Save and exit", "No Save and exit", "Return"};
            int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (n == 0) {// save and exit
                saveFile();
                this.dispose();// dispose all resources and close the application
            } else if (n == 1) {// no save and exit
                this.dispose();// dispose all resources and close the application
            }
        } else {
            this.dispose();// dispose all resources and close the application
        }
    }
	/**
	*此函数用于对应于新文件动作
	*@param 无
	*新建文件，如果文本区有内容先提示是否保存
	*
	*/
    private void newFileFunction() {
        if (canEdit) {
            Object[] options = {"Save", "No Save", "Return"};
            int n = JOptionPane.showOptionDialog(this, "Do you want to save the file at first ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (n == 0) {// save
                saveFile();
                canEdit = false;
            } else if (n == 1) {
                canEdit = false;
                ClearAll.clear(textArea);
            }
        } else {
            ClearAll.clear(textArea);
        }
    }
	/**
	*此函数用于对应于加粗动作
	*@param 无
	*针对文本进行加粗处理
	*
	*/
    private void boldFontFunction() {
        if (textArea.getFont().getStyle() == Font.BOLD) {
            textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
        } else {
            textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        }
    }
	/**
	*此函数用于对应于斜体动作
	*@param 无
	*针对文本进行斜体处理
	*
	*/
    private void italicFontFunction() {
        if (textArea.getFont().getStyle() == Font.ITALIC) {
            textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
        } else {
            textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
        }
    }
	/**
	*此函数用于对应于清除动作
	*@param 无
	*清除文本框内所有文本
	*
	*/
    private void clearFileFunction() {
        Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == 0) {// clear
            ClearAll.clear(textArea);
        }
    }
	/**
	*此函数用于返回textArea
	*@param 无
	*返回对应值
	*
	*/
    public JTextArea getTextArea() {
        return textArea;
    }
	/**
	*此函数用于对应于打开文件动作
	*@param 无
	*打开一个已存在的文件
	*
	*/
    private void openFileFunction() {
        JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to  browse files to open)
        int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
        if (option == JFileChooser.APPROVE_OPTION) {
            ClearAll.clear(textArea); // clear the TextArea before applying the file contents
            try {
                File openFile = open.getSelectedFile();
                setTitle(openFile.getName() + " | 打开文件");
                Scanner scan = new Scanner(new FileReader(openFile.getPath()));
                while (scan.hasNext()) {
                    textArea.append(scan.nextLine() + "\n");
                }

                //enableAutoComplete(openFile);
            } catch (Exception ex) { // catch any exceptions, and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }

    }

	/**
	*此函数用监听windows按键
	*@param 无
	*针对不同按键进行不同的处理，主要是处理退出和关闭程序
	*
	*/
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (canEdit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                System.exit(99);
            }
        }
    }

    public JTextArea getEditor() {
        return textArea;
    }

	/**
	*此类用于提供对于全选的支持
	*@param 无
	*全选用的基础类
	*
	*/
    class SelectAllAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic, final JTextArea textArea) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }

    }

	/**
	*此函数用于对应于保存文件动作
	*@param 无
	*将现有的文件进行保存
	*
	*/
    private void saveFile() {
        // Open a file chooser
        JFileChooser fileChoose = new JFileChooser();
        // Open the file, only this time we call
        int option = fileChoose.showSaveDialog(this);

        /*
         * ShowSaveDialog instead of showOpenDialog if the user clicked OK
         * (and not cancel)
         */
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File openFile = fileChoose.getSelectedFile();
                setTitle(openFile.getName() + " | 文件保存");

                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(textArea.getText());
                out.close();

                //enableAutoComplete(openFile);
                canEdit = false;
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }
    }
}
