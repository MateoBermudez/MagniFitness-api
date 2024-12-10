package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.model.Action;

public class ActionMapper {
    public static ActionDTO toDTO(Action action) {
        return new ActionDTO(action.getId(), action.getName());
    }

    public static Action toEntity(ActionDTO actionDTO) {
        Action action = new Action();

        if (actionDTO.getIdentifier() != null) {
            action.setId(actionDTO.getIdentifier());
        }

        action.setName(actionDTO.getName_action());

        return action;
    }
}
