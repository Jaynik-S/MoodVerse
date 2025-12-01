package view;

import interface_adapter.new_document.NewDocumentController;
import interface_adapter.new_document.NewDocumentState;
import interface_adapter.new_document.NewDocumentViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for when the user is creating or editing a document in the program.
 */
public class NewDocumentView extends JPanel implements ActionListener, PropertyChangeListener {

    private final NewDocumentViewModel newDocumentViewModel;
    private NewDocumentController newDocumentController;

    private final JTextField titleInputField = new JTextField(15);
    private final JTextField dateInputField = new JTextField(12);
    private final JTextArea textBodyInputField = new JTextArea(20, 40);
    private final JTextField keywordsDisplayField = new JTextField();

    private final JButton backButton = new JButton("Back");
    private final JButton saveButton = new JButton("Save");
    private final JButton recommendButton = new JButton("Get Media Recommendations");
    private final JButton toggleKeywordsButton = new JButton("Show Keywords");

    private final JPanel keywordsPanel = new JPanel(new BorderLayout());
    private boolean keywordsVisible = false;

    public NewDocumentView(NewDocumentViewModel newDocumentViewModel) {
        this.newDocumentViewModel = newDocumentViewModel;
        this.newDocumentViewModel.addPropertyChangeListener(this);

        // Soft background and outer padding matching HomeMenuView
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top bar container (card-like) with buttons right-aligned
        JPanel topCard = new JPanel(new BorderLayout());
        topCard.setBackground(new Color(219, 234, 254));
        topCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12))
        );


        // Button styling to stay consistent across app
        styleSecondaryButton(backButton);
        styleSecondaryButton(recommendButton);
        styleSecondaryButton(toggleKeywordsButton);
        stylePrimaryButton(saveButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(toggleKeywordsButton);
        buttonPanel.add(recommendButton);
        buttonPanel.add(saveButton);

        topCard.add(backButton, BorderLayout.WEST);
        topCard.add(buttonPanel, BorderLayout.EAST);

        // Main content card
        JPanel contentCard = new JPanel(new BorderLayout(12, 12));
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16))
        );

        // Title + date row
        JPanel titleDatePanel = new JPanel(new BorderLayout(10, 0));
        titleDatePanel.setOpaque(false);

        titleInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8))
        );
        // Match larger, readable size similar to HomeMenuView table text
        titleInputField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        dateInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8))
        );
        dateInputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateInputField.setPreferredSize(new Dimension(150, 36));
        dateInputField.setHorizontalAlignment(JTextField.CENTER);
        dateInputField.setEditable(false); //Can't type
        dateInputField.setFocusable(false); //Can't select

        titleDatePanel.add(titleInputField, BorderLayout.CENTER);
        titleDatePanel.add(dateInputField, BorderLayout.EAST);
        titleDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Keywords row (hidden until toggled)
        JLabel keywordsLabel = new JLabel("Keywords");
        keywordsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        keywordsLabel.setForeground(new Color(55, 65, 81));

        keywordsDisplayField.setEditable(false);
        keywordsDisplayField.setFocusable(false);
        keywordsDisplayField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        keywordsDisplayField.setBackground(new Color(248, 250, 252));
        keywordsDisplayField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8))
        );
        keywordsDisplayField.setText("Keywords hidden");

        JPanel keywordsRow = new JPanel(new BorderLayout(10, 0));
        keywordsRow.setOpaque(false);
        keywordsRow.add(keywordsLabel, BorderLayout.WEST);
        keywordsRow.add(keywordsDisplayField, BorderLayout.CENTER);

        keywordsPanel.setOpaque(false);
        keywordsPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        keywordsPanel.add(keywordsRow, BorderLayout.CENTER);
        keywordsPanel.setVisible(false);
        keywordsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Body area
        JPanel textBodyPanel = new JPanel(new BorderLayout());
        textBodyPanel.setOpaque(false);

        // Increase text body size for better readability
        textBodyInputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textBodyInputField.setLineWrap(true);
        textBodyInputField.setWrapStyleWord(true);
        textBodyInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10))
        );

        JScrollPane scrollPane = new JScrollPane(textBodyInputField);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        textBodyPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel headerStack = new JPanel();
        headerStack.setOpaque(false);
        headerStack.setLayout(new BoxLayout(headerStack, BoxLayout.Y_AXIS));
        headerStack.add(titleDatePanel);
        headerStack.add(keywordsPanel);

        contentCard.add(headerStack, BorderLayout.NORTH);
        contentCard.add(textBodyPanel, BorderLayout.CENTER);

        add(topCard, BorderLayout.NORTH);
        add(contentCard, BorderLayout.CENTER);

        // Behaviour wiring remains the same
        saveButton.addActionListener(evt -> {
            if (evt.getSource().equals(saveButton)) {
                final String title = titleInputField.getText();
                final String date = dateInputField.getText();
                final String textBody = textBodyInputField.getText();
                newDocumentController.executeSave(title, date, textBody);
            }
        });

        backButton.addActionListener(evt -> {
            if (evt.getSource().equals(backButton)) {
                newDocumentController.executeBack();
            }
        });

        recommendButton.addActionListener(evt -> {
            if (evt.getSource().equals(recommendButton)) {
                final String textBody = textBodyInputField.getText();
                newDocumentController.executeGetRecommendations(textBody);
            }
        });

        toggleKeywordsButton.addActionListener(evt -> {
            if (evt.getSource().equals(toggleKeywordsButton)) {
                if (!keywordsVisible) {
                    keywordsVisible = true;
                    keywordsPanel.setVisible(true);
                    toggleKeywordsButton.setText("Hide Keywords");
                    keywordsDisplayField.setText("Extracting keywords...");
                    final String textBody = textBodyInputField.getText();
                    newDocumentController.executeAnalyzeKeywords(textBody);
                } else {
                    keywordsVisible = false;
                    keywordsPanel.setVisible(false);
                    toggleKeywordsButton.setText("Show Keywords");
                }
                revalidate();
                repaint();
            }
        });
    }

    private static void stylePrimaryButton(JButton button) {
        button.setFont(new Font(button.getFont().getFontName(), Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // fix for MacOS
        button.setBackground(new Color(37, 99, 235));
        button.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void styleSecondaryButton(JButton button) {
        button.setFont(new Font(button.getFont().getFontName(), Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setForeground(new Color(55, 65, 81));
        button.setBackground(new Color(239, 246, 255));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12))
        );
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final NewDocumentState state = (NewDocumentState) evt.getNewValue();
        if (state.getError() == null) {
            if ("keywords".equals(evt.getPropertyName())) {
                updateKeywords(state.getKeywords());
            } else {
                setFields(state);
            }
        }
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFields(NewDocumentState state) {
        titleInputField.setText(state.getTitle());
        dateInputField.setText(state.getDate());
        textBodyInputField.setText(state.getTextBody());
        updateKeywords(state.getKeywords());
    }

    private void updateKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            keywordsDisplayField.setText("No keywords extracted yet.");
            if (!keywordsVisible) {
                keywordsPanel.setVisible(false);
            }
        } else {
            keywordsDisplayField.setText(String.join(", ", keywords));
            keywordsPanel.setVisible(keywordsVisible);
        }
        toggleKeywordsButton.setText(keywordsVisible ? "Hide Keywords" : "Show Keywords");
    }

    public String getViewName() {
        return newDocumentViewModel.getViewName();
    }

    public void setNewDocumentController(NewDocumentController controller) {
        this.newDocumentController = controller;
    }
}
