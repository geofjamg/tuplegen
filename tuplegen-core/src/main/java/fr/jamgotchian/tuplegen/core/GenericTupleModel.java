/*
 * Copyright 2012 Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
            throw new IllegalArgumentException("Tuple length must be > 0");
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
