package interface_adapter.recommendation_menu;


import interface_adapter.ViewModel;

public class RecommendationMenuViewModel extends ViewModel<RecommendationMenuState> {

    public RecommendationMenuViewModel() {
        super("recommendation_menu");
        setState(new RecommendationMenuState());
    }
}

