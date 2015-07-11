class DoubleAccum2 implements AccumD {

  public double sum = 0.0;

  public void add(double x) {
    sum += x;
  }

  public void print() {
    System.out.println(sum);
  }


  public static void main(String[] args) {
    DoubleAccum2 accum = new DoubleAccum2();
    double i = 0.0;
    long start_time = System.nanoTime();
    double acc = 0.0;
    while (i < 1.0e10) {
      acc += i;
      i += 1.0;
    }
    double end_time = System.nanoTime();
    double dur = (end_time - start_time) / 1.0e9;
    accum.print();
    System.out.println(acc);
    System.out.println("Runtime: " + dur);
  }


  // public static void main(String[] args) {
  //   AccumD accum = new DoubleAccum2();
  //   double i = 0.0;
  //   long start_time = System.nanoTime();
  //   while (i < 1.0e10) {
  //     accum.add(i);
  //     i += 1.0;
  //   }
  //   double end_time = System.nanoTime();
  //   double dur = (end_time - start_time) / 1.0e9;
  //   accum.print();
  //   System.out.println("Runtime: " + dur);
  // }

}