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

package no.api.freemarker.java8.time;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import no.api.freemarker.java8.config.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static no.api.freemarker.java8.time.DateTimeTools.*;

/**
 * LocalTimeAdapter adds basic format support for {@link LocalTime} too FreeMarker 2.3.23 and above.
 */
public class LocalTimeAdapter extends AbstractAdapter<LocalTime> implements AdapterTemplateModel,
        TemplateScalarModel, TemplateHashModel {

    public LocalTimeAdapter(LocalTime obj, Configuration cfg) {
        super(obj, cfg);
    }

    @Override
    public TemplateModel get(String s) throws TemplateModelException {
        if (METHOD_FORMAT.equals(s)) {
            return new LocalTimeFormatter(getObject());
        } else if(METHOD_EQUALS.equals(s) || METHOD_AFTER.equals(s) || METHOD_BEFORE.equals(s)) {
            return new LocalTimeChecker(getObject(), s);
        }
        throw new TemplateModelException(METHOD_UNKNOWN_MSG + s);
    }

    public class LocalTimeFormatter extends AbstractFormatter<LocalTime> implements TemplateMethodModelEx {

        public LocalTimeFormatter(LocalTime obj) {
            super(obj);
        }

        @Override
        public Object exec(List list) throws TemplateModelException {
            return getObject().format(createDateTimeFormatter(list, 0, DateTimeFormatter.ISO_LOCAL_TIME));
        }
    }

    public class LocalTimeChecker extends AbstractChecker<LocalTime> implements TemplateMethodModelEx {
        private String method;

        public LocalTimeChecker(LocalTime obj, String method) {
            super(obj);
            this.method = method;
        }

        @Override
        public Object exec(List list) throws TemplateModelException {
            LocalTimeAdapter adapter = (LocalTimeAdapter) list.get(0);
            switch(method) {
                case METHOD_EQUALS:
                    return getObject().equals(adapter.getObject());
                case METHOD_AFTER:
                    return getObject().isAfter(adapter.getObject());
                case METHOD_BEFORE:
                    return getObject().isBefore(adapter.getObject());
            }
            throw new TemplateModelException("method not implemented");
        }
    }
}
