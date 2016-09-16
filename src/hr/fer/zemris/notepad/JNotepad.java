package hr.fer.zemris.notepad;

import hr.fer.zemris.local.FormLocalizationProvider;
import hr.fer.zemris.local.ILocalizationProvider;
import hr.fer.zemris.local.LocalizationProvider;
import hr.fer.zemris.local.swing.LJFileChooser;
import hr.fer.zemris.local.swing.LJMenu;
import hr.fer.zemris.local.swing.LJToolBar;
import hr.fer.zemris.local.swing.LocalizableAction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

/**
 * It is a program that acts as a normal notepad.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class JNotepad extends JFrame {

    private static final long serialVersionUID = 363861041290763136L;

    /** Title */
    private static final String TITLE = "Notepad++ v1.0";

    /** List of opened files */
    private List<JNotepadFile> files;
    /** Current opened fiel */
    private JNotepadFile currentFile;
    /** Tabs used in notepad */
    private JTabbedPane tabs;

    /** Provides localization for this program. */
    private final ILocalizationProvider provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

    /**
     * Creates a new notepad. Sets it's title, location, size and initializes the GUI.
     */
    private JNotepad() {
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(100, 100);
        setSize(600, 600);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(null);
            }
        });

        initGUI();
    }

    /**
     * Initializes the GUI. Adds menus, toolbars and tabs.
     */
    private void initGUI() {
        files = new ArrayList<>();

        getContentPane().setLayout(new BorderLayout());

        createActions();
        createMenus();
        createToolbar();
        createTabs();
        newFileAction.actionPerformed(null); // create an empty tab
    }

    /**
     * Creates actions, adds some values to them.
     */
    private void createActions() {
        createFileActions();
        createEditActions();
        createHelpActions();
        createToolbarActions();
    }

    /**
     * Creates menus.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LJMenu(FILE_MENU, provider);
        fileMenu.add(new JMenuItem(newFileAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(openFileAction));
        fileMenu.add(new JMenuItem(saveFileAction));
        fileMenu.add(new JMenuItem(saveAsFileAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(closeFileAction));
        fileMenu.add(new JMenuItem(exitAction));
        menuBar.add(fileMenu);

        JMenu editMenu = new LJMenu(EDIT_MENU, provider);
        editMenu.add(new JMenuItem(copySelectedPartAction));
        editMenu.add(new JMenuItem(cutSelectedPartAction));
        editMenu.add(new JMenuItem(pasteSelectedPartAction));
        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.addSeparator();
        editMenu.add(new JMenuItem(toggleCaseAction));
        menuBar.add(editMenu);

        JMenu helpMenu = new LJMenu(HELP_MENU, provider);
        helpMenu.add(new JMenuItem(hrLanguageAction));
        helpMenu.add(new JMenuItem(enLanguageAction));
        helpMenu.addSeparator();
        helpMenu.add(new JMenuItem(aboutAction));
        menuBar.add(helpMenu);

        this.setJMenuBar(menuBar);
    }

    /**
     * Creates Toolbar.
     */
    private void createToolbar() {
        JToolBar toolBar = new LJToolBar(TOOLBAR, provider);
        toolBar.setFloatable(true);

        toolBar.add(new JButton(openFileAction));
        toolBar.add(new JButton(saveFileAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(toggleLangAction));
        toolBar.add(new JButton(toggleCaseAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Creates localized tabs.
     */
    private void createTabs() {
        tabs = new JTabbedPane();
        tabs.addChangeListener(e -> {
            final int selected = tabs.getSelectedIndex();
            currentFile = (selected == -1 ? null : files.get(selected));
        });
        provider.addLocalizationListener(() -> {
            for (int i = 0, limit = tabs.getComponentCount(); i < limit; i++) {
                tabs.setTitleAt(i, files.get(i).getTitle());
            }
        });
        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    /**
     * Creates actions that will be used in the File menu.
     */
    private void createFileActions() {
        newFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        newFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        newFileAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(NEW_FILE_ACTION_DESC));

        openFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openFileAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(OPEN_FILE_ACTION_DESC));

        saveFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveFileAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(SAVE_FILE_ACTION_DESC));

        saveAsFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveAsFileAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(SAVEAS_FILE_ACTION_DESC));

        closeFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        closeFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        closeFileAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(CLOSE_FILE_ACTION_DESC));

        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F3"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        exitAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(EXIT_ACTION_DESC));
    }

    /**
     * Creates actions availible in the Edit menu.
     */
    private void createEditActions() {
        copySelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copySelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        copySelectedPartAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(COPY_ACTION_DESC));

        cutSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
        cutSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(CUT_ACTION_DESC));

        pasteSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
        pasteSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(PASTE_ACTION_DESC));

        deleteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        deleteSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(DELETE_ACTION_DESC));
    }

    /**
     * Creates actions used in the Help menu.
     */
    private void createHelpActions() {
        hrLanguageAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
        hrLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
        hrLanguageAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(HR_LANG_ACTION_DESC));

        enLanguageAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
        enLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
        enLanguageAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(EN_LANG_ACTION_DESC));

        aboutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
        aboutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        aboutAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(ABOUT_ACTION_DESC));
    }

    /**
     * Creates actions that will be used in the Toolbar.
     */
    private void createToolbarActions() {
        toggleLangAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
        toggleLangAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
        toggleLangAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(TOGGLE_LANG_ACTION_DESC));

        toggleCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));
        toggleCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        toggleCaseAction.putValue(Action.SHORT_DESCRIPTION, provider.getString(TOGGLE_CASE_ACTION_DESC));
    }

    // --------------------------------------------------------------------------------------------
    // ....................................File Actions............................................
    // --------------------------------------------------------------------------------------------

    @SuppressWarnings("serial")
    private final Action newFileAction = new LocalizableAction(NEW_FILE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JNotepadFile file = new JNotepadFile(provider);
            files.add(file);
            openNewTab(file);
        }
    };

    @SuppressWarnings("serial")
    private final Action openFileAction = new LocalizableAction(OPEN_FILE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new LJFileChooser(CHOOSE_FILE_OPEN, provider);
            if (fc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            JNotepadFile file = new JNotepadFile(provider, fc.getSelectedFile().toPath());
            int index = files.indexOf(file);
            if (index == -1) {
                files.add(file);
                openNewTab(file);
            } else {
                tabs.setSelectedIndex(index);
            }
        }
    };

    @SuppressWarnings("serial")
    private final Action saveFileAction = new LocalizableAction(SAVE_FILE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFile.getFilePath() == null) {
                JFileChooser fc = new LJFileChooser(CHOOSE_FILE_SAVE, provider);
                if (fc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(JNotepad.this, provider.getString(NOTHING_SAVED_TEXT), provider
                            .getString(NOTHING_SAVED_TITLE), JOptionPane.WARNING_MESSAGE);
                    return;
                }
                currentFile.setFilePath(fc.getSelectedFile().toPath());
            }
            currentFile.save();
        }
    };

    @SuppressWarnings("serial")
    private final Action saveAsFileAction = new LocalizableAction(SAVEAS_FILE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new LJFileChooser(CHOOSE_FILE_SAVEAS, provider);
            if (fc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(JNotepad.this, provider.getString(NOTHING_SAVED_TEXT), provider
                        .getString(NOTHING_SAVED_TITLE), JOptionPane.WARNING_MESSAGE);
                return;
            }

            JNotepadFile newFile = new JNotepadFile(provider, fc.getSelectedFile().toPath(), currentFile.getTextArea()
                    .getText()); // it is the same as the active document, but with different path

            files.set(files.indexOf(currentFile), newFile);
            currentFile = newFile;
            newFile.save();

        }
    };

    @SuppressWarnings("serial")
    private final Action closeFileAction = new LocalizableAction(CLOSE_FILE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFile.isChanged()) {
                final int answer = JOptionPane.showConfirmDialog(JNotepad.this, provider
                        .getString(SAVE_BEFORE_CLOSE_MESSAGE), provider.getString(SAVE_BEFORE_CLOSE_TITLE),
                        JOptionPane.YES_NO_CANCEL_OPTION);

                switch (answer) {
                    case JOptionPane.YES_OPTION:
                        saveAsFileAction.actionPerformed(e);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    default:
                        return; // cancel was pressed
                }
            }

            final int oldIndex = files.indexOf(currentFile);
            final int newIndex = ((oldIndex + 1) % files.size());
            currentFile = files.get(newIndex);
            files.remove(oldIndex);
            tabs.setSelectedIndex(newIndex);
            tabs.remove(oldIndex);
        }
    };

    @SuppressWarnings("serial")
    private final Action exitAction = new LocalizableAction(EXIT_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0, limit = files.size(); i < limit; i++) {
                if (files.get(i).isChanged()) {
                    tabs.setSelectedIndex(i);
                    final int answer = JOptionPane.showConfirmDialog(JNotepad.this, provider
                            .getString(SAVE_ON_CLOSE_TEXT), provider.getString(SAVE_ON_CLOSE_TITLE),
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    switch (answer) {
                        case JOptionPane.YES_OPTION:
                            saveAsFileAction.actionPerformed(e);
                        case JOptionPane.NO_OPTION:
                            continue;
                        default:
                            return; // cancel was pressed
                    }
                }
            }

            JNotepad.this.dispose(); // everything went well
        }
    };

    // --------------------------------------------------------------------------------------------
    // ....................................Edit Actions............................................
    // --------------------------------------------------------------------------------------------

    @SuppressWarnings("serial")
    private final Action copySelectedPartAction = new LocalizableAction(COPY_ACTION, provider) {
        private final Action action = new DefaultEditorKit.CopyAction();

        @Override
        public void actionPerformed(ActionEvent e) {
            action.actionPerformed(e);
        }
    };

    @SuppressWarnings("serial")
    private final Action cutSelectedPartAction = new LocalizableAction(CUT_ACTION, provider) {
        private final Action action = new DefaultEditorKit.CutAction();

        @Override
        public void actionPerformed(ActionEvent e) {
            action.actionPerformed(e);
        }
    };

    @SuppressWarnings("serial")
    private final Action pasteSelectedPartAction = new LocalizableAction(PASTE_ACTION, provider) {
        private final Action action = new DefaultEditorKit.PasteAction();

        @Override
        public void actionPerformed(ActionEvent e) {
            action.actionPerformed(e);
        }
    };

    @SuppressWarnings("serial")
    private final Action deleteSelectedPartAction = new LocalizableAction(DELETE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JTextArea editor = currentFile.getTextArea();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            if (len == 0) return;
            int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            try {
                doc.remove(offset, len);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    };

    // --------------------------------------------------------------------------------------------
    // ....................................Help Actions............................................
    // --------------------------------------------------------------------------------------------

    @SuppressWarnings("serial")
    private final Action hrLanguageAction = new LocalizableAction(HR_LANG_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("hr");
        }
    };

    @SuppressWarnings("serial")
    private final Action enLanguageAction = new LocalizableAction(EN_LANG_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("en");
        }
    };

    @SuppressWarnings("serial")
    private final Action aboutAction = new LocalizableAction(ABOUT_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(JNotepad.this, provider.getString(ABOUT_TEXT), provider
                    .getString(ABOUT_TITLE), JOptionPane.INFORMATION_MESSAGE);
        }
    };

    // --------------------------------------------------------------------------------------------
    // ....................................Toolbar Actions.........................................
    // --------------------------------------------------------------------------------------------

    @SuppressWarnings("serial")
    private final Action toggleLangAction = new LocalizableAction(TOGGLE_LANG_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            LANG_INDEX = (++LANG_INDEX) % SUPPORTED_LANGS.length;
            LocalizationProvider.getInstance().setLanguage(SUPPORTED_LANGS[LANG_INDEX]);
        }
    };

    @SuppressWarnings("serial")
    private final Action toggleCaseAction = new LocalizableAction(TOGGLE_CASE_ACTION, provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JTextArea editor = currentFile.getTextArea();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            int offset = 0;
            if (len != 0) {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = toggleCase(text);
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String toggleCase(String text) {
            char[] znakovi = text.toCharArray();
            for (int i = 0; i < znakovi.length; i++) {
                char c = znakovi[i];
                if (Character.isLowerCase(c)) {
                    znakovi[i] = Character.toUpperCase(c);
                } else if (Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    // --------------------------------------------------------------------------------------------
    // ...................................Actions end..............................................
    // --------------------------------------------------------------------------------------------

    /**
     * Used to open a new tab with given file and set it as active.
     * 
     * @param file file to open in a new tab
     */
    private void openNewTab(JNotepadFile file) {
        currentFile = file;
        tabs.addTab(currentFile.getTitle(), new JScrollPane(currentFile.getTextArea()));
        tabs.setSelectedComponent(tabs.getComponentAt(tabs.getTabCount() - 1));
    }

    /**
     * Main method that is run when this class is started. Creates a new {@link JNotepad} and sets it's visibility to
     * <code>true</code>.
     * 
     * @param args don't mather
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepad().setVisible(true));
    }

    private static final String[] SUPPORTED_LANGS;
    private static int LANG_INDEX = 0;
    static {
        SUPPORTED_LANGS = new String[] { "en", "hr" };
    }

    private static final String NEW_FILE_ACTION = "newFileActionKey";
    private static final String OPEN_FILE_ACTION = "openFileActionKey";
    private static final String SAVE_FILE_ACTION = "saveFileActionKey";
    private static final String SAVEAS_FILE_ACTION = "saveAsFileActionKey";
    private static final String CLOSE_FILE_ACTION = "closeFileActionKey";
    private static final String EXIT_ACTION = "exitActionKey";

    private static final String COPY_ACTION = "copyActionKey";
    private static final String CUT_ACTION = "cutActionKey";
    private static final String PASTE_ACTION = "pasteActionKey";
    private static final String DELETE_ACTION = "deleteActionKey";

    private static final String HR_LANG_ACTION = "hrLangActionKey";
    private static final String EN_LANG_ACTION = "enLangActionKey";
    private static final String ABOUT_ACTION = "aboutActionKey";

    private static final String TOGGLE_LANG_ACTION = "toggleLangActionKey";
    private static final String TOGGLE_CASE_ACTION = "toggleCaseActionKey";

    private static final String NEW_FILE_ACTION_DESC = "newFileActionDescKey";
    private static final String OPEN_FILE_ACTION_DESC = "openFileActionDescKey";
    private static final String SAVE_FILE_ACTION_DESC = "saveFileActionDescKey";
    private static final String SAVEAS_FILE_ACTION_DESC = "saveAsFileActionDescKey";
    private static final String CLOSE_FILE_ACTION_DESC = "closeFileActionDescKey";
    private static final String EXIT_ACTION_DESC = "exitActionKey";

    private static final String COPY_ACTION_DESC = "copyActionDescKey";
    private static final String CUT_ACTION_DESC = "cutActionDescKey";
    private static final String PASTE_ACTION_DESC = "pasteActionDescKey";
    private static final String DELETE_ACTION_DESC = "deleteActionDescKey";

    private static final String HR_LANG_ACTION_DESC = "hrLangActionDescKey";
    private static final String EN_LANG_ACTION_DESC = "enLangActionDescKey";
    private static final String ABOUT_ACTION_DESC = "aboutActionDescKey";

    private static final String TOGGLE_LANG_ACTION_DESC = "toggleLangActionDescKey";
    private static final String TOGGLE_CASE_ACTION_DESC = "toggleCaseActionDescKey";

    private static final String SAVE_BEFORE_CLOSE_MESSAGE = "saveBeforeCloseMessageKey";
    private static final String SAVE_BEFORE_CLOSE_TITLE = "saveBeforeCloseTitleKey";
    private static final String CHOOSE_FILE_OPEN = "chooseFileOpenKey";
    private static final String CHOOSE_FILE_SAVE = "chooseFileSaveKey";
    private static final String CHOOSE_FILE_SAVEAS = "chooseFileSaveAsKey";
    private static final String NOTHING_SAVED_TEXT = "nothingSavedTextKey";
    private static final String NOTHING_SAVED_TITLE = "nothingSavedTitleKey";
    private static final String SAVE_ON_CLOSE_TEXT = "saveOnCloseTextKey";
    private static final String SAVE_ON_CLOSE_TITLE = "saveOnCloseTitleKey";
    private static final String ABOUT_TEXT = "aboutTextKey";
    private static final String ABOUT_TITLE = "aboutTitleKey";

    private static final String FILE_MENU = "fileMenuKey";
    private static final String EDIT_MENU = "editMenuKey";
    private static final String HELP_MENU = "helpMenuKey";
    private static final String TOOLBAR = "toolbarKey";

}
