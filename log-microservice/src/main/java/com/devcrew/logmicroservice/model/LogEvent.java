package com.devcrew.logmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class represents the LogEvent entity, which is used to store the logs of the system.
 */
@Entity
@Table (
        name = "LOG_EVENT",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The id of the log event.
     */
    @Id
    @SequenceGenerator(
            name = "log_event_sequence",
            sequenceName = "log_event_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "log_event_sequence"
    )
    private Integer id;

    /**
     * The action that was performed.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    @NotNull
    private Action actionId;

    /**
     * The module that was affected.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @NotNull
    private AppModule moduleId;

    /**
     * The entity that was affected.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    @NotNull
    private AppEntity entityId;

    /**
     * The user that performed the action.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private LogUser userId;

    /**
     * The date and time when the log was created.
     */
    //It will never be updated (The log is created only once)
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    /**
     * The description of the log.
     */
    @Column(name = "description")
    @NotNull
    private String description;

    /**
     * The state of the entity before the action was performed.
     */
    @Column(name = "json_Before")
    @NotNull
    private String jsonBefore;

    /**
     * The state of the entity after the action was performed.
     */
    @Column(name = "json_After")
    @NotNull
    private String jsonAfter;

    public LogEvent(Action actionId,
                    AppModule moduleId,
                    AppEntity entityId,
                    LogUser userId,
                    String description,
                    String jsonBefore,
                    String jsonAfter) {
        this.actionId = actionId;
        this.moduleId = moduleId;
        this.entityId = entityId;
        this.userId = userId;
        this.description = description;
        this.jsonBefore = jsonBefore;
        this.jsonAfter = jsonAfter;
    }
}
