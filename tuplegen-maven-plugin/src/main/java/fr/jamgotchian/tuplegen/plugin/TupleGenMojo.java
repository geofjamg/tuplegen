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
package fr.jamgotchian.tuplegen.plugin;

import fr.jamgotchian.tuplegen.core.TupleGen;
import fr.jamgotchian.tuplegen.core.TupleGenLogger;
import fr.jamgotchian.tuplegen.core.config.GenericTuple;
import fr.jamgotchian.tuplegen.core.config.ObjectFactory;
import fr.jamgotchian.tuplegen.core.config.SourceLanguage;
import fr.jamgotchian.tuplegen.core.config.TupleConfig;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.xml.sax.SAXException;

/**
 * Generate tuples.
 * @goal generate
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class TupleGenMojo extends AbstractMojo {

    private class MavenTupleGenLogger implements TupleGenLogger {

        public void info(String msg) {
            getLog().info(msg);
        }

        public void warning(String msg) {
            getLog().warn(msg);
        }

    }

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The name of the generated source package.
     * @parameter
     */
    private String packageName;

    /**
     * Generated source version.
     * @parameter
     */
    private Float sourceVersion;

    /**
     * Generated source encoding.
     * @parameter
     */
    private String sourceEncoding;

    /**
     * Generic tuples length.
     * @parameter
     */
    private String lengths;

    /**
     * Tuples config file.
     * @parameter
     */
    private File configFile;

    private final MavenTupleGenLogger logger = new MavenTupleGenLogger();

    public void execute() throws MojoExecutionException {

        File genSrcDir = new File(project.getBasedir(), "target/generated-sources/tuplegen");
        project.addCompileSourceRoot(genSrcDir.getAbsolutePath());

        logger.info("generating...");

        try {
            TupleGen generator = new TupleGen();

            if (lengths != null && lengths.length() > 0) {
                if (packageName == null) {
                    throw new MojoExecutionException("packageName is not set");
                }
                ObjectFactory factory = new ObjectFactory();
                TupleConfig config = factory.createTupleConfig();
                config.setPackageName(packageName);
                config.setSourceLanguage(SourceLanguage.JAVA);

                if (sourceVersion != null) {
                    config.setSourceVersion(sourceVersion);
                }
                if (sourceEncoding != null) {
                    config.setSourceEncoding(sourceEncoding);
                }
                for (String token : lengths.split(",")) {
                    int length = Integer.valueOf(token);
                    if (length <= 0) {
                        throw new MojoExecutionException("Generic tuple length should be greater than zero");
                    }
                    GenericTuple tuple = factory.createGenericTuple();
                    tuple.setLength(length);
                    config.getGenericTuples().add(tuple);
                }
                generator.generate(config, genSrcDir, true, logger);
            }

            if (configFile != null) {
                generator.generate(configFile, genSrcDir, true, logger);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.toString(), e);
        } catch (JAXBException e) {
            throw new MojoExecutionException(e.toString(), e);
        } catch (SAXException e) {
            throw new MojoExecutionException(e.toString(), e);
        }
    }
}
