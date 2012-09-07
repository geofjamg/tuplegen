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

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class Main {

    private static class CmdLineLogger implements TupleGenLogger {

        public void info(String msg) {
            System.out.println(msg);
        }

        public void warning(String msg) {
            System.out.println(msg);
        }

    }

    private static final TupleGenLogger LOGGER = new CmdLineLogger();

    private static final Options OPTIONS = new Options();

    static {
        OPTIONS.addOption(OptionBuilder.withLongOpt("generated-dir")
                                       .hasArg()
                                       .withArgName("DIR")
                                       .withDescription("generated files will go into this directory")
                                       .create("d"));
        OPTIONS.addOption(OptionBuilder.withLongOpt("config-file")
                                       .hasArg()
                                       .withArgName("FILE")
                                       .withDescription("tuples configuration file")
                                       .create("c"));
        OPTIONS.addOption(OptionBuilder.withLongOpt("help")
                                       .withDescription("display this help message")
                                       .create("h"));
    }

    private static void usage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("tuplegen", OPTIONS);
        System.exit(-1);
    }

    public static void main(String[] args) {
        CommandLineParser parser = new PosixParser();

        try {
            CommandLine line = parser.parse(OPTIONS, args);
            if (line.hasOption("h") || !line.hasOption("c") || !line.hasOption("d")) {
                usage();
            }
            File genSrcDir = new File(line.getOptionValue("d"));
            if (!genSrcDir.exists()) {
                throw new IllegalArgumentException(genSrcDir + " does not exit");
            }
            if (!genSrcDir.isDirectory()) {
                throw new IllegalArgumentException(genSrcDir + " should be a directory");
            }
            File cfgFile = new File(line.getOptionValue("c"));
            if (!cfgFile.exists()) {
                throw new IllegalArgumentException(cfgFile + " does not exist");
            }
            TupleGen generator = new TupleGen();
            generator.generate(cfgFile, genSrcDir, false, LOGGER);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
