package palisades.lakes.java.p2;


/** Objects parameterized with <code>double</code> will often not
 * be exactly equal, but need,at least for testing, some
 * notion of 'close enough'.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2017-12-08
 */
public final class Equivalent {

  //--------------------------------------------------------------
  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final D2 p0, 
                                             final D2 p1,
                                             final double delta) {
    return 
      (Math.abs(p0.getX() - p1.getX()) <= delta)
      &&
      (Math.abs(p0.getY() - p1.getY()) <= delta); }

  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final D2 p0, 
                                             final D2 p1) {
    final double delta = 
      Math.ulp(
        10.0 * 
        ( Math.abs(p0.getX()) + 
          Math.abs(p0.getY()) +
          Math.abs(p1.getX()) +
          Math.abs(p1.getY()) ));
    return approximately(p0,p1,delta); }

  //--------------------------------------------------------------
  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final H2 p0, 
                                             final H2 p1,
                                             final double delta) {
    // TODO:  Is * really better than / ?
    // TODO: would fused-multiply-add help?
    final double p0w = p0.getW();
    final double p1w = p1.getW();
    final double p0x = p0.getX() * p1w;
    final double p0y = p0.getY() * p1w;
    final double p1x = p1.getX() * p0w;
    final double p1y = p1.getY() * p0w;
    return 
      (Math.abs(p0x - p1x) <= delta)
      &&
      (Math.abs(p0y - p1y) <= delta); }

  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final H2 p0, 
                                             final H2 p1) {
    final double p0w = p0.getW();
    final double p1w = p1.getW();
    final double p0x = p0.getX() * p1w;
    final double p0y = p0.getY() * p1w;
    final double p1x = p1.getX() * p0w;
    final double p1y = p1.getY() * p0w;
    final double delta = 
      Math.ulp( 
        10.0 * 
        ( Math.abs(p0x) + 
          Math.abs(p0y) + 
          Math.abs(p1x) + 
          Math.abs(p1y) ));
    return approximately(p0,p1,delta); }

  //--------------------------------------------------------------
  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final D2 p0, 
                                             final H2 p1,
                                             final double delta) {
    // TODO:  Is * really better than / ?
    // TODO: would fused-multiply-add help?
    final double p1w = p1.getW();
    final double p0x = p0.getX() * p1w;
    final double p0y = p0.getY() * p1w;
    final double p1x = p1.getX();
    final double p1y = p1.getY();
    return 
      (Math.abs(p0x - p1x) <= delta)
      &&
      (Math.abs(p0y - p1y) <= delta); }

  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final D2 p0, 
                                             final H2 p1) {
    final double p1w = p1.getW();
    final double p0x = p0.getX() * p1w;
    final double p0y = p0.getY() * p1w;
    final double p1x = p1.getX();
    final double p1y = p1.getY();
    final double delta = 
      Math.ulp( 
        10.0 * 
        ( Math.abs(p0x) + 
          Math.abs(p0y) + 
          Math.abs(p1x) + 
          Math.abs(p1y) ));
    return approximately(p0,p1,delta); }

  //--------------------------------------------------------------
  // TODO: better to just reverse the args?
  //--------------------------------------------------------------
  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final H2 p0, 
                                             final D2 p1,
                                             final double delta) {
    // TODO:  Is * really better than / ?
    // TODO: would fused-multiply-add help?
    final double p0w = p0.getW();
    final double p0x = p0.getX();
    final double p0y = p0.getY();
    final double p1x = p1.getX() * p0w;
    final double p1y = p1.getY() * p0w;
    return 
      (Math.abs(p0x - p1x) <= delta)
      &&
      (Math.abs(p0y - p1y) <= delta); }

  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final H2 p0, 
                                             final D2 p1) {
    final double p0w = p0.getW();
    final double p0x = p0.getX();
    final double p0y = p0.getY();
    final double p1x = p1.getX() * p0w;
    final double p1y = p1.getY() * p0w;
    final double delta = 
      Math.ulp( 
        10.0 * 
        ( Math.abs(p0x) + 
          Math.abs(p0y) + 
          Math.abs(p1x) + 
          Math.abs(p1y) ));
    return approximately(p0,p1,delta); }

  //--------------------------------------------------------------
  /** Corresponding parameters are close enough.
   */
  public static final boolean approximately (final Object p0, 
                                             final Object p1) {
    if (p0 instanceof D2) {
      if (p1 instanceof D2) {
        return approximately((D2) p0, (D2) p1); }
      if (p1 instanceof H2) {
        return approximately((D2) p0, (H2) p1); } }
    if (p0 instanceof H2) {
      if (p1 instanceof D2) {
        return approximately((H2) p0, (D2) p1); }
      if (p1 instanceof H2) {
        return approximately((H2) p0, (H2) p1); } }
    throw new UnsupportedOperationException(
      "can't compare" 
        + p0.getClass() 
        + " and " 
        + p1.getClass()); }

  //--------------------------------------------------------------
  // disable construction
  //--------------------------------------------------------------

  private Equivalent () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

}
