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

/**
 * The View for when the user is creating or editing a document in the program.
 */
public class NewDocumentView extends JPanel implements ActionListener, PropertyChangeListener {

    private final NewDocumentViewModel newDocumentViewModel;
    private NewDocumentController newDocumentController;

    private final JTextField titleInputField = new JTextField(20);
    private final JTextField dateInputField = new JTextField(10);
    private final JTextArea textBodyInputField = new JTextArea(20, 40);

    private final JButton backButton = new JButton("Back");
    private final JButton saveButton = new JButton("Save");
    private final JButton recommendButton = new JButton("Get Media Recommendations");

    public NewDocumentView(NewDocumentViewModel newDocumentViewModel) {
        this.newDocumentViewModel = newDocumentViewModel;
        this.newDocumentViewModel.addPropertyChangeListener(this);

        // Set up the main layout
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with buttons
        final JPanel topPanel = new JPanel(new BorderLayout());
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(backButton);
        buttonPanel.add(recommendButton);
        buttonPanel.add(saveButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        // Content panel
        final JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // Title and Date panel
        final JPanel titleDatePanel = new JPanel(new BorderLayout(10, 5));
        titleDatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleDatePanel.setBackground(Color.LIGHT_GRAY);

        // Title field
        titleInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        titleInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Date field
        dateInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        dateInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateInputField.setPreferredSize(new Dimension(150, 30));

        titleDatePanel.add(titleInputField, BorderLayout.CENTER);
        titleDatePanel.add(dateInputField, BorderLayout.EAST);

        // Text body panel
        final JPanel textBodyPanel = new JPanel(new BorderLayout());
        textBodyPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        textBodyPanel.setBackground(Color.LIGHT_GRAY);

        textBodyInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textBodyInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textBodyInputField.setLineWrap(true);
        textBodyInputField.setWrapStyleWord(true);

        final JScrollPane scrollPane = new JScrollPane(textBodyInputField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textBodyPanel.add(scrollPane, BorderLayout.CENTER);

        // Add title/date and text body to content panel
        contentPanel.add(titleDatePanel, BorderLayout.NORTH);
        contentPanel.add(textBodyPanel, BorderLayout.CENTER);

        // Add panels to main view
        this.add(topPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
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
        setFields(state);
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFields(NewDocumentState state) {
        titleInputField.setText(state.getTitle());
        dateInputField.setText(state.getDate());
        textBodyInputField.setText(state.getTextBody());
    }

    public String getViewName() {
        return newDocumentViewModel.getViewName();
    }

    public void setNewDocumentController(NewDocumentController controller) {
        this.newDocumentController = controller;
    }
}

