package org.jboss.test.vfs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.junit.Assume;
import org.junit.Test;

/**
 * @author Tomaz Cerar (c) 2014 Red Hat Inc.
 */
public class FileVFSTestCase {

    public void setUp() throws Exception {
        System.setProperty(VFSUtils.FORCE_CASE_SENSITIVE_KEY, "true");
    }


    public void tearDown() throws Exception {
        System.clearProperty(VFSUtils.FORCE_CASE_SENSITIVE_KEY);
    }


    /**
     * Test VirtualFile.exists for vfsfile based urls.
     *
     * @throws Exception
     */
    @Test
    public void testMountRealFileExists() throws Exception {
        Assume.assumeTrue("windows have case insensitive FS", File.separatorChar == '/');
        File tmpRoot = File.createTempFile("vfs", ".real");
        tmpRoot.delete();
        tmpRoot.mkdir();
        File tmp = File.createTempFile("testFileExists", null, tmpRoot);
        System.out.println("+++ testFileExists, tmp=" + tmp.getCanonicalPath());

        VFS.mountReal(tmpRoot, VFS.getChild("real"));
        VirtualFile testdir = VFS.getChild("/real/");
        VirtualFile tmpVF = testdir.getChild(tmp.getName());
        VirtualFile tmpVFNotExist = testdir.getChild(tmpVF.getName().toUpperCase());
        assertTrue(tmpVF.getPathName() + ".exists()", tmpVF.exists());
        assertFalse("!" + tmpVFNotExist.getPathName() + ".exists()", tmpVFNotExist.exists());
        assertTrue("tmp.delete()", tmpVF.delete());
        assertFalse(tmpVF.getPathName() + ".exists()", tmpVF.exists());
        assertTrue(tmpRoot + ".delete()", tmpRoot.delete());
    }
}
