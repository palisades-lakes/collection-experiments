package palisades.lakes.java.p2;

import clojure.lang.IFn;

//----------------------------------------------------------------
/** (Immutable) point in <b>P</b><sup>2</sup> represented 
 * by homogeneous <code>double</code> <code>x</code>, 
 * <code>y</code>, and <code>w</code> coordinates, 
 * for benchmarking collections.
 *<p>
 * No interfaces but Object; generic operations defined via
 * <code>defmulti</code> or explicit <code>instanceof</code>
 * if-then-else.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2017-12-08
 */

public final class H2 {

  // TODO: to me, (w,x,y) seems more natural than (x,y,w),
  // especially since we have (w,x), (w,x,y,z), etc., for P1, P3, 
  // etc. But (x,y,w) convention seems almost universal, in GPU
  // code as well as literature.
  
  private final double _x;
  public final double getX () { return _x; }
  private final double _y;
  public final double getY () { return _y; }
  private final double _w;
  public final double getW () { return _w; }

  //--------------------------------------------------------------
  // Object interface
  //--------------------------------------------------------------

  @Override
  public final String toString () {
    return
      String.format("H2[%2.4g, %2.4g, %2.4g]", 
        Double.valueOf(_x),
        Double.valueOf(_y),
        Double.valueOf(_w)); }
  
  @Override
  public final int hashCode () {
    int c = 17;

    final long l0  = Double.doubleToLongBits(_x);
    final int c0 = (int) (l0 ^ (l0 >>> 32));
    c += 37*c0;
    
    final long l1  = Double.doubleToLongBits(_y);
    final int c1 = (int) (l1 ^ (l1 >>> 32));
    c += 37*c1;
    
    final long l2  = Double.doubleToLongBits(_w);
    final int c2 = (int) (l2 ^ (l2 >>> 32));
    c += 37*c2;
    
    return c; }

  @Override
  public final boolean equals (final Object o) {
    if (this == o) { return true; }
    if (! (o instanceof H2)) { return false; }
    final H2 i = (H2) o;
    return 
      (_x == i.getX()) 
      && 
      (_y == i.getY()) 
      && 
      (_w == i.getW()); }
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private H2 (final double x, 
              final double y,
              final double w) {
    _x = x; _y = y; _w = w; }

  public static final H2 make (final double x, 
                               final double y,
                               final double w) {

    return new H2(x,y,w); }

  public static final H2 make (final double x, 
                               final double y) {

    return make(x,y,1.0); }

  /** <code>g</code> is a 'function' of no arguments, which is 
   * expected to return a different value on each call, typically
   * wrapping some pseudo-random number generator.
   * Clojure unfortunately only supports functions returning
   * primitive <code>long</code> and <code>double</code>
   * values.
   */

  public static final H2 generate (final IFn.D g) {
    final double x = g.invokePrim();
    final double y = g.invokePrim();
    final double w = g.invokePrim();
    return make(x,y,w); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
