package com.dlvr.continuum;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.datum.impl.BaseFields;
import com.dlvr.continuum.series.datum.impl.BaseDatum;
import com.dlvr.continuum.series.datum.impl.BaseTags;
import com.dlvr.continuum.series.query.Function;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.impl.BaseQuery;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum {
    public static void sayHello() {
        System.out.printf("Hello");
    }

    public static DatumBuilder datum(String name) {
        return new DatumBuilder(name);
    }

    public static Tags tags(Map<String, String> tags) {
        return new BaseTags(tags);
    }

    public static Fields fields(Map<String, Object> fields) {
        return new BaseFields(fields);
    }

    public static QueryBuilder query(String name) {
        return new QueryBuilder(name);
    }

    public static class DatumBuilder {

        private final BaseDatum target = new BaseDatum();

        private DatumBuilder() {}
        DatumBuilder(String name) {
            target.name = name;
        }
        public DatumBuilder tags(Tags tags) {
            target.tags = (BaseTags)tags;
            return this;
        }
        public DatumBuilder fields(Fields fields) {
            target.fields = (BaseFields)fields;
            return this;
        }
        public DatumBuilder timestamp(long timestamp) {
            target.timestamp = timestamp;
            return this;
        }
        public DatumBuilder value(double value) {
            target.value = value;
            return this;
        }
        public Datum build() {
            return target;
        }
    }

    static class QueryBuilder {
        private final BaseQuery target = new BaseQuery();
        private QueryBuilder() {}
        QueryBuilder(String name) {
            target.name = name;
        }
        public QueryBuilder start(long start) {
            target.start = start;
            return this;
        }
        public QueryBuilder end(long end) {
            target.end = end;
            return this;
        }
        public QueryBuilder interval(TimeUnit interval) {
            target.interval = interval;
            return this;
        }
        public QueryBuilder function(Function function) {
            target.function = function;
            return this;
        }
        public QueryBuilder tags(Tags tags) {
            target.tags = tags;
            return this;
        }
        public QueryBuilder fields(Fields fields) {
            target.fields = fields;
            return this;
        }

        public Query build() {
            return target;
        }
    }
}
