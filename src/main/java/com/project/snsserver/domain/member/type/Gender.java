package com.project.snsserver.domain.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public enum Gender {

    MALE, FEMALE;

    @JsonCreator
    public static Gender from (String gender) {
        for(Gender g : Gender.values()) {
            if(Objects.equals(g.name(), gender.toUpperCase())) {
                return g;
            }
        }
        return null;
    }
}
