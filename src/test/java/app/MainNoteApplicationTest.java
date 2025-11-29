package app;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MainNoteApplicationTest {

    @Test
    void builderBuildsApplicationFrameWithCardLayout() {
        NoteAppBuilder builder = new NoteAppBuilder();
        JFrame app = builder
                .addLockScreenView()
                .addHomeMenuView()
                .addNewDocumentView()
                .addRecommendationView()
                .addVerifyPasswordUseCase()
                .addHomeMenuUseCases()
                .addNewDocumentUseCases()
                .addRecommendationMenuUseCase()
                .build();

        assertNotNull(app, "Builder should return a non-null JFrame");
        assertEquals("MoodVerse - Mood-Based Diary & Recommendations", app.getTitle());

        Container content = app.getContentPane();
        assertEquals(1, content.getComponentCount(),
                "Application should contain a single root card panel");

        Component root = content.getComponent(0);
        assertTrue(root instanceof JPanel, "Root component should be a JPanel");

        LayoutManager layout = ((JPanel) root).getLayout();
        assertTrue(layout instanceof CardLayout, "Root panel should use CardLayout");

        JPanel cardPanel = (JPanel) root;
        assertTrue(cardPanel.getComponentCount() >= 2,
                "Card panel should contain multiple views (lock screen, home menu, etc.)");

        app.dispose();
    }

    @Test
    void mainMethodRunsWithoutThrowing() {
        assertDoesNotThrow(() -> MainNoteApplication.main(new String[]{}));
    }
}

