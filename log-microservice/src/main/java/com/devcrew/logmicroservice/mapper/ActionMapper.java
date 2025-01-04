package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.model.Action;

/**
 * This class is used to map the Action entity to the ActionDTO and vice versa.
 */
public class ActionMapper {

    /**
     * This method is used to map the Action entity to the ActionDTO.
     * @param action The Action entity.
     * @return The ActionDTO.
     */
    public static ActionDTO toDTO(Action action) {
        return new ActionDTO(action.getId(), action.getName());
    }

    /**
     * This method is used to map the ActionDTO to the Action entity.
     * @param actionDTO The ActionDTO.
     * @return The Action entity.
     */
    public static Action toEntity(ActionDTO actionDTO) {
        Action action = new Action();

        if (actionDTO.getIdentifier() != null) {
            action.setId(actionDTO.getIdentifier());
        }

        action.setName(actionDTO.getName_action());

        return action;
    }
}
