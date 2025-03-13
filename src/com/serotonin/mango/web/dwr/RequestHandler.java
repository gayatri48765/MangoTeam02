package com.serotonin.mango.web.dwr;

import java.util.Map;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest;
import com.serotonin.mango.web.dwr.longPoll.LongPollState;

public interface RequestHandler {
    void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state);
}