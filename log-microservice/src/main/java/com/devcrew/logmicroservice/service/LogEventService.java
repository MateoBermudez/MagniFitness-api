package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.dto.LogEventFilter;
import com.devcrew.logmicroservice.dto.PaginatedLogsResponse;
import com.devcrew.logmicroservice.mapper.ActionMapper;
import com.devcrew.logmicroservice.mapper.AppEntityMapper;
import com.devcrew.logmicroservice.mapper.AppModuleMapper;
import com.devcrew.logmicroservice.mapper.LogEventMapper;
import com.devcrew.logmicroservice.model.Action;
import com.devcrew.logmicroservice.model.AppEntity;
import com.devcrew.logmicroservice.model.AppModule;
import com.devcrew.logmicroservice.model.LogEvent;
import com.devcrew.logmicroservice.repository.ActionRepository;
import com.devcrew.logmicroservice.repository.AppEntityRepository;
import com.devcrew.logmicroservice.repository.LogEventRepository;
import com.devcrew.logmicroservice.repository.ModuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for LogEvent entity
 * Contains methods for getting, saving, deleting and filtering logs
 * Also contains methods for getting a single log and deleting all logs
 */
@Service
public class LogEventService {

    /**
     * Repository for LogEvent entity
     */
    private final LogEventRepository logEventRepository;

    /**
     * Repository for AppEntity entity
     */
    private final AppEntityRepository appEntityRepository;

    /**
     * Repository for Action entity
     */
    private final ActionRepository actionRepository;

    /**
     * Repository for AppModule entity
     */
    private final ModuleRepository moduleRepository;

    /**
     * Constructor for LogEventService, initializes repositories
     * @param logEventRepository Repository for LogEvent entity
     * @param appEntityRepository Repository for AppEntity entity
     * @param actionRepository Repository for Action entity
     * @param moduleRepository Repository for AppModule entity
     */
    @Autowired
    public LogEventService(LogEventRepository logEventRepository, AppEntityRepository appEntityRepository, ActionRepository actionRepository, ModuleRepository moduleRepository) {
        this.logEventRepository = logEventRepository;
        this.appEntityRepository = appEntityRepository;
        this.actionRepository = actionRepository;
        this.moduleRepository = moduleRepository;
    }

    /**
     * Method for getting all logs
     * @return List of LogEventDTO objects
     */
    public List<LogEventDTO> getLogs() {
        List<LogEvent> logs = logEventRepository.findAll();
        return logs.stream().map(LogEventMapper::toDTO).toList();
    }

    /**
     * Method for getting paginated logs
     * @param page Page number
     * @param size Number of logs per page
     * @param filter Filter for logs
     * @param sortDirection Sort direction
     * @return PaginatedLogsResponse object
     */
    public PaginatedLogsResponse getPaginatedLogs(Integer page,
                                              Integer size,
                                              LogEventFilter filter,
                                              String sortDirection) {
        Sort sort = Sort.by("creationDate");
        sort = sortDirection.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        LogEvent probe = getProbe(filter);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("creationDate", "description", "jsonBefore", "jsonAfter");

        Example<LogEvent> example = Example.of(probe, matcher);

        Page<LogEvent> logs;
        long totalElements;

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            LocalDateTime startDate = LocalDateTime.parse(filter.getStartDate());
            LocalDateTime endDate = LocalDateTime.parse(filter.getEndDate());
            Page<LogEvent> filteredLogs = logEventRepository.findAll(example, pageable);
            logs = new PageImpl<>(filteredLogs.stream()
                    .filter(log -> {
                        LocalDateTime creationDate = log.getCreationDate();
                        return creationDate.isAfter(startDate) && creationDate.isBefore(endDate);
                    })
                    .collect(Collectors.toList()), pageable, filteredLogs.getTotalElements());
        } else {
            logs = logEventRepository.findAll(example, pageable);
        }
        totalElements = logs.getTotalElements();

