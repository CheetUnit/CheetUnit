/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gepardec.cheetunit.examples.college.eager.web;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@FacesConverter("com.gepardec.cheetunit.examples.college.eager.web.LocalDateConverter")
public class LocalDateConverter extends DateTimeConverter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        Object o = super.getAsObject(facesContext, uiComponent, value);

        if (o == null) {
            return null;
        }

        if (o instanceof Date) {
            Instant instant = Instant.ofEpochMilli(((Date) o).getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        } else {
            throw new IllegalArgumentException(String.format("value=%s could not be converted to a LocalDate, result super.getAsObject=%s", value, o));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null) {
            return super.getAsString(facesContext, uiComponent,value);
        }

        if (value instanceof LocalDate) {
            LocalDate lDate = (LocalDate) value;
            Instant instant = lDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            return super.getAsString(facesContext, uiComponent, date);
        } else {
            throw new IllegalArgumentException(String.format("value=%s is not a instanceof LocalDate", value));
        }
    }
}
