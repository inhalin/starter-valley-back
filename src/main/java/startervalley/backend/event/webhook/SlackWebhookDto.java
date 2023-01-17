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

        if (o.getClass() == Lunchbus.class) {
            Lunchbus bus = (Lunchbus) o;

            sb.append("🚌 런치버스가 승객을 모집합니다.\n\n")
                    .append("제목: ").append(bus.getTitle()).append("\n")
                    .append("운전자: ").append(bus.getDriver().getName()).append("\n")
                    .append("모집인원: ").append(bus.getOccupancy()).append("\n");

            if (bus.getClosedDate() != null) {
                sb.append("출발시간: ").append(bus.getClosedDate().format(DateTimeFormatter.ofPattern("MM.dd. E요일 HH시 mm분")));
            }

            return sb.toString();
        } else if (o.getClass() == Notice.class) {
            Notice notice = (Notice) o;

            return sb.append("🔔 공지사항이 등록되었습니다.\n\n")
                    .append("제목: ").append(notice.getTitle()).append("\n")
                    .append("작성자: ").append(notice.getAdminUser().getName()).append("\n")
                    .toString();
        }

        throw new ResourceNotValidException("사용 가능한 객체가 아닙니다.");
    }
}
