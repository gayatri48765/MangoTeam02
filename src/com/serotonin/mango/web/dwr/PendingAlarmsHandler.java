package com.serotonin.mango.web.dwr;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest;
import com.serotonin.mango.web.dwr.longPoll.LongPollState;
import java.util.HashMap;
import java.util.Map;
import org.directwebremoting.WebContextFactory;
import com.serotonin.util.StringUtils;
import com.serotonin.mango.db.dao.EventDao;
import javax.servlet.http.HttpServletRequest;
public class PendingAlarmsHandler implements RequestHandler {
    private final MiscDwr miscDwr;

    public PendingAlarmsHandler(MiscDwr miscDwr) {
        this.miscDwr = miscDwr;
    }
    @Override
    public void handleRequest(LongPollRequest pollRequest, User user, Map<String, Object> response, LongPollState state) {
        if (pollRequest.isPendingAlarms() && user != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("events", new EventDao().getPendingEvents(user.getId()));
            model.put("pendingEvents", true);
            model.put("noContentWhenEmpty", true);
            HttpServletRequest httpRequest = WebContextFactory.get().getHttpServletRequest();
            String currentContent = miscDwr.generateContent(httpRequest, "eventList.jsp", model);
            currentContent = StringUtils.trimWhitespace(currentContent);

            if (!StringUtils.isEqual(currentContent, state.getPendingAlarmsContent())) {
                response.put("pendingAlarmsContent", currentContent);
                state.setPendingAlarmsContent(currentContent);
            }
        }
    }
}