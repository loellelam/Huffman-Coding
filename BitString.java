/**
 * 
 */
package edu.ics211.h11;

/**
 * Store, convert, and access relevant information on bits and bytes 
 * represented as an array of booleans
 * @author edo, loelle
 *
 */
public class BitString implements BitStringInterface {

	java.util.ArrayList<Boolean> bitString;

	/**
	 * Converts a byte to a bit array using true/false to represent each bit
	 */
	  private void addBitsInByte(byte value, int count) {
	    int mask = 128;
	    for (int b = 0; b < count; b++) {
	      bitString.add((value & mask) != 0);
	      mask = mask / 2;
	    }
	  }

	  /**
	   * Converts 8 bits to a byte
	   * @return	byte representation
	   */
	  private byte bitsToByte(Boolean b1, Boolean b2, Boolean b3, Boolean b4,
	                          Boolean b5, Boolean b6, Boolean b7, Boolean b8) {
	    int result = (b1 ? 128 : 0) +
	                 (b2 ?  64 : 0) +
	                 (b3 ?  32 : 0) +
	                 (b4 ?  16 : 0) +
	                 (b5 ?   8 : 0) +
	                 (b6 ?   4 : 0) +
	                 (b7 ?   2 : 0) +
	                 (b8 ?   1 : 0);
	    return (byte)result;
	  }

	  // this constructor builds an empty bitstring
	  public BitString() {
	    bitString = new java.util.ArrayList<Boolean>();
	  }

	  // this constructor builds a bitstring from an array of bytes
	  // the very first byte is not part of the bitstring, it contains
	  // a value between 1 and 8 to record how many bits of the last byte
	  // are part of the bitstring
	  public BitString(byte[] bits) {
	    assert (bits != null) : "constructor bits is null";
	    assert (bits.length > 0) : ("constructor bits length " + bits.length);
	    assert (bits[0] >= 1) : ("constructor last byte bits < 1, " + bits[0]);
	    assert (bits[0] <= 8) : ("constructor last byte bits > 8, " + bits[0]);
	    for (byte b: bits) {
	      System.out.printf("%02x ", b);
	    }
	    System.out.println();
	    bitString = new java.util.ArrayList<Boolean>();
	    // add the bits of all but the last byte
	    for (int i = 1; i + 1 < bits.length; i++) {
	      addBitsInByte(bits[i], 8);
	    }
	    // add the bits of the last byte
	    addBitsInByte(bits[bits.length - 1], bits[0]);
	  }

	  // how many bits are in this bitstring?
	  public int length() {
	    return bitString.size();
	  }

	  // adds these bits at the end of the string of bits
	  public void addBits(Boolean[] bits) {
	    for (Boolean b: bits) {
	      bitString.add(b);
	    }
	  }

	  // returns the bytes corresponding to the bitstring, in the same format
	  // as for the constructor
	  public byte[] toBytes() {
	    // in this computation, we round up to the next whole byte,
	    // so for example 7 becomes 1, 8 is 1, 9 is 2
	    int dataBytes = (bitString.size() + 7) / 8;
	    // the initial byte of the array holds the number of bits in the final
	    // byte, so the result array has to have one more byte than the dataBytes
	    byte[] result = new byte[1 + dataBytes];
	    result[0] = (byte)(bitString.size() % 8);
	    if (result[0] == 0) {
	      result[0] = 8;
	    }
	    assert (result[0] >= 1) : ("toBytes, last byte has " + result[0] + " bits");
	    assert (result[0] <= 8) : ("toBytes, last byte has " + result[0] + " bits");
	    for (int i = 0; i < dataBytes - 1; i++) {
	      result[i + 1] = bitsToByte(bitString.get(i * 8),
	                                 bitString.get(i * 8 + 1),
	                                 bitString.get(i * 8 + 2),
	                                 bitString.get(i * 8 + 3),
	                                 bitString.get(i * 8 + 4),
	                                 bitString.get(i * 8 + 5),
	                                 bitString.get(i * 8 + 6),
	                                 bitString.get(i * 8 + 7));
	    }
	    if (dataBytes > 0) {
	      // get the bits of the last byte and add them to the result
	      int index = (dataBytes - 1) * 8;
	      Boolean b1 = bitString.get(index);
	      Boolean b2 = (result[0] >= 2) && bitString.get(index + 1);
	      Boolean b3 = (result[0] >= 3) && bitString.get(index + 2);
	      Boolean b4 = (result[0] >= 4) && bitString.get(index + 3);
	      Boolean b5 = (result[0] >= 5) && bitString.get(index + 4);
	      Boolean b6 = (result[0] >= 6) && bitString.get(index + 5);
	      Boolean b7 = (result[0] >= 7) && bitString.get(index + 6);
	      Boolean b8 = (result[0] >= 8) && bitString.get(index + 7);
	      result[dataBytes] = bitsToByte(b1, b2, b3, b4, b5, b6, b7, b8);
	    }
	    return result;
	  }

