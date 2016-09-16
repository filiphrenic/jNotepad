package hr.fer.zemris.notepad;

import hr.fer.zemris.local.ILocalizationProvider;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * This class represents a single notepad document, which has it's own text area and a path to some file that it
 * represents.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
class JNotepadFile {

    /** Default charset is set to UTF-8 */
    private static final Charset DEF_CHARSET = StandardCharsets.UTF_8;

    // keys used by the localization provider

    private static final String UNTITLED = "untitledKey";

    private static final String SAVED_MESSAGE = "savedMessageKey";
    private static final String SAVED_TITLE = "savedTitleKey";
    private static final String NOT_READABLE_MESSAGE = "fileNotReadableMessageKey";
    private static final String NOT_READABLE_TITLE = "fileNotReadableTitleKey";

    private static final String WRITE_ERROR_MESSAGE = "errorWhileWritingFileMessageKey";
    private static final String WRITE_ERROR_TITLE = "errorWhileWritingFileTitleKey";
    private static final String READ_ERROR_MESSAGE = "errorWhileReadingFileMessageKey";
    private static final String READ_ERROR_TITLE = "errorWhileReadingFileTitleKey";

    private Path filePath;
    private final JTextArea textArea;
    private boolean changed;
    private final ILocalizationProvider provider;

    /**
     * Creates a new empty {@link JNotepadFile} with a given localization provider.
     * 
     * @param provider localization provider
     */
    public JNotepadFile(ILocalizationProvider provider) {
        this(provider, null);
    }

    /**
     * Opens a new {@link JNotepadFile} with given location provider and path to read the contents from.
     * 
     * @param provider localization provider
     * @param filePath path to some file you want to represent with this {@link JNotepadFile}
     */
    public JNotepadFile(ILocalizationProvider provider, Path filePath) {
        this.provider = provider;
        this.filePath = filePath;
        textArea = createTextArea();
        changed = false;
        readFromFile();
    }

    /**
     * Creates a new {@link JNotepadFile} with given path and predifined content given through <code>content</code>
     * 
     * @param provider localization provider
     * @param filePath path to file
     * @param content content that will be in the file
     */
    public JNotepadFile(ILocalizationProvider provider, Path filePath, String content) {
        this.provider = provider;
        this.filePath = filePath;
        textArea = createTextArea();
        textArea.setText(content);
        changed = false;
    }

    /**
     * @return the filePath
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Sets this documents path.
     * 
     * @param filePath path you want to set
     */
    public void setFilePath(final Path filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the textArea
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Returns the title of this document.
     * 
     * @return title of this document, or <code>untitled</code> if the document wasn't saved yet
     */
    public String getTitle() {
        if (filePath != null) {
            return filePath.getFileName().toString();
        } else {
            return provider.getString(UNTITLED);
        }
    }

    /**
     * Used to save a document onto his path.
     */
    public void save() {
        if (!changed) {
            return;
        }

        byte[] textFromArea = textArea.getText().getBytes(DEF_CHARSET);
        try {
            Files.write(filePath, textFromArea);
            changed = false; // it is saved now
            JOptionPane.showMessageDialog(textArea, provider.getString(SAVED_MESSAGE), provider.getString(SAVED_TITLE),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(textArea, provider.getString(WRITE_ERROR_MESSAGE), provider
                    .getString(WRITE_ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a new {@link JTextArea} that has the power to registring when a key was released.
     * 
     * @return new {@link JTextArea}
     */
    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                changed = true;
            }
        });
        return textArea;
    }

    /**
     * Used to read text from file and set it as text in text area.
     */
    private void readFromFile() {
        if (filePath == null) {
            return;
        }

        if (!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(textArea, provider.getString(NOT_READABLE_MESSAGE), provider
                    .getString(NOT_READABLE_TITLE), JOptionPane.ERROR_MESSAGE);
            return;
        }

        byte[] textFromFile;
        try {
            textFromFile = Files.readAllBytes(filePath);
            textArea.setText(new String(textFromFile, DEF_CHARSET));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(textArea, provider.getString(READ_ERROR_MESSAGE), provider
                    .getString(READ_ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Two {@link JNotepadFile} are considered to be equal if their paths are equal.
     * 
     * @param obj tested object
     * @return <code>true</code> if the given object is equal to this object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JNotepadFile) {
            JNotepadFile second = (JNotepadFile) obj;
            if (second.filePath == null) {
                return filePath == null;
            }
            return this.filePath.equals(second.filePath);
        }
        return false;
    }

}
