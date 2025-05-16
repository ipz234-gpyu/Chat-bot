package bot.domain;

import bot.domain.Interface.Identifiable;

import java.util.UUID;

public class HistoryEntry implements Identifiable {
    private UUID id;
    private String sender;
    private String message;
    private long timestamp;

    public HistoryEntry() {}

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