	  // the toString method returns all the bits in the bitstring as 0s and 1s
	  // not part of this interface but included in a comment because we
	  // inherit it from the Object class and override it
	  public String toString() {
	    StringBuilder result = new StringBuilder();
	    int bitsBeforeSpace = 4;
	    for (Boolean b: bitString) {
	      if (bitsBeforeSpace == 0) {
	        result.append(" ");
	        bitsBeforeSpace = 3;  // 4, already decremented by one
	      } else {
	        bitsBeforeSpace--;
	      }
	      result.append(b ? "1" : "0");
	    }
	    return result.toString();
	  }

	  private class BitIterator implements java.util.Iterator<Boolean> {
	    Boolean[] bits;
	    int position;

	    public BitIterator(java.util.ArrayList<Boolean> initialBits) {
	      assert (initialBits != null);
	      Boolean[] type = new Boolean[1];
	      bits = initialBits.toArray(type);
	      position = 0;
	    }

	    public boolean hasNext() {
	      return position < bits.length;
	    }

	    public Boolean next() {
	      try {
	        int index = position;
	        position++;
	        return bits[index];
	      } catch (Exception e) {   // usually, ArrayIndexOutOfBounds
	        throw new java.util.NoSuchElementException();
	      }
	    }
	  }

	  // returns each bit in turn, true representing 1 and false representing 0
	  public java.util.Iterator<Boolean> iterator() {
	    return new BitIterator(bitString);
	  }

	  // we wish this was the .equals method for arrays
	  private static boolean arraysEqual(byte[] a1, byte[] a2) {
	    if ((a1 == null) && (a2 == null)) {
	      return true;
	    }
	    if ((a1 == null) || (a2 == null)) {
	      return false;
	    }
	    if ((a1.length != a2.length)) {
	      System.out.println ("lengths are " + a1.length + " and " + a2.length);
	      return false;
	    }
	    for (int i = 0; i < a1.length; i++) {
	      if (a1 [i] != a2 [i]) {
	        System.out.println ("values [" + i + "] are " + a1[i] + ", " + a2[i]);
	        return false;
	      }
	    }
	    return true;
	  }

	  // a simple unit test
	  private static boolean unitTest(byte[] array, int expectedLength) {
	    BitString b = new BitString(array);
	    assert (b.length() == expectedLength) :
	            ("length " + b.length() + " != expected " + expectedLength);
	    System.out.println("bitstring is " + b);
	    byte[] copy = b.toBytes();
	    if (arraysEqual(copy, array)) {
	      return true;
	    }
	    for (int i = 0; i < copy.length; i++) {
	      System.out.println("copy [" + i + "] " + copy[i] + " =? " + array[i]);
	    }
	    return false;
	  }

	  public static void main(String[] args) {
	    byte[] b1 = { 1, -128 };
	    if (unitTest(b1, 1)) {
	      byte[] b2 = { 3, 5, 120, -96 };
	      if (unitTest(b2, 19)) {
	        byte[] b3 = new byte[100];
	        java.util.Random r = new java.util.Random();
	        r.nextBytes(b3);
	        int bit = 128;
	        b3[b3.length - 1] = 0;
	        for (byte i = 1; i <= 8; i++) {
	          b3[0] = i;  // 1..8 terminating bits
	          b3[b3.length - 1] += (r.nextBoolean() ? (byte)bit : 0);
	          bit = bit / 2;
	          if (! unitTest(b3, (b3.length - 2) * 8 + i)) {
	            System.out.println ("unit test failed for i " + i);
	          }
	        }
	      }
	    }
	  }

}
