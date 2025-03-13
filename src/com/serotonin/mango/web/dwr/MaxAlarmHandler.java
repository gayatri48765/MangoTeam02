package com.serotonin.mango.web.dwr;

import java.util.Map;
import com.serotonin.mango.vo.User; // For the User class
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest; // For the LongPollRequest class
import com.serotonin.mango.web.dwr.longPoll.LongPollState; // For the LongPollState class
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.EventDao;

public class MaxAlarmHandler implements RequestHandler {
    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if (pollRequest.isMaxAlarm() && user != null) {
            long lastEMUpdate = Common.ctx.getEventManager().getLastAlarmTimestamp();
            if (state.getLastAlarmLevelChange() < lastEMUpdate) {
                state.setLastAlarmLevelChange(lastEMUpdate);
                int maxAlarmLevel = new EventDao().getHighestUnsilencedAlarmLevel(user.getId());
                if (maxAlarmLevel != state.getMaxAlarmLevel()) {
                    response.put("highestUnsilencedAlarmLevel", maxAlarmLevel);
                    state.setMaxAlarmLevel(maxAlarmLevel);
                }
            }
        }
    }
}