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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class TupleGen {

    private static final TupleModel TUPLE_MODEL = new TupleModel();

    private final VelocityEngine ve;

    public TupleGen() {
        ve = new VelocityEngine();
        ve.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();
    }

    public void generate(int tupleLength, String packageName, float sourceVersion, Writer writer) {
        Template t = ve.getTemplate("vm/java/tuple.vm");
        VelocityContext context1 = new VelocityContext();
        context1.put("tupleLength", tupleLength);
        context1.put("packageName", packageName);
        context1.put("sourceVersion", sourceVersion);
        context1.put("tupleModel", TUPLE_MODEL);
        t.merge(context1, writer);
    }

    public void generate(int tupleLength, String packageName, float sourceVersion,
                         File generatedSources, TupleGenLogger logger) throws IOException {
        String tupleName = TUPLE_MODEL.getTupleName(tupleLength);
        String fileName = tupleName.substring(0, 1).toUpperCase() + tupleName.substring(1, tupleName.length()) + ".java";
        String packageRelDir = packageName.replace('.', '/');
        File packageDir = new File(generatedSources, packageRelDir);
        logger.log("generating...");
        logger.log(packageRelDir + "/" + fileName);
        // ensure package directory exits
        packageDir.mkdirs();
        File tupleFile = new File(packageDir, fileName);
        Writer writer = new FileWriter(tupleFile);
        try {
            generate(tupleLength, packageName, sourceVersion, writer);
        } finally {
            writer.close();
        }
    }
}