        Page<LogEventDTO> logEventDTOPage = logs.map(LogEventMapper::toDTO);
        return new PaginatedLogsResponse(logEventDTOPage, totalElements);
    }

    /**
     * Method for getting a probe for filtering logs
     * @param filter Filter for logs
     * @return LogEvent object with filter parameters
     */
    private static LogEvent getProbe(LogEventFilter filter) {
        LogEvent probe = new LogEvent();
        if (filter.getId() != null) probe.setId(filter.getId());
        if (filter.getAction() != null) probe.setActionId(ActionMapper.toEntity(filter.getAction()));
        if (filter.getModule() != null) probe.setModuleId(AppModuleMapper.toEntity(filter.getModule()));
        if (filter.getEntity() != null) probe.setEntityId(AppEntityMapper.toEntity(filter.getEntity()));
        if (filter.getUserId() != null) probe.setUserId(filter.getUserId());
        return probe;
    }

    /**
     * Method for saving a log event
     * @param logEventJSON JSON representation of a log event
     * @throws Exception if JSON is invalid or entity, module or action is not found
     */
    @Transactional
    public void saveLogEvent(String logEventJSON) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LogEvent logEvent = objectMapper.readValue(logEventJSON, LogEvent.class);
        logEventRepository.save(mapLogEvent(logEvent));
    }

    /**
     * Method for mapping log event, setting entity, module and action IDs and names.
     * It was created because LogEvents usually came without Entity, Module and Action Names.
     * @param logEvent LogEvent object
     * @return LogEvent object with entity, module and action IDs and names
     */
    private LogEvent mapLogEvent(LogEvent logEvent) {
        logEvent.setEntityId(extractEntity(logEvent));
        logEvent.setActionId(extractActions(logEvent));
        logEvent.setModuleId(extractModules(logEvent));
        return logEvent;
    }

    /**
     * Method for extracting entity from log event and setting its ID and name
     * @param logEvent LogEvent object
     * @return AppEntity object
     */
    private AppEntity extractEntity(LogEvent logEvent) {
        AppEntity entity = logEvent.getEntityId();
        Integer id = appEntityRepository.findByName(entity.getName()).orElseThrow(
                () -> new IllegalArgumentException("Entity with name " + entity.getName() + " not found")
        ).getId();
        entity.setId(id);
        return entity;
    }

    /**
     * Method for extracting module from log event and setting its ID and name
     * @param logEvent LogEvent object
     * @return AppModule object
     */
    private AppModule extractModules(LogEvent logEvent) {
        AppModule module = logEvent.getModuleId();
        Integer id = moduleRepository.findByName(module.getName()).orElseThrow(
                () -> new IllegalArgumentException("Module with name " + module.getName() + " not found")
        ).getId();
        module.setId(id);
        return module;
    }

    /**
     * Method for extracting action from log event and setting its ID and name
     * @param logEvent LogEvent object
     * @return Action object
     */
    private Action extractActions(LogEvent logEvent) {
        Action action = logEvent.getActionId();
        Integer id = actionRepository.findByName(logEvent.getActionId().getName()).orElseThrow(
                () -> new IllegalArgumentException("Action with name " + logEvent.getActionId().getName() + " not found")
        ).getId();
        action.setId(id);
        return action;
    }

    /**
     * Method for getting a single log by ID
     * @param id Log ID to get
     * @return LogEventDTO object
     */
    public LogEventDTO getLog(Integer id) {
        LogEvent log = logEventRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Log with id " + id + " not found")
        );
        return LogEventMapper.toDTO(log);
    }

    /**
     * Method for deleting a single log by ID
     * @param id Log ID to delete
     */
    @Transactional
    public void deleteLog(Integer id) {
        logEventRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Log with id " + id + " not found")
        );
        logEventRepository.deleteById(id);
    }

    /**
     * Method for deleting all logs from the database
     */
    @Transactional
    public void deleteLogs() {
        logEventRepository.deleteAll();
    }
}
