package interface_adapter.home_menu;
import interface_adapter.ViewModel;

public class HomeMenuViewModel extends ViewModel<HomeMenuState> {

    public static final String Title = "MoodVerse";

    public HomeMenuViewModel() {
        super("HomeMenu");
        setState(new HomeMenuState());
    }
}
