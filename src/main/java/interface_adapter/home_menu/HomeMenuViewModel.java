package interface_adapter.home_menu;
import interface_adapter.ViewModel;

/**
 * View model for the home menu view.
 * This class holds the HomeMenuState used by the home menu UI and
 * notifies its observers when the state changes. Presenters update this
 * view model, and views listen for property changes to refresh the screen.
 */

public class HomeMenuViewModel extends ViewModel{

    public static final String Title = "MoodVerse";
    private HomeMenuState state = new HomeMenuState();

    public HomeMenuViewModel() {
        super("HomeMenu");
    }

    public HomeMenuState getState() {
        return state;
    }

    public void setState(HomeMenuState state) {
        this.state = state;
        this.firePropertyChanged();
    }
}