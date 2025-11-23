package app;

import javax.swing.*;

/**
 * Main application entry point for MoodVerse.
 */
public class MainNoteApplication {
    public static void main(String[] args) {
        // Build and launch the application
        final NoteAppBuilder builder = new NoteAppBuilder();
        final JFrame application = builder
                .addLockScreenView()
                .addHomeMenuView()
                .addNewDocumentView()
                .addRecommendationView()
                .addVerifyPasswordUseCase()
                .addHomeMenuUseCases()
                .addNewDocumentUseCases()
                .addRecommendationMenuUseCase()
                .build();

        application.setVisible(true);
    }
}
