package interface_adapter.get_packing_tips;

import use_case.get_packing_tips.GetPackingTipsOutputBoundary;
import use_case.get_packing_tips.GetPackingTipsOutputData;

import java.util.List;

public class GetPackingTipsPresenter implements GetPackingTipsOutputBoundary {
    private final PackingTipsViewModel viewModel;

    public GetPackingTipsPresenter(PackingTipsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(GetPackingTipsOutputData outputData) {
        viewModel.setErrorMessage("");
        viewModel.setTips(outputData.tips());
    }

    @Override
    public void presentFailure(String errorMessage) {
        viewModel.setTips(List.of());
        viewModel.setErrorMessage(errorMessage);
    }

    public PackingTipsViewModel getViewModel() {
        return viewModel;
    }
}
