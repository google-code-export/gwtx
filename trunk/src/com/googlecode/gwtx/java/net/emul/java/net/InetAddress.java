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
 * http://svn.apache.org/repos/asf/harmony/enhanced/classlib/trunk/modules/luni/src/main/java/java/net/InetAddress.java
 */

package java.net;

import java.io.Serializable;

/**
 * The Internet Protocol (IP) address class. This class encapsulates an IP
 * address and provides name and reverse name resolution functions. The address
 * is stored in network order, but as a signed (rather than unsigned) integer.
 */
public class InetAddress extends Object implements Serializable {

    final static byte[] any_bytes = { 0, 0, 0, 0 };

    final static byte[] localhost_bytes = { 127, 0, 0, 1 };

    private static final String ERRMSG_CONNECTION_REFUSED = "Connection refused"; //$NON-NLS-1$

    String hostName;

    private static class WaitReachable {
    }

    private transient Object waitReachable = new WaitReachable();

    private boolean reached;

    private int addrCount;

    int family = 2;

    byte[] ipaddress;

    // Fill in the JNI id caches
    private static native void oneTimeInitialization(boolean supportsIPv6);

    static {
        oneTimeInitialization(true);
    }

    /**
     * Constructs an InetAddress.
     */
    InetAddress() {
        super();
    }

    /**
     * Constructs an InetAddress, representing the <code>address</code> and
     * <code>hostName</code>.
     *
     * @param address
     *            network address
     */
    InetAddress(byte[] address) {
        super();
        this.ipaddress = address;
    }

    /**
     * Constructs an InetAddress, representing the <code>address</code> and
     * <code>hostName</code>.
     *
     * @param address
     *            network address
     */
    InetAddress(byte[] address, String hostName) {
        super();
        this.ipaddress = address;
        this.hostName = hostName;
    }

    /**
     * Compares this <code>InetAddress</code> against the specified object.
     *
     * @param obj
     *            the object to be tested for equality
     * @return boolean true, if the objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        // now check if their byte arrays match...
        byte[] objIPaddress = ((InetAddress) obj).ipaddress;
        for (int i = 0; i < objIPaddress.length; i++) {
            if (objIPaddress[i] != this.ipaddress[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the IP address of this <code>InetAddress</code> as an array.
     * The elements are in network order (the highest order address byte is in
     * the zero-th element).
     *
     * @return byte[] the address as a byte array
     */
    public byte[] getAddress() {
        byte[] clone = new byte[ipaddress.length];
        for ( int i = 0; i < ipaddress.length; i++ )
        {
            clone[i] = ipaddress[i];
        }
        return clone;
    }

    /**
     * Answer a hashcode for this IP address.
     *
     * @return int the hashcode
     */
    @Override
    public int hashCode() {
        return bytesToInt(ipaddress, 0);
    }

    /**
     * Answer true if the InetAddress is an IP multicast address.
     *
     * @return boolean true, if the address is in the multicast group
     */
    public boolean isMulticastAddress() {
        return ((ipaddress[0] & 255) >>> 4) == 0xE;
    }

