package app;

import data_access.DBNoteDataObject;
import data_access.RecommendationAPIAccessObject;
import data_access.VerifyPasswordDataAccessObject;
import interface_adapter.GoBackPresenter;
import interface_adapter.ViewManagerModel;
import interface_adapter.home_menu.HomeMenuController;
import interface_adapter.home_menu.HomeMenuPresenter;
import interface_adapter.home_menu.HomeMenuViewModel;
import interface_adapter.lock_screen.LockScreenController;
import interface_adapter.lock_screen.LockScreenPresenter;
import interface_adapter.lock_screen.LockScreenViewModel;
import interface_adapter.new_document.NewDocumentController;
import interface_adapter.new_document.NewDocumentPresenter;
import interface_adapter.new_document.NewDocumentViewModel;
import interface_adapter.recommendation_menu.RecommendationMenuController;
import interface_adapter.recommendation_menu.RecommendationMenuPresenter;
import interface_adapter.recommendation_menu.RecommendationMenuViewModel;
import use_case.create_entry.CreateEntryInputBoundary;
import use_case.create_entry.CreateEntryInteractor;
import use_case.delete_entry.DeleteEntryInputBoundary;
import use_case.delete_entry.DeleteEntryInteractor;
import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInteractor;
import use_case.go_back.GoBackInputBoundary;
import use_case.go_back.GoBackInteractor;
import use_case.load_entry.LoadEntryInputBoundary;
import use_case.load_entry.LoadEntryInteractor;
import use_case.save_entry.SaveEntryInputBoundary;
import use_case.save_entry.SaveEntryInteractor;
import use_case.verify_password.VerifyPasswordInputBoundary;
import use_case.verify_password.VerifyPasswordInteractor;
import view.HomeMenuView;
import view.LockscreenView;
import view.NewDocumentView;
import view.RecommendationView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

/**
 * Builder for the MoodVerse Note Application.
 */
