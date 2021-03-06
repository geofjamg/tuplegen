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

import fr.jamgotchian.tuplegen.core.config.GenericTuple;
import fr.jamgotchian.tuplegen.core.config.TupleConfig;
import fr.jamgotchian.tuplegen.core.config.UserDefinedTuple;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class TupleGen {

    private static final TemplateUtil UTIL = new TemplateUtil();

    private static String getTemplateDir(TupleConfig config) {
        return "vm/" + config.getSourceLanguage().toString().toLowerCase() + "/";
    }

    private static float getSourceVersion(TupleConfig config) {
        return config.getSourceVersion() == null ? 1.6f : config.getSourceVersion();
    }

    private static String getSourceEncoding(TupleConfig config) {
        return config.getSourceEncoding() == null ? "UTF-8" : config.getSourceEncoding();
    }

    private static boolean isLatinName(GenericTuple tuple) {
        return tuple.isLatinName() == null ? Boolean.TRUE : tuple.isLatinName();
    }

    private static TupleModel getGenericTupleModel(TupleConfig config, int i, TupleGenLogger logger) {
        GenericTuple tuple = config.getGenericTuples().get(i);
        return new GenericTupleModel(getTemplateDir(config), config.getPackageName(),
                                     getSourceVersion(config), getSourceEncoding(config),
                                     tuple.getLength(), isLatinName(tuple),
                                     logger);
    }

    private static TupleModel getUserDefinedTupleModel(TupleConfig config, int i) {
        UserDefinedTuple tuple = config.getUserDefinedTuples().get(i);
        String[] elementsName = new String[tuple.getElement().size()];
        String[] elementsType = new String[tuple.getElement().size()];
        for (int j = 0; j < tuple.getElement().size(); j++) {
            elementsName[j] = tuple.getElement().get(j).getName();
            elementsType[j] = tuple.getElement().get(j).getType();
        }
        return new UserDefinedTupleModel(getTemplateDir(config), config.getPackageName(),
                                         getSourceVersion(config), getSourceEncoding(config),
                                         tuple.getName(), elementsName, elementsType);
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

    private void generate(TupleModel model, File genSrcDir, boolean readOnly,
                          TupleGenLogger logger) throws IOException {
        String tupleName = model.getTupleName();
        String fileName = UTIL.upperCaseFirstChar(tupleName) + ".java";
        String packageRelDir = model.getPackageName().replace('.', '/');
        File packageDir = new File(genSrcDir, packageRelDir);
        if (logger != null) {
            if (packageRelDir.length() > 0) {
                logger.info(packageRelDir + "/" + fileName);
            } else {
                logger.info(fileName);
            }
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
        if (readOnly && !tupleFile.setReadOnly()) {
            throw new RuntimeException("Cannot set file " + tupleFile + " read only");
        }
    }

    private void validateConfig(TupleConfig config) throws JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(TupleConfig.class);
        JAXBSource source = new JAXBSource(jc, config);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(getClass().getResource("/xsd/tupleconfig.xsd"));
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }
        });
        validator.validate(source);
    }

    public void generate(File configFile, File genSrcDir, boolean readOnly, TupleGenLogger logger)
            throws JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(TupleConfig.class);
        Unmarshaller um = jc.createUnmarshaller();
        TupleConfig config = (TupleConfig) um.unmarshal(configFile);
        generate(config, genSrcDir, readOnly, logger);
    }

    public void generate(TupleConfig config, File genSrcDir, boolean readOnly, TupleGenLogger logger)
            throws JAXBException, SAXException, IOException {
        validateConfig(config);
        for (int i = 0; i < config.getGenericTuples().size(); i++) {
            TupleModel model = getGenericTupleModel(config, i, logger);
            generate(model, genSrcDir, readOnly, logger);
        }
        for (int i = 0; i < config.getUserDefinedTuples().size(); i++) {
            TupleModel model = getUserDefinedTupleModel(config, i);
            generate(model, genSrcDir, readOnly, logger);
        }
    }

}
