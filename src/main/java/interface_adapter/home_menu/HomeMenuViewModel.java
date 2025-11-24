package interface_adapter.home_menu;
import interface_adapter.ViewModel;

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
