package interface_adapter.get_packing_tips;

import use_case.get_packing_tips.GetPackingTipsInputBoundary;
import use_case.get_packing_tips.GetPackingTipsInputData;

public class GetPackingTipsController {
    private final GetPackingTipsInputBoundary interactor;

    public GetPackingTipsController(GetPackingTipsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void getPackingTips(double latitude, double longitude) {
        GetPackingTipsInputData inputData = new GetPackingTipsInputData(latitude, longitude);
        interactor.execute(inputData);
    }
}
