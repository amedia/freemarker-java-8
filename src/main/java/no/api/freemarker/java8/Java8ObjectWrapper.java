/*
 * Copyright (c) 2015-2015 Amedia Utvikling AS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.api.freemarker.java8;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import no.api.freemarker.java8.time.ClockAdapter;
import no.api.freemarker.java8.time.DurationAdapter;
import no.api.freemarker.java8.time.InstantAdapter;
import no.api.freemarker.java8.time.LocalDateAdapter;
import no.api.freemarker.java8.time.LocalDateTimeAdapter;
import no.api.freemarker.java8.time.LocalTimeAdapter;
import no.api.freemarker.java8.time.MonthDayAdapter;
import no.api.freemarker.java8.time.OffsetDateTimeAdapter;
import no.api.freemarker.java8.time.OffsetTimeAdapter;
import no.api.freemarker.java8.time.PeriodAdapter;
import no.api.freemarker.java8.time.TemporalDialerAdapter;
import no.api.freemarker.java8.time.YearAdapter;
import no.api.freemarker.java8.time.YearMonthAdapter;
import no.api.freemarker.java8.time.ZoneIdAdapter;
import no.api.freemarker.java8.time.ZoneOffsetAdapter;
import no.api.freemarker.java8.time.ZonedDateTimeAdapter;
import no.api.freemarker.java8.zone.KeepingZonedDateTimeStrategy;
import no.api.freemarker.java8.zone.ZonedDateTimeStrategy;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

/**
 * Freemarker ObjectWrapper that extends the DefaultObjectWrapper with support for all classes in the new java.time api.
 */
public class Java8ObjectWrapper extends DefaultObjectWrapper {

    private ZonedDateTimeStrategy strategy;


    public Java8ObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
        this.strategy = new KeepingZonedDateTimeStrategy();
    }


    public Java8ObjectWrapper(Version incompatibleImprovements, ZonedDateTimeStrategy strategy) {
        super(incompatibleImprovements);
        this.strategy = strategy;
    }


    public void setZonedDateTimeStrategy(ZonedDateTimeStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
        TemplateModel delegate = _handleUnknownType(obj);
        return obj instanceof Temporal && delegate instanceof TemplateHashModel
                ? new TemporalDialerAdapter((Temporal) obj, this, (TemplateHashModel) delegate)
                : delegate;
    }

    private TemplateModel _handleUnknownType(Object obj) throws TemplateModelException {
        if (obj instanceof Clock) {
            return new ClockAdapter((Clock) obj, this);
        } else if (obj instanceof Duration) {
            return new DurationAdapter((Duration) obj, this);
        } else if (obj instanceof Instant) {
            return new InstantAdapter((Instant) obj, this);
        } else if (obj instanceof LocalDate) {
            return new LocalDateAdapter((LocalDate) obj, this);
        } else if (obj instanceof LocalDateTime) {
            return new LocalDateTimeAdapter((LocalDateTime) obj, this);
        } else if (obj instanceof LocalTime) {
            return new LocalTimeAdapter((LocalTime) obj, this);
        } else if (obj instanceof MonthDay) {
            return new MonthDayAdapter((MonthDay) obj, this);
        } else if (obj instanceof OffsetDateTime) {
            return new OffsetDateTimeAdapter((OffsetDateTime) obj, this);
        } else if (obj instanceof OffsetTime) {
            return new OffsetTimeAdapter((OffsetTime) obj, this);
        } else if (obj instanceof Period) {
            return new PeriodAdapter((Period) obj, this);
        } else if (obj instanceof Year) {
            return new YearAdapter((Year) obj, this);
        } else if (obj instanceof YearMonth) {
            return new YearMonthAdapter((YearMonth) obj, this);
        } else if (obj instanceof ZonedDateTime) {
            return new ZonedDateTimeAdapter((ZonedDateTime) obj, this, strategy);
        } else if (obj instanceof ZoneOffset) {
            return new ZoneOffsetAdapter((ZoneOffset) obj, this);
        } else if (obj instanceof ZoneId) {
            return new ZoneIdAdapter((ZoneId) obj, this);
        }
        return super.handleUnknownType(obj);
    }
}