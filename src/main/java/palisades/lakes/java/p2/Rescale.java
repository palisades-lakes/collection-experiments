package palisades.lakes.java.p2;


/** Move coordinates with the point's equivalence class.
 * (Scalar multiplication doesn't make sense for affine and
 * projective spaces, only linear.)
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2017-12-08
 */
public final class Rescale {

  //--------------------------------------------------------------
  /** Move coordinates with the point's equivalence class.
   * (Scalar multiplication doesn't make sense for affine and
   * projective spaces, only linear.)
   */
  public static final H2 rescale (final double w, 
                                  final D2 p) {
    return H2.make(w*p.getX(),w*p.getY(),w); }

  /** Move coordinates with the point's equivalence class.
   * (Scalar multiplication doesn't make sense for affine and
   * projective spaces, only linear.)
   */
  public static final H2 rescale (final double w, 
                                  final H2 p) {
    return H2.make(w*p.getX(),w*p.getY(),w*p.getW()); }

  /** Move coordinates with the point's equivalence class.
   * (Scalar multiplication doesn't make sense for affine and
   * projective spaces, only linear.)
   */
  public static final H2 rescale (final double w, 
                                  final Object p) {
    if (p instanceof D2) { return rescale(w,(D2) p); }
    if (p instanceof H2) { return rescale(w,(H2) p); }
    throw new UnsupportedOperationException(
      "can't rescale" + p.getClass()); }

  //--------------------------------------------------------------
  // disable construction
  //--------------------------------------------------------------

  private Rescale () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  //--------------------------------------------------------------
}
