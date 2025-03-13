package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.serotonin.mango.vo.User; // For the User class
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest; // For the LongPollRequest class
import com.serotonin.mango.web.dwr.longPoll.LongPollState; // For the LongPollState class
import com.serotonin.mango.web.dwr.beans.ViewComponentState;
import com.serotonin.mango.web.dwr.beans.WatchListState;
public class ViewHandler implements RequestHandler {
    private final ViewDwr viewDwr = new ViewDwr();

    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if ((pollRequest.isView() && user != null) || (pollRequest.isViewEdit() && user != null)
                || pollRequest.getAnonViewId() > 0) {
            List<ViewComponentState> newStates;
            if (pollRequest.getAnonViewId() > 0) {
                newStates = viewDwr.getViewPointDataAnon(pollRequest.getAnonViewId());
            } else {
                newStates = viewDwr.getViewPointData(pollRequest.isViewEdit());
            }
            List<ViewComponentState> differentStates = new ArrayList<>();

            for (ViewComponentState newState : newStates) {
                ViewComponentState oldState = state.getViewComponentState(newState.getId());
                if (oldState == null) {
                    differentStates.add(newState);
                } else {
                    ViewComponentState copy = newState.clone();
                    copy.removeEqualValue(oldState);
                    if (!copy.isEmpty()) {
                        differentStates.add(copy);
                    }
                }
            }

            if (!differentStates.isEmpty()) {
                response.put("viewStates", differentStates);
                state.setViewComponentStates(newStates);
            }
        }
    }
}