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
public abstract class AbstractTupleModel implements TupleModel {

    private final String templateDir;

    private final String packageName;

    private final float sourceVersion;

    private final String sourceEncoding;

    public AbstractTupleModel(String templateDir, String packageName, float sourceVersion, String sourceEncoding) {
        this.templateDir = templateDir;
        this.packageName = packageName;
        this.sourceVersion = sourceVersion;
        this.sourceEncoding = sourceEncoding;
    }

    @Override
    public String getTemplateDir() {
        return templateDir;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public float getSourceVersion() {
        return sourceVersion;
    }

    @Override
    public String getSourceEncoding() {
        return sourceEncoding;
    }

}
