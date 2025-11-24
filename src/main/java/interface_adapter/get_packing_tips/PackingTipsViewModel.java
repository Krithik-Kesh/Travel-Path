package interface_adapter.get_packing_tips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackingTipsViewModel {
    private List<String> tips = new ArrayList<>();
    private String errorMessage = "";

    public List<String> getTips() {
        return Collections.unmodifiableList(tips);
    }

    public void setTips(List<String> tips) {
        this.tips = new ArrayList<>(tips);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }
}
