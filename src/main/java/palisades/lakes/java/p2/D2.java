package palisades.lakes.java.p2;

import clojure.lang.IFn;

//----------------------------------------------------------------
/** (Immutable) point in <b>P</b><sup>2</sup> represented 
 * by non-homogeneous <code>double</code> <code>x</code> 
 * and <code>y</code>
 * coordinates, for benchmarking collections.
 *<p>
 * No interfaces but Object; generic operations defined via
 * <code>defmulti</code> or explicit <code>instanceof</code>
 * if-then-else.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2017-12-08
 */

public final class D2 {

  private final double _x;
  public final double getX () { return _x; }
  private final double _y;
  public final double getY () { return _y; }

  //--------------------------------------------------------------
  // Object interface
  //--------------------------------------------------------------
  @Override
  public final String toString () {
    return
      String.format("D2[%2.4g, %2.4g]", 
        Double.valueOf(_x),
        Double.valueOf(_y)); }
  @Override
  public final int hashCode () {
    int c = 17;

    final long l0  = Double.doubleToLongBits(_x);
    final int c0 = (int) (l0 ^ (l0 >>> 32));
    c += 37*c0;
    
    final long l1  = Double.doubleToLongBits(_y);
    final int c1 = (int) (l1 ^ (l1 >>> 32));
    c += 37*c1;
    
    return c; }

  @Override
  public final boolean equals (final Object o) {
    if (this == o) { return true; }
    if (! (o instanceof D2)) { return false; }
    final D2 i = (D2) o;
    return (_x == i.getX()) && (_y == i.getY()); }
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private D2 (final double x, 
              final double y) {
    _x = x; _y = y; }

  public static final D2 make (final double v0, final double v1) {

    return new D2(v0,v1); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   */

  public static final D2 generate (final IFn.D g) {
    final double x = g.invokePrim();
    final double y = g.invokePrim();
    return make(x,y); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
