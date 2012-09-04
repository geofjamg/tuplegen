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
import fr.jamgotchian.tuplegen.core.TupleGenParameters;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

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
     * Generic tuples defintion.
     * @parameter
     */
    private List<GenericTuple> genericTuples;

    /**
     * User defined tuples defintion.
     * @parameter
     */
    private List<UserDefinedTuple> userDefinedTuples;

    private final MavenTupleGenLogger logger = new MavenTupleGenLogger();

    private int getTupleCount() {
        int count = 0;
        if (genericTuples != null) {
            count += genericTuples.size();
        }
        if (userDefinedTuples != null) {
            count += userDefinedTuples.size();
        }
        return count;
    }

    public void execute() throws MojoExecutionException {
        if (getTupleCount() == 0) {
            return;
        }
        if (packageName == null) {
            throw new MojoExecutionException("packageName is not set");
        }

        File generatedSources = new File(project.getBasedir(), "target/generated-sources/tuplegen");
        project.addCompileSourceRoot(generatedSources.getAbsolutePath());

        logger.info("generating...");

        try {
            TupleGen generator = new TupleGen();

            if (genericTuples != null) {
                for (GenericTuple tuple : genericTuples) {
                    if (tuple.getLength() == null) {
                        throw new MojoExecutionException("Generic tuple length is not set");
                    }
                    if (tuple.getLength() <= 0) {
                        throw new MojoExecutionException("Generic tuple length should be greater than zero");
                    }
                    TupleGenParameters parameters = new TupleGenParameters();
                    parameters.setPackageName(packageName);
                    if (tuple.isLatinName() != null) {
                        parameters.setLatinName(tuple.isLatinName());
                    }
                    parameters.setTupleLength(tuple.getLength());
                    if (sourceVersion != null) {
                        parameters.setSourceVersion(sourceVersion);
                    }
                    if (sourceEncoding != null) {
                        parameters.setSourceEncoding(sourceEncoding);
                    }
                    generator.generate(parameters, generatedSources, logger);
                }
            }

            if (userDefinedTuples != null) {
                for (UserDefinedTuple tuple : userDefinedTuples) {
                    if (tuple.getName() == null) {
                        throw new MojoExecutionException("User defined tuple name is not set");
                    }
                    if (tuple.getElements() == null || tuple.getElements().isEmpty()) {
                        throw new MojoExecutionException("Empty user defined tuple");
                    }
                    for (Element elt : tuple.getElements()) {
                        if (elt.getName() == null) {
                            throw new MojoExecutionException("Element name is not set");
                        }
                        if (elt.getType() == null) {
                            throw new MojoExecutionException("Element type is not set");
                        }
                        // TODO
                    }
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.toString(), e);
        }
    }
}