    /**
     * Answers a string containing a concise, human-readable description of the
     * address.
     *
     * @return String the description, as host/address
     */
    @Override
    public String toString() {
        return (hostName == null ? "" : hostName) + "/" + ipaddress.toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Answer true if the address is a loop back address. Valid IPv4 loopback
     * addresses are 127.d.d.d Valid IPv6 loopback address is ::1
     *
     * @return boolean
     */
    public boolean isLoopbackAddress() {
        return false;
    }

    /**
     * Answers true if the address is a link local address.
     *
     * Valid IPv6 link local addresses are FE80::0 through to
     * FEBF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF
     *
     * There are no valid IPv4 link local addresses.
     *
     * @return boolean
     */
    public boolean isLinkLocalAddress() {
        return false;
    }

    /**
     * Answers true if the address is a site local address.
     *
     * Valid IPv6 link local addresses are FEC0::0 through to
     * FEFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF
     *
     * There are no valid IPv4 site local addresses.
     *
     * @return boolean
     */
    public boolean isSiteLocalAddress() {
        return false;
    }

    /**
     * Answers true if the address is a global multicast address.
     *
     * Valid IPv6 link global multicast addresses are FFxE:/112 where x is a set
     * of flags, and the additional 112 bits make up the global multicast
     * address space
     *
     * Valid IPv4 global multicast addresses are between: 224.0.1.0 to
     * 238.255.255.255
     *
     * @return boolean
     */
    public boolean isMCGlobal() {
        return false;
    }

    /**
     * Answers true if the address is a node local multicast address.
     *
     * Valid IPv6 node local multicast addresses are FFx1:/112 where x is a set
     * of flags, and the additional 112 bits make up the node local multicast
     * address space
     *
     * There are no valid IPv4 node local multicast addresses.
     *
     * @return boolean
     */
    public boolean isMCNodeLocal() {
        return false;
    }

    /**
     * Answers true if the address is a link local multicast address.
     *
     * Valid IPv6 link local multicast addresses are FFx2:/112 where x is a set
     * of flags, and the additional 112 bits make up the node local multicast
     * address space
     *
     * Valid IPv4 link-local addresses are between: 224.0.0.0 to 224.0.0.255
     *
     * @return boolean
     */
    public boolean isMCLinkLocal() {
        return false;
    }

    /**
     * Answers true if the address is a site local multicast address.
     *
     * Valid IPv6 site local multicast addresses are FFx5:/112 where x is a set
     * of flags, and the additional 112 bits make up the node local multicast
     * address space
     *
     * Valid IPv4 site-local addresses are between: 239.252.0.0 to
     * 239.255.255.255
     *
     * @return boolean
     */
    public boolean isMCSiteLocal() {
        return false;
    }

    /**
     * Answers true if the address is a organization local multicast address.
     *
     * Valid IPv6 organization local multicast addresses are FFx8:/112 where x
     * is a set of flags, and the additional 112 bits make up the node local
     * multicast address space
     *
     * Valid IPv4 organization-local addresses are between: 239.192.0.0 to
     * 239.251.255.255
     *
     * @return boolean
     */
    public boolean isMCOrgLocal() {
        return false;
    }

    /**
     * Method isAnyLocalAddress.
     *
     * @return boolean
     */
    public boolean isAnyLocalAddress() {
        return false;
    }

    private static boolean isIPv4MappedAddress(byte ipAddress[]) {
        // Check if the address matches ::FFFF:d.d.d.d
        // The first 10 bytes are 0. The next to are -1 (FF).
        // The last 4 bytes are varied.
        for (int i = 0; i < 10; i++) {
            if (ipAddress[i] != 0) {
                return false;
            }
        }

        if (ipAddress[10] != -1 || ipAddress[11] != -1) {
            return false;
        }

        return true;
    }

    /**
     * Takes the integer and chops it into 4 bytes, putting it into the byte
     * array starting with the high order byte at the index start. This method
     * makes no checks on the validity of the parameters.
     */
    static void intToBytes(int value, byte bytes[], int start) {
        // Shift the int so the current byte is right-most
        // Use a byte mask of 255 to single out the last byte.
        bytes[start] = (byte) ((value >> 24) & 255);
        bytes[start + 1] = (byte) ((value >> 16) & 255);
        bytes[start + 2] = (byte) ((value >> 8) & 255);
        bytes[start + 3] = (byte) (value & 255);
    }

    /**
     * Takes the byte array and creates an integer out of four bytes starting at
     * start as the high-order byte. This method makes no checks on the validity
     * of the parameters.
     */
    static int bytesToInt(byte bytes[], int start) {
        // First mask the byte with 255, as when a negative
        // signed byte converts to an integer, it has bits
        // on in the first 3 bytes, we are only concerned
        // about the right-most 8 bits.
        // Then shift the rightmost byte to align with its
        // position in the integer.
        int value = ((bytes[start + 3] & 255))
                | ((bytes[start + 2] & 255) << 8)
                | ((bytes[start + 1] & 255) << 16)
                | ((bytes[start] & 255) << 24);
        return value;
    }

}
