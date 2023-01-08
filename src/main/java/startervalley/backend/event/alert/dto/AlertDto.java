package startervalley.backend.event.alert.dto;

public class AlertDto {

    private AlertType type;

    private Long id;

    private String message;

    public AlertDto(AlertType type, String message) {
        this.type = type;
        this.message = message;
    }

    public AlertDto(AlertType type, Long id, String message) {
        this.type = type;
        this.id = id;
        this.message = message;
    }
}
