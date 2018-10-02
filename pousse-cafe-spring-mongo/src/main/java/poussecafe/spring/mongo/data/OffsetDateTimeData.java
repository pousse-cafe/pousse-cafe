package poussecafe.spring.mongo.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@SuppressWarnings("serial")
public class OffsetDateTimeData implements Serializable {

    public static OffsetDateTimeData adapt(OffsetDateTime dateTime) {
        if(dateTime == null) {
            return null;
        } else {
            OffsetDateTimeData data = new OffsetDateTimeData();
            data.dateTime = dateTime.toLocalDateTime();
            data.offsetId = OffsetDateTime.now().getOffset().getId();
            return data;
        }
    }

    private LocalDateTime dateTime;

    private String offsetId;

    public OffsetDateTime adapt() {
        return dateTime.atOffset(ZoneOffset.of(offsetId));
    }
}
