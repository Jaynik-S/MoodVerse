package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import interface_adapter.note.NoteController;
import interface_adapter.note.NoteState;
import interface_adapter.note.NoteViewModel;
import interface_adapter.recommendation.RecommendationController;

/**
 * The View for when the user click on the 'Analyze Mood' button.
 */
public class RecommendationView implements ActionListener, PropertyChangeListener {

    private final NoteViewModel noteViewModel;

    private RecommendationController recommendationControllerController;

    private final JLabel noteName = new JLabel("Recommendations based on your diary");

    private final JButton backButton = new JButton("Back");

    public RecommendationView(NoteViewModel noteViewModel) {
        this.noteViewModel = noteViewModel;
        this.noteViewModel.addPropertyChangeListener(this);

        final JPanel buttons = new JPanel();
        buttons.add(backButton);

        backButton.addActionListener(
                evt -> {

                }
        );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(noteName);
        this.add(noteInputField);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private void setFields(NoteState state) {
        noteInputField.setText(state.getNote());
    }

    public void setNoteController(NoteController controller) {
        this.noteController = controller;
    }
}
