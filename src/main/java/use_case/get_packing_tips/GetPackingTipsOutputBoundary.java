package use_case.get_packing_tips;

public interface GetPackingTipsOutputBoundary {
    void presentSuccess(GetPackingTipsOutputData outputData);
    void presentFailure(String errorMessage);
}
