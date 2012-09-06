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
                generator.generate(config, genSrcDir, logger, true);
            }

            if (configFile != null) {
                generator.generate(configFile, genSrcDir, logger);
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
