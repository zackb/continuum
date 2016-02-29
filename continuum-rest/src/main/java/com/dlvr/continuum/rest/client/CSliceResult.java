package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.core.atom.NValues;
import com.dlvr.continuum.core.atom.SAtom;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.List;

/**
 *
 * Created by zack on 2/26/16.
 */
public class CSliceResult implements SliceResult {

    public String name;
    public NValues values;
    public List<SliceResult> children;
    public List<SAtom> atoms;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Values values() {
        return values;
    }

    @Override
    public List<SliceResult> children() {
        return children;
    }

    @Override
    public List<Atom> atoms() {
        return null;
    }

    @Override
    public void addChild(SliceResult sliceResult) {

    }

    @Override
    public SliceResult getChild(String s) {
        return null;
    }

    @Override
    public SliceResult getChild(int i) {
        return null;
    }

    @Override
    public long timestamp() {
        return 0;
    }
}
