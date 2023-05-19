/**
 * 
 */
package edu.ics211.h11;

/**
 * An interface containing the methods that must be implemented in BitString
 * @author edo, loelle
 *
 */
public interface BitStringInterface {
	  // this constructor builds a bitstring from an array of bytes
	  // the very first byte is not part of the bitstring, it contains
	  // a value between 1 and 8 to record how many bits of the last byte
	  // are part of the bitstring
	  // BitString(byte[] bits);

	  // this constructor builds an empty bitstring
	  // BitString();

	  // how many bits are in this bitstring?
	  int length();

	  // returns each bit in turn, true representing 1 and false representing 0
	  java.util.Iterator<Boolean> iterator();

	  // adds these bits to the end of the string of bits
	  void addBits(Boolean[] bits);

	  // returns the bytes corresponding to the bitstring, in the same format
	  // as for the constructor
	  byte[] toBytes();

	  // the toString method returns all the bits in the bitstring as 0s and 1s
	  // not part of this interface but included in a comment because we
	  // inherit it from the Object class and override it
	  // String toString();
}
