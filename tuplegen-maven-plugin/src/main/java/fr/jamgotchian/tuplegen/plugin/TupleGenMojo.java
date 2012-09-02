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

        public void log(String msg) {
            getLog().info(msg);
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
     * Tuple defintion list.
     * @parameter
     */
    private List<Tuple> tuples;

    private final MavenTupleGenLogger logger = new MavenTupleGenLogger();

    public void execute() throws MojoExecutionException {
        if (packageName == null) {
            throw new MojoExecutionException("packageName parameter is not set");
        }

        File generatedSources = new File(project.getBasedir(), "target/generated-sources/tuplegen");
        project.addCompileSourceRoot(generatedSources.getAbsolutePath());

        try {
            TupleGen generator = new TupleGen();
            for (Tuple tuple : tuples) {
                if (tuple.getLength() <= 0) {
                    throw new MojoExecutionException("tupleLength should be greater than zero");
                }
                TupleGenParameters parameters = new TupleGenParameters();
                parameters.setPackageName(packageName);
                parameters.setTupleKind(tuple.getKind());
                parameters.setTupleLength(tuple.getLength());
                if (sourceVersion != null) {
                    parameters.setSourceVersion(sourceVersion);
                }
                generator.generate(parameters, generatedSources, logger);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.toString(), e);
        }
    }
}
