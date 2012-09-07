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
