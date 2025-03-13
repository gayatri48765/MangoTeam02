package com.serotonin.mango.web.dwr;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest;
import com.serotonin.mango.web.dwr.longPoll.LongPollState;
import com.serotonin.mango.web.dwr.beans.WatchListState;
public class WatchListHandler implements RequestHandler {
    private final WatchListDwr watchListDwr = new WatchListDwr();
    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if (pollRequest.isWatchList() && user != null) {
            synchronized (state) {
                List<WatchListState> newStates = watchListDwr.getPointData();
                List<WatchListState> differentStates = new ArrayList<>();
                for (WatchListState newState : newStates) {
                    WatchListState oldState = state.getWatchListState(newState.getId());
                    if (oldState == null) {
                        differentStates.add(newState);
                    } else {
                        WatchListState copy = newState.clone();
                        copy.removeEqualValue(oldState);
                        if (!copy.isEmpty()) {
                            differentStates.add(copy);
                        }
                    }
                }
                if (!differentStates.isEmpty()) {
                    response.put("watchListStates", differentStates);
                    state.setWatchListStates(newStates);
                }
            }
        }
    }
}