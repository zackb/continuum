package com.dlvr.continuum;

import com.dlvr.continuum.series.point.Fields;
import com.dlvr.continuum.series.point.Point;
import com.dlvr.continuum.series.point.Tags;
import com.dlvr.continuum.series.point.impl.BaseFields;
import com.dlvr.continuum.series.point.impl.BasePoint;
import com.dlvr.continuum.series.point.impl.BaseTags;
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

    public static PointBuilder point(String name) {
        return new PointBuilder(name);
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

    static class PointBuilder {

        private final BasePoint target = new BasePoint();

        private PointBuilder() {}
        PointBuilder(String name) {
            target.name = name;
        }
        public PointBuilder tags(Tags tags) {
            target.tags = tags;
            return this;
        }
        public PointBuilder fields(Fields fields) {
            target.fields = fields;
            return this;
        }
        public PointBuilder timestamp(long timestamp) {
            target.timestamp = timestamp;
            return this;
        }
        public PointBuilder value(double value) {
            target.value = value;
            return this;
        }
        public Point build() {
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
