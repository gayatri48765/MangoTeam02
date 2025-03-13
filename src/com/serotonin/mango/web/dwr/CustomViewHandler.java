package com.serotonin.mango.web.dwr;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.serotonin.mango.vo.User; // For the User class
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest; // For the LongPollRequest class
import com.serotonin.mango.web.dwr.longPoll.LongPollState; // For the LongPollState class
import com.serotonin.mango.web.dwr.beans.CustomComponentState;
public class CustomViewHandler implements RequestHandler {
    private final CustomViewDwr customViewDwr = new CustomViewDwr();

    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if (pollRequest.isCustomView()) {
            List<CustomComponentState> newStates = customViewDwr.getViewPointData();
            List<CustomComponentState> differentStates = new ArrayList<>();

            for (CustomComponentState newState : newStates) {
                CustomComponentState oldState = state.getCustomViewState(newState.getId());
                if (oldState == null) {
                    differentStates.add(newState);
                } else {
                    CustomComponentState copy = newState.clone();
                    copy.removeEqualValue(oldState);
                    if (!copy.isEmpty()) {
                        differentStates.add(copy);
                    }
                }
            }
            if (!differentStates.isEmpty()) {
                response.put("customViewStates", differentStates);
                state.setCustomViewStates(newStates);
            }
        }
    }
}