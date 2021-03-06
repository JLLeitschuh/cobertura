package net.sourceforge.cobertura.bugs;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * This integration test is for verifying that the version number
 * gets into the manfiest.mf file. There is a bug where if there
 * is no manifest.mf file then it will produce a null version. This
 * is to verify that when a "mvn package" command is executed that
 * there will not be an issue.
 * 
 * ON FAILURES: If this fails in eclipse, just ignore it.
 * It is only suppose to be executed when mvn integration-test
 * is performed, because the .jar file is not created until after
 * the regular mvn test is performed.
 * 
 * @author christ66
 *
 */
public class GithubIssue37IT {
	@Test
	public void testVersionCorrect() throws IOException {
		Collection<File> allJars = FileUtils.listFiles(new File("target"),
				new String[]{"jar"}, false);

		File loc = null;

		for (File file : allJars) {
			if (!file.getName().contains("sources")
					&& !file.getName().contains("javadoc")) {
				loc = file;
			}
		}

		assertNotNull("Could not locate the correct jar", loc);

		JarFile jar = null;
		try {
			jar = new JarFile(loc);
			Manifest mf = jar.getManifest();
			assertNotNull(
					"Failed to find Implementation Version in the mainAttribute",
					mf.getMainAttributes().getValue("Implementation-Version"));
		} finally {
			jar.close();
		}
	}
}
