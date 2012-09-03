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

    private final int tupleLength;

    private final boolean latinName;

    public GenericTupleModel(String templateDir, String packageName, float sourceVersion,
                             String sourceEncoding, int tupleLength, boolean latinName) {
        super(templateDir, packageName, sourceVersion, sourceEncoding);
        this.tupleLength = tupleLength;
        this.latinName = latinName;
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    public int getTupleLength() {
        return tupleLength;
    }

    @Override
    public String getTupleName(int length) {
        if (latinName) {
            switch (length) {
                case 1: return "single";
                case 2: return "pair";
                case 3: return "triple";
                case 4: return "quadruple";
                case 5: return "quintuple";
                case 6: return "sextuple";
                case 7: return "septuple";
                case 8: return "octuple";
                case 9: return "nonuple";
                case 10: return "decuple";
                case 11: return "undecuple";
                case 12: return "duodecuple";
                case 13: return "tredecuple";
                case 14: return "quattuordecuple";
                case 15: return "quindecuple";
                case 16: return "sexdecuple";
                case 17: return "septendecuple";
                case 18: return "octodecuple";
                case 19: return "novemdecuple";
                case 20: return "vigenuple";
                default: throw new RuntimeException("Don't know how to name a "
                        + length + "-tuple");
            }
        } else {
            return "tuple" + length;
        }
    }

    @Override
    public String getElementName(int ordinal) {
        if (latinName) {
            switch (ordinal) {
                case 1: return "first";
                case 2: return "second";
                case 3: return "third";
                case 4: return "fourth";
                case 5: return "fifth";
                case 6: return "sixth";
                case 7: return "seventh";
                case 8: return "eighth";
                case 9: return "ninth";
                case 10: return "tenth";
                case 11: return "eleventh";
                case 12: return "twelfth";
                case 13: return "thirteenth";
                case 14: return "fourteenth";
                case 15: return "fifteenth";
                case 16: return "sixteenth";
                case 17: return "seventeenth";
                case 18: return "eighteenth";
                case 19: return "nineteenth";
                case 20: return "twentieth";
                default: throw new RuntimeException("Don't know how to name the ordinal number"
                        + ordinal);
            }
        } else {
            return "elt" + ordinal;
        }
    }

    @Override
    public String getElementType(int number) {
        return "T" + number;
    }

}