public class NoteAppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // Data Access Objects
    private final DBNoteDataObject noteDataAccess = new DBNoteDataObject();
    private final VerifyPasswordDataAccessObject passwordDataAccess = new VerifyPasswordDataAccessObject();
    private final RecommendationAPIAccessObject recommendationDataAccess = new RecommendationAPIAccessObject();

    // View Models
    private LockScreenViewModel lockScreenViewModel;
    private HomeMenuViewModel homeMenuViewModel;
    private HomeMenuPresenter homeMenuPresenter;
    private NewDocumentViewModel newDocumentViewModel;
    private RecommendationMenuViewModel recommendationMenuViewModel;

    // Views
    private NewDocumentView newDocumentView;
    private RecommendationView recommendationView;

    public NoteAppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the LockScreen view to the application.
     * @return this builder
     */
    public NoteAppBuilder addLockScreenView() {
        lockScreenViewModel = new LockScreenViewModel();
        return this;
    }

    /**
     * Adds the HomeMenu view to the application.
     * @return this builder
     */
    public NoteAppBuilder addHomeMenuView() {
        homeMenuViewModel = new HomeMenuViewModel();
        homeMenuPresenter = new HomeMenuPresenter(homeMenuViewModel);
        return this;
    }

    /**
     * Adds the NewDocument view to the application.
     * @return this builder
     */
    public NoteAppBuilder addNewDocumentView() {
        newDocumentViewModel = new NewDocumentViewModel();
        newDocumentView = new NewDocumentView(newDocumentViewModel);
        cardPanel.add(newDocumentView, newDocumentViewModel.getViewName());
        return this;
    }

    /**
     * Adds the Recommendation view to the application.
     * @return this builder
     */
    public NoteAppBuilder addRecommendationView() {
        recommendationMenuViewModel = new RecommendationMenuViewModel();
        return this;
    }

    /**
     * Adds the verify password use case (LockScreen).
     * @return this builder
     */
    public NoteAppBuilder addVerifyPasswordUseCase() {
        if (homeMenuViewModel == null) {
            addHomeMenuView();
        } else if (homeMenuPresenter == null) {
            homeMenuPresenter = new HomeMenuPresenter(homeMenuViewModel);
        }
        final LockScreenPresenter presenter = new LockScreenPresenter(
                lockScreenViewModel, viewManagerModel, homeMenuViewModel, homeMenuPresenter);
        final VerifyPasswordInputBoundary interactor = new VerifyPasswordInteractor(
                passwordDataAccess, presenter, noteDataAccess);
        final LockScreenController controller = new LockScreenController(interactor);

        final LockscreenView lockscreenView = new LockscreenView(lockScreenViewModel, controller);
        cardPanel.add(lockscreenView, lockScreenViewModel.getViewName());
        return this;
    }

    /**
     * Adds the home menu use cases (create entry, load entry, delete entry).
     * @return this builder
     */
    public NoteAppBuilder addHomeMenuUseCases() {
        // Create Entry Use Case
        final NewDocumentPresenter newDocumentPresenter = new NewDocumentPresenter(
                newDocumentViewModel, viewManagerModel);
        final CreateEntryInputBoundary createEntryInteractor = new CreateEntryInteractor(newDocumentPresenter);

        // Load Entry Use Case
        final LoadEntryInputBoundary loadEntryInteractor = new LoadEntryInteractor(
                newDocumentPresenter, noteDataAccess);

        // Delete Entry Use Case
        final DeleteEntryInputBoundary deleteEntryInteractor = new DeleteEntryInteractor(
                new use_case.delete_entry.DeleteEntryOutputBoundary() {
                    @Override
                    public void prepareSuccessView(use_case.delete_entry.DeleteEntryOutputData outputData) {
                        // Refresh the home menu view after deletion
                        // Could add logic here to refresh the home menu
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                },
                noteDataAccess
        );

        final HomeMenuController controller = new HomeMenuController(
                createEntryInteractor, loadEntryInteractor, deleteEntryInteractor);

        final HomeMenuView homeMenuView = new HomeMenuView(controller, homeMenuViewModel);
        cardPanel.add(homeMenuView, homeMenuViewModel.getViewName());
        return this;
    }

    /**
     * Adds the new document use cases (save entry, get recommendations, go back).
     * @return this builder
     */
    public NoteAppBuilder addNewDocumentUseCases() {
        if (recommendationMenuViewModel == null) {
            recommendationMenuViewModel = new RecommendationMenuViewModel();
        }
        // Save Entry Use Case
        final NewDocumentPresenter savePresenter = new NewDocumentPresenter(
                newDocumentViewModel, viewManagerModel);
        final SaveEntryInputBoundary saveEntryInteractor = new SaveEntryInteractor(
                savePresenter, noteDataAccess);

        // Get Recommendations Use Case (results go to recommendation menu)
        final RecommendationMenuPresenter recommendationPresenter = new RecommendationMenuPresenter(
                recommendationMenuViewModel, viewManagerModel);
        final GetRecommendationsInputBoundary getRecommendationsInteractor = new GetRecommendationsInteractor(
                recommendationDataAccess, recommendationPresenter);

        // Go Back Use Case (from NewDocument to HomeMenu)
        final GoBackPresenter goBackPresenter = new GoBackPresenter(
                viewManagerModel, homeMenuViewModel);
        final GoBackInputBoundary goBackInteractor = new GoBackInteractor(goBackPresenter);

        final NewDocumentController controller = new NewDocumentController(
                getRecommendationsInteractor, goBackInteractor, saveEntryInteractor);

        newDocumentView.setNewDocumentController(controller);
        return this;
    }

    /**
     * Adds the recommendation menu use case (go back from recommendations).
     * @return this builder
     */
    public NoteAppBuilder addRecommendationMenuUseCase() {
        if (recommendationMenuViewModel == null) {
            recommendationMenuViewModel = new RecommendationMenuViewModel();
        }
        // Go Back Use Case (from Recommendation to NewDocument)
        final GoBackPresenter goBackPresenter = new GoBackPresenter(
                viewManagerModel, newDocumentViewModel);
        final GoBackInputBoundary goBackInteractor = new GoBackInteractor(goBackPresenter);

        // Get Recommendations interactor and presenter for this view
        final RecommendationMenuPresenter recommendationPresenter = new RecommendationMenuPresenter(
                recommendationMenuViewModel, viewManagerModel);
        final GetRecommendationsInputBoundary getRecommendationsInteractor = new GetRecommendationsInteractor(
                recommendationDataAccess, recommendationPresenter);

        final RecommendationMenuController controller = new RecommendationMenuController(
                getRecommendationsInteractor, goBackInteractor);

        if (recommendationView == null) {
            try {
                recommendationView = new RecommendationView(
                        recommendationMenuViewModel,
                        controller,
                        recommendationMenuViewModel.getState()
                );
                cardPanel.add(recommendationView, recommendationMenuViewModel.getViewName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Guard against empty recommendationView.
            // This way we simply switch to the existing view when the back button is pressed rather than instantiating
            // a new one.
            recommendationView.setRecommendationController(controller);
        }
        return this;
    }

    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("MoodVerse - Mood-Based Diary & Recommendations");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1350, 1100);

        application.add(cardPanel);

        // Create a ViewManager to handle view switching
        new ViewManager(cardPanel, cardLayout, viewManagerModel);

        if (homeMenuPresenter != null && homeMenuViewModel != null) {
            viewManagerModel.addPropertyChangeListener(new HomeMenuRefreshListener(
                    homeMenuViewModel.getViewName(),
                    noteDataAccess,
                    homeMenuPresenter));
        }

        // Set initial view to LockScreen
        viewManagerModel.setState(lockScreenViewModel.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
