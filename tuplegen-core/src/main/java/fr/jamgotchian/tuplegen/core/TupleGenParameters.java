/*
 * Copyright (C) 2012 Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.jamgotchian.tuplegen.core;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class TupleGenParameters {

    private int tupleLength;

    private boolean latinName = Boolean.TRUE;

    private String packageName;

    private float sourceVersion = 1.6f;

    private String sourceEncoding = "UTF-8";

    public TupleGenParameters() {
    }

    public int getTupleLength() {
        return tupleLength;
    }

    public void setTupleLength(int tupleLength) {
        this.tupleLength = tupleLength;
    }

    public boolean isLatinName() {
        return latinName;
    }

    public void setLatinName(boolean latinName) {
        this.latinName = latinName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public float getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(float sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getSourceEncoding() {
        return sourceEncoding;
    }

    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

}
