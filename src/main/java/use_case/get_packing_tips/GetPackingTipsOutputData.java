package use_case.get_packing_tips;

import java.util.List;

public record GetPackingTipsOutputData(List<String> tips) {

    public boolean hasTips() {
        return !tips.isEmpty();
    }
}
