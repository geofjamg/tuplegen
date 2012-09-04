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
public class GenericTupleModel extends AbstractTupleModel {

    private static final String[] TUPLE_LATIN_NAMES = {
        "single",
        "pair",
        "triple",
        "quadruple",
        "quintuple",
        "sextuple",
        "septuple",
        "octuple",
        "nonuple",
        "decuple",
        "undecuple",
        "duodecuple",
        "tredecuple",
        "quattuordecuple",
        "quindecuple",
        "sexdecuple",
        "septendecuple",
        "octodecuple",
        "novemdecuple",
        "vigenuple"};

    private static final String[] ELEMENT_LATIN_NAMES = {
        "first",
        "second",
        "third",
        "fourth",
        "fifth",
        "sixth",
        "seventh",
        "eighth",
        "ninth",
        "tenth",
        "eleventh",
        "twelfth",
        "thirteenth",
        "fourteenth",
        "fifteenth",
        "sixteenth",
        "seventeenth",
        "eighteenth",
        "nineteenth",
        "twentieth"};

    private final int tupleLength;

    private final boolean latinName;

    private final TupleGenLogger logger;

    public GenericTupleModel(String templateDir, String packageName, float sourceVersion,
                             String sourceEncoding, int tupleLength, boolean latinName,
                             TupleGenLogger logger) {
        super(templateDir, packageName, sourceVersion, sourceEncoding);
        if (tupleLength < 1) {
            throw new IllegalArgumentException("Tuple length must be < 1");
        }
        if (logger != null) {
            if (tupleLength > TUPLE_LATIN_NAMES.length) {
                logger.warning("Don't know what is the latin name of a " + tupleLength + "-tuple");
            }
        }
        this.tupleLength = tupleLength;
        this.latinName = latinName;
        this.logger = logger;
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    public int getTupleLength() {
        return tupleLength;
    }

    @Override
    public String getTupleName() {
        if (latinName && tupleLength <= TUPLE_LATIN_NAMES.length) {
            return TUPLE_LATIN_NAMES[tupleLength-1];
        }
        return "tuple" + tupleLength;
    }

    @Override
    public String getElementName(int ordinal) {
        if (latinName && ordinal <= ELEMENT_LATIN_NAMES.length) {
            return ELEMENT_LATIN_NAMES[ordinal-1];
        }
        return "elt" + ordinal;
    }

    @Override
    public String getElementType(int number) {
        return "T" + number;
    }

}
