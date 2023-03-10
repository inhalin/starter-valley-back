package startervalley.backend.event.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import startervalley.backend.entity.Lunchbus;
import startervalley.backend.entity.Notice;
import startervalley.backend.exception.ResourceNotValidException;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlackWebhookDto {
    private String text;

    public static SlackWebhookDto of(Object o) {
        return new SlackWebhookDto(objToText(o));
    }

    private static String objToText(Object o) {
        StringBuilder sb = new StringBuilder();
        final String releaseUri = "https://starter-valley.site";

        if (o.getClass() == Lunchbus.class) {
            Lunchbus bus = (Lunchbus) o;

            return sb.append("ð ë°ì¹ë²ì¤ê° ì¹ê°ì ëª¨ì§í©ëë¤.\n\n")
                    .append("ì ëª©: ").append(bus.getTitle()).append("\n")
                    .append("ì´ì ì: ").append(bus.getDriver().getName()).append("\n")
                    .append("ëª¨ì§ì¸ì: ").append(bus.getOccupancy()).append("\n")
                    .append("ì¶ë°ìê°: ").append(bus.getClosedDate() != null ? bus.getClosedDate().format(DateTimeFormatter.ofPattern("MM.dd. E HHì mmë¶")) : "ë¯¸ì ").append("\n\n")
                    .append("ë°ë¡ê°ê¸° ð ").append(releaseUri).append("/lunchbus/").append(bus.getId())
                    .toString();
        } else if (o.getClass() == Notice.class) {
            Notice notice = (Notice) o;

            return sb.append("ð ê³µì§ì¬í­ì´ ë±ë¡ëììµëë¤.\n\n")
                    .append("ì ëª©: ").append(notice.getTitle()).append("\n")
                    .append("ìì±ì: ").append(notice.getAdminUser().getName()).append("\n\n")
                    .append("ë°ë¡ê°ê¸° ð ").append(releaseUri).append("/notice/").append(notice.getId())
                    .toString();
        }

        throw new ResourceNotValidException("ì¬ì© ê°ë¥í ê°ì²´ê° ìëëë¤.");
    }
}
