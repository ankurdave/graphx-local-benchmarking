#include <cstdlib>
#include <unordered_map>
#include <vector>
#include <ctime>

// Usage:
// g++ -Wall -O3 mrTriplets.cpp -o mrTriplets
// ./mrTriplets 14167504 < ~/Downloads/uk-2007-05-coalesced-part-00137

int main(int argc, char* argv[]) {

  int num_edges = atoi(argv[1]);

  std::vector<int> srcIds(num_edges);
  std::vector<int> dstIds(num_edges);
  std::vector<double> attrs(num_edges, 1.0);
  std::unordered_map<long, int> vertex_index;

  printf("Loading...\n");

  long src, dst;
  int voffset = 0;
  for (int i = 0; i < num_edges; i++) {
    scanf("%ld\t%ld", &src, &dst);

    if (vertex_index.find(src) == vertex_index.end()) {
      vertex_index[src] = voffset;
      srcIds[i] = voffset;
      voffset++;
    } else {
      srcIds[i] = vertex_index[src];
    }

    if (vertex_index.find(dst) == vertex_index.end()) {
      vertex_index[dst] = voffset;
      dstIds[i] = voffset;
      voffset++;
    } else {
      dstIds[i] = vertex_index[dst];
    }
  }

  std::vector<double> vertex_attrs(voffset, 1.0);

  printf("Scanning...\n");

  std::vector<double> vertex_preagg(voffset, 0.0);

  clock_t start_time = clock();

  for (size_t k = 0; k < 10; ++k) {
    for (int i = 0; i < num_edges; i++) {
      vertex_preagg[dstIds[i]] += vertex_attrs[srcIds[i]] * attrs[i];;
    }
  }

  clock_t end_time = clock();
  printf("Scanned %d edges in %f seconds\n",
         num_edges, (end_time - start_time) / static_cast<double>(CLOCKS_PER_SEC));

  double sum = 0.0;
  for (int i = 0; i < voffset; i++) {
    sum += vertex_preagg[i];
  }
  printf("Sum: %f\n", sum);

  return 0;
}
