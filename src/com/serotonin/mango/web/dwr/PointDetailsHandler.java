package com.serotonin.mango.web.dwr;

import java.util.Map;
import com.serotonin.mango.vo.User; // For the User class
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest; // For the LongPollRequest class
import com.serotonin.mango.web.dwr.longPoll.LongPollState; // For the LongPollState class
import com.serotonin.mango.web.dwr.beans.ViewComponentState;
import com.serotonin.mango.web.dwr.beans.WatchListState;
public class PointDetailsHandler implements RequestHandler {
    private final DataPointDetailsDwr dataPointDetailsDwr = new DataPointDetailsDwr();

    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if (pollRequest.isPointDetails() && user != null) {
            WatchListState newState = dataPointDetailsDwr.getPointData();
            WatchListState responseState;
            WatchListState oldState = state.getPointDetailsState();

            if (oldState == null) {
                responseState = newState;
            } else {
                responseState = newState.clone();
                responseState.removeEqualValue(oldState);
            }

            if (!responseState.isEmpty()) {
                response.put("pointDetailsState", responseState);
                state.setPointDetailsState(newState);
            }
        }
    }
}