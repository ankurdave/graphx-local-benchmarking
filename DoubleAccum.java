class DoubleAccum implements Accum<Double> {

  public double sum = 0.0;

  public void add(Double x) {
    sum += x;
  }

  public void print() {
    System.out.println(sum);
  }


  public static void main(String[] args) {
    Accum<Double> accum = new DoubleAccum();
    double i = 0.0;
    long start_time = System.nanoTime();
    while (i < 1.0e10) {
      accum.add(i);
      i += 1.0;
    }
    double end_time = System.nanoTime();
    double dur = (end_time - start_time) / 1.0e9;
    accum.print();
    System.out.println("Runtime: " + dur);
  }

}