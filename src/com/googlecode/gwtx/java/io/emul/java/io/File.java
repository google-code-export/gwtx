/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * This file is based on code from the Apache Harmony Project.
 * http://svn.apache.org/repos/asf/harmony/enhanced/classlib/trunk/modules/luni/src/main/java/java/io/File.java
 */

/*
 * TODO some File methods may be implemented using HTTP headers & status code : exists(), lastModified()...
 */

package java.io;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * File is a class which represents a file name or directory. The file may be
 * absolute relative to the root directory of the file system or relative to the
 * current directory in which the program is running.
 * <p>
 * This class provides methods for querying/changing information about the file
 * and also directory listing capabilities if the File represents a directory.
 * <p>
 * When manipulating file paths, the static fields of this class may be used to
 * determine the platform specific separators.
 *
 * @see java.io.Serializable
 * @see java.lang.Comparable
 */
public class File implements Serializable, Comparable<File> {

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private String path;

    transient byte[] properPath;

    /**
     * System dependent file separator character.
     */
    public static final char separatorChar;

    /**
     * System dependent file separator String. The initial value of this field
     * is the System property "file.separator".
     */
    public static final String separator;

    /**
     * System dependent path separator character.
     */
    public static final char pathSeparatorChar;

    /**
     * System dependent path separator String. The initial value of this field
     * is the System property "path.separator".
     */
    public static final String pathSeparator;

    /* Temp file counter */
    private static int counter;

    private static boolean caseSensitive;

    private static native void oneTimeInitialization();

    static {
        separatorChar = '/';
        pathSeparatorChar = '/';
        separator = "/";
        pathSeparator = new String(new char[] { pathSeparatorChar }, 0, 1);
        caseSensitive = true;
    }

    /**
     * Constructs a new File using the specified directory and name.
     *
     * @param dir
     *            the directory for the file name
     * @param name
     *            the file name to be contained in the dir
     */
    public File(File dir, String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (dir == null) {
            this.path = fixSlashes(name);
        } else {
            this.path = calculatePath(dir.getPath(), name);
        }
    }

    /**
     * Constructs a new File using the specified path.
     *
     * @param path
     *            the path to be used for the file
     */
    public File(String path) {
        // path == null check & NullPointerException thrown by fixSlashes
        this.path = fixSlashes(path);
    }

    /**
     * Constructs a new File using the specified directory and name placing a
     * path separator between the two.
     *
     * @param dirPath
     *            the directory for the file name
     * @param name
     *            the file name to be contained in the dir
     */
    public File(String dirPath, String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (dirPath == null) {
            this.path = fixSlashes(name);
        } else {
            this.path = calculatePath(dirPath, name);
        }
    }

    /**
     * Constructs a new File using the path of the specified URI
     *
     * <code>uri</code> needs to be an absolute and hierarchical
     * <code>URI </code> with file scheme, and non-empty path component, but
     * with undefined authority, query or fragment components.
     *
     * @param uri
     *            the URI instance which will be used to construct this file
     *
     * @throws IllegalArgumentException
     *             if <code>uri</code> does not comply with the conditions
     *             above.
     *
     * @see #toURI
     * @see java.net.URI
     */
    public File(URI uri) {
        // check pre-conditions
        checkURI(uri);
        this.path = fixSlashes(uri.getPath());
    }

    private String calculatePath(String dirPath, String name) {
        dirPath = fixSlashes(dirPath);
        if (!name.equals(EMPTY_STRING) || dirPath.equals(EMPTY_STRING)) {
            // Remove all the proceeding separator chars from name
            name = fixSlashes(name);

            int separatorIndex = 0;
            while ((separatorIndex < name.length())
                    && (name.charAt(separatorIndex) == separatorChar)) {
                separatorIndex++;
            }
            if (separatorIndex > 0) {
                name = name.substring(separatorIndex, name.length());
            }

            // Ensure there is a separator char between dirPath and name
            if (dirPath.length() > 0
                    && (dirPath.charAt(dirPath.length() - 1) == separatorChar)) {
                return dirPath + name;
            }
            return dirPath + separatorChar + name;
        }

        return dirPath;
    }

    @SuppressWarnings("nls")
    private void checkURI(URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException(uri.toString());
        } else if (!uri.getRawSchemeSpecificPart().startsWith("/")) {
            throw new IllegalArgumentException(uri.toString());
        }

        String temp = uri.getScheme();
        if (temp == null || !temp.equals("file")) {
            throw new IllegalArgumentException(uri.toString());
        }

        temp = uri.getRawPath();
        if (temp == null || temp.length() == 0) {
            throw new IllegalArgumentException(uri.toString());
        }

        if (uri.getRawAuthority() != null) {
            throw new IllegalArgumentException(uri.toString());
        }

        if (uri.getRawQuery() != null) {
            throw new IllegalArgumentException(uri.toString());
        }

        if (uri.getRawFragment() != null) {
            throw new IllegalArgumentException(uri.toString());
        }
    }

    /**
     * The purpose of this method is to take a path and fix the slashes up. This
     * includes changing them all to the current platforms fileSeparator and
     * removing duplicates.
     */
    private String fixSlashes(String origPath) {
        int uncIndex = 1;
        int length = origPath.length(), newLength = 0;
        if (separatorChar == '/') {
            uncIndex = 0;
        } else if (length > 2 && origPath.charAt(1) == ':') {
            uncIndex = 2;
        }

        boolean foundSlash = false;
        char newPath[] = origPath.toCharArray();
        for (int i = 0; i < length; i++) {
            char pathChar = newPath[i];
            if (pathChar == '\\' || pathChar == '/') {
                /* UNC Name requires 2 leading slashes */
                if ((foundSlash && i == uncIndex) || !foundSlash) {
                    newPath[newLength++] = separatorChar;
                    foundSlash = true;
                }
            } else {
                // check for leading slashes before a drive
                if (pathChar == ':'
                        && uncIndex > 0
                        && (newLength == 2 || (newLength == 3 && newPath[1] == separatorChar))
                        && newPath[0] == separatorChar) {
                    newPath[0] = newPath[newLength - 1];
                    newLength = 1;
                    // allow trailing slash after drive letter
                    uncIndex = 2;
                }
                newPath[newLength++] = pathChar;
                foundSlash = false;
            }
        }
        // remove trailing slash
        if (foundSlash
                && (newLength > (uncIndex + 1) || (newLength == 2 && newPath[0] != separatorChar))) {
            newLength--;
        }
        String tempPath = new String(newPath, 0, newLength);
        // If it's the same keep it identical for SecurityManager purposes
        if (!tempPath.equals(origPath)) {
            return tempPath;
        }
        return origPath;
    }

    /**
     * Answers the relative sort ordering of paths for the receiver and given
     * argument. The ordering is platform dependent.
     *
     * @param another
     *            a File to compare the receiver to
     * @return an int determined by comparing the two paths. The meaning is
     *         described in the Comparable interface.
     * @see Comparable
     */
    public int compareTo(File another) {
        if (caseSensitive) {
            return this.getPath().compareTo(another.getPath());
        }
        return this.getPath().compareToIgnoreCase(another.getPath());
    }

    /**
     * Deletes the file specified by this File. Directories must be empty before
     * they will be deleted.
     *
     * @return <code>true</code> if this File was deleted, <code>false</code>
     *         otherwise.
     *
     * @see java.lang.SecurityManager#checkDelete
     */
    public boolean delete() {
        return false;
    }

    /**
     * When the virtual machine terminates, any abstract files which have been
     * sent <code>deleteOnExit()</code> will be deleted. This will only happen
     * when the virtual machine terminates normally as described by the Java
     * Language Specification section 12.9.
     *
     */
    public void deleteOnExit() {
        // nothing
    }

    /**
     * Compares the argument <code>obj</code> to the receiver, and answers
     * <code>true</code> if they represent the <em>same</em> object using a
     * path specific comparison.
     *
     * @param obj
     *            the Object to compare with this Object
     * @return <code>true</code> if the object is the same as this object,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof File)) {
            return false;
        }
        if (!caseSensitive) {
            return path.equalsIgnoreCase(((File) obj).getPath());
        }
        return path.equals(((File) obj).getPath());
    }

    /**
     * Answers the filename (not directory) of this File.
     *
     * @return the filename or empty string
     */
    public String getName() {
        int separatorIndex = path.lastIndexOf(separator);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1,
                path.length());
    }

    /**
     * Answers the pathname of the parent of this File. This is the path up to
     * but not including the last name. <code>null</code> is returned when
     * there is no parent.
     *
     * @return the parent name or <code>null</code>
     */
    public String getParent() {
        int length = path.length(), firstInPath = 0;
        if (separatorChar == '\\' && length > 2 && path.charAt(1) == ':') {
            firstInPath = 2;
        }
        int index = path.lastIndexOf(separatorChar);
        if (index == -1 && firstInPath > 0) {
            index = 2;
        }
        if (index == -1 || path.charAt(length - 1) == separatorChar) {
            return null;
        }
        if (path.indexOf(separatorChar) == index
                && path.charAt(firstInPath) == separatorChar) {
            return path.substring(0, index + 1);
        }
        return path.substring(0, index);
    }

    /**
     * Answers a new File made from the pathname of the parent of this File.
     * This is the path up to but not including the last name. <code>null</code>
     * is returned when there is no parent.
     *
     * @return a new File representing parent or <code>null</code>
     */
    public File getParentFile() {
        String tempParent = getParent();
        if (tempParent == null) {
            return null;
        }
        return new File(tempParent);
    }

    /**
     * Answers the file path of this File.
     *
     * @return the file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Answers an integer hash code for the receiver. Any two objects which
     * answer <code>true</code> when passed to <code>equals</code> must
     * answer the same value for this method.
     *
     * @return the receiver's hash
     *
     * @see #equals
     */
    @Override
    public int hashCode() {
        if (caseSensitive) {
            return path.hashCode() ^ 1234321;
        }
        return path.toLowerCase().hashCode() ^ 1234321;
    }

    /**
     * Answers if this File is an absolute pathname. Whether a pathname is
     * absolute is platform specific. On UNIX it is if the path starts with the
     * character '/', on Windows it is absolute if either it starts with '\',
     * '/', '\\' (to represent a file server), or a letter followed by a colon.
     *
     * @return <code>true</code> if this File is absolute, <code>false</code>
     *         otherwise.
     *
     * @see #getPath
     */
    public boolean isAbsolute() {
        return path.startsWith("http://") || path.startsWith("https://");
    }

    /**
     * Answers if this File represents a <em>directory</em> on the underlying
     * file system.
     *
     * @return <code>true</code> if this File is a directory,
     *         <code>false</code> otherwise.
     *
     * @see #getPath
     * @see java.lang.SecurityManager#checkRead(FileDescriptor)
     */
    public boolean isDirectory() {
        if (path.length() == 0) {
            return false;
        }
        return path.lastIndexOf("/") == path.length()-1;
    }

    /**
     * Answers if this File represents a <em>file</em> on the underlying file
     * system.
     *
     * @return <code>true</code> if this File is a file, <code>false</code>
     *         otherwise.
     *
     * @see #getPath
     * @see java.lang.SecurityManager#checkRead(FileDescriptor)
     */
    public boolean isFile() {
        if (path.length() == 0) {
            return false;
        }
        return !isDirectory();
    }

    private native boolean isFileImpl(byte[] filePath);

    /**
     * Returns whether or not this file is a hidden file as defined by the
     * operating system.
     *
     * @return <code>true</code> if the file is hidden, <code>false</code>
     *         otherwise.
     */
    public boolean isHidden() {
        return false;
    }

    /**
     * Answers the time this File was last modified.
     *
     * @return the time this File was last modified.
     *
     * @see #getPath
     * @see java.lang.SecurityManager#checkRead(FileDescriptor)
     */
    public long lastModified() {
        // TODO? make it check HTTP lastModified header?
        throw new UnsupportedOperationException("lastModified not supported");
    }

    /**
     * Sets the time this File was last modified.
     *
     * @param time
     *            The time to set the file as last modified.
     * @return the time this File was last modified.
     *
     * @see java.lang.SecurityManager#checkWrite(FileDescriptor)
     */
    public boolean setLastModified(long time) {
        throw new UnsupportedOperationException("setLastModified not supported");
    }

    /**
     * Marks this file or directory to be read-only as defined by the operating
     * system.
     *
     * @return <code>true</code> if the operation was a success,
     *         <code>false</code> otherwise
     */
    public boolean setReadOnly() {
        return false;
    }

    /**
     * Renames this File to the name represented by the File <code>dest</code>.
     * This works for both normal files and directories.
     *
     * @param dest
     *            the File containing the new name.
     * @return <code>true</code> if the File was renamed, <code>false</code>
     *         otherwise.
     *
     * @see #getPath
     * @see java.lang.SecurityManager#checkRead(FileDescriptor)
     * @see java.lang.SecurityManager#checkWrite(FileDescriptor)
     */
    public boolean renameTo(java.io.File dest) {
        return false;
    }

    /**
     * Answers a string containing a concise, human-readable description of the
     * receiver.
     *
     * @return a printable representation for the receiver.
     */
    @Override
    public String toString() {
        return path;
    }

}
