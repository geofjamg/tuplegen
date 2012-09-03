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
public class UserDefinedTupleModel extends AbstractTupleModel {

    private final String tupleName;

    private final String[] elementsName;

    private final String[] elementsType;

    public UserDefinedTupleModel(String templateDir, String packageName, float sourceVersion,
                          String sourceEncoding, String tupleName, String[] elementsName,
                          String[] elementsType) {
        super(templateDir, packageName, sourceVersion, sourceEncoding);
        this.tupleName = tupleName;
        this.elementsName = elementsName;
        this.elementsType = elementsType;
    }

    public boolean isGeneric() {
        return false;
    }

    public int getTupleLength() {
        return elementsName.length;
    }

    public String getTupleName() {
        return tupleName;
    }

    public String getElementName(int number) {
        return elementsName[number-1];
    }

    public String getElementType(int number) {
        return elementsType[number-1];
    }

}
