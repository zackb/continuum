package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.db.query.Const;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Series based QueryID implementation
 * Created by zack on 2/12/16.
 */
public class NQueryID implements QueryID {

    private static final byte b = 0x0;

    private final byte[] id;

    public NQueryID(String name, Tags tags) {
        byte[] bname = Bytes.bytes(name);
        byte[] btags = encode(tags);
        id = new byte[bname.length + btags.length + 1];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bname);
        buff.put(b);
        buff.put(btags);
    }

    /**
     * build tag/value structure omitting values with wildcard
     * @param tags to use
     * @return bytes to use for query
     */
    static byte[] encode(Tags tags) {
        byte[] bid = new byte[1024];
        List<String> names = tags.names();
        int len = names.size();

        int pos = 0;
        byte[] tmp;

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            tmp = Bytes.bytes(name);
            bid = Bytes.append(bid, pos, tmp);
            pos += tmp.length;
            bid[pos++] = b;
        }

        for (int i = 0; i < names.size(); i++) {
            String val = tags.get(names.get(i));
            if (val.equals(Const.SWILDCARD)) break;
            tmp = Bytes.bytes(val);
            bid = Bytes.append(bid, pos, tmp);
            pos += tmp.length;
            if (i < len - 1) bid[pos++] = b;
        }

        return Bytes.range(bid, 0, pos);
    }

    @Override
    public String string() {
        return Bytes.String(id);
    }

    @Override
    public byte[] bytes() {
        return id;
    }
}
