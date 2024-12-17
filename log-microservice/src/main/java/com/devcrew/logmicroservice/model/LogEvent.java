package com.devcrew.logmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table (
        name = "LOG_EVENT",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class LogEvent {

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    @NotNull
    private Action actionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @NotNull
    private AppModule moduleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    @NotNull
    private AppEntity entityId;

    //It comes from a RestTemplate call to the user microservice
    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    //It will never be updated (The log is created only once)
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "jsonBefore")
    @NotNull
    private String jsonBefore;

    @Column(name = "jsonAfter")
    @NotNull
    private String jsonAfter;

    public LogEvent(Action actionId,
                    AppModule moduleId,
                    AppEntity entityId,
                    Integer userId,
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
