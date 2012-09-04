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

    private static final TemplateUtil UTIL = new TemplateUtil();

    private static TupleModel getTupleModel(TupleGenParameters params, TupleGenLogger logger) {
        String templateDir = "vm/" + params.getSourceLanguage().toString().toLowerCase() + "/";
        return new GenericTupleModel(templateDir, params.getPackageName(),
                                     params.getSourceVersion(), params.getSourceEncoding(),
                                     params.getTupleLength(), params.isLatinName(),
                                     logger);
//        return new UserDefinedTupleModel(templateDir, params.getPackageName(),
//                                         params.getSourceVersion(), params.getSourceEncoding(),
//                                         "result", new String[] {"logs", "returnCode"},
//                                         new String[] {"String", "Integer"});
    }

    private final VelocityEngine ve;

    public TupleGen() {
        ve = new VelocityEngine();
        ve.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();
    }

    private void generate(TupleModel model, Writer writer) {
        Template t = ve.getTemplate(model.getTemplateDir() + "tuple.vm", model.getSourceEncoding());
        VelocityContext context = new VelocityContext();
        context.put("model", model);
        context.put("util", UTIL);
        t.merge(context, writer);
    }

    public void generate(TupleGenParameters params, Writer writer) throws IOException {
        TupleModel model = getTupleModel(params, null);
        generate(model, writer);
    }

    private void generate(TupleModel model, File genSrcDir, TupleGenLogger logger) throws IOException {
        String tupleName = model.getTupleName();
        String fileName = UTIL.upperCaseFirstChar(tupleName) + ".java";
        String packageRelDir = model.getPackageName().replace('.', '/');
        File packageDir = new File(genSrcDir, packageRelDir);
        if (logger != null) {
            logger.info(packageRelDir + "/" + fileName);
        }
        // ensure package directory exits
        if (!packageDir.exists()) {
            if (!packageDir.mkdirs()) {
                throw new RuntimeException("Fail to create directory " + packageDir);
            }
        }
        File tupleFile = new File(packageDir, fileName);
        Writer writer = new FileWriter(tupleFile);
        try {
            generate(model, writer);
        } finally {
            writer.close();
        }
    }

    public void generate(TupleGenParameters params, File genSrcDir, TupleGenLogger logger) throws IOException {
        TupleModel model = getTupleModel(params, logger);
        generate(model, genSrcDir, logger);
    }
}
