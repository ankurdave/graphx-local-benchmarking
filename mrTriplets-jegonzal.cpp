#include <cstdlib>
#include <unordered_map>
#include <vector>
#include <ctime>

// Usage:
// g++ -Wall -O3 mrTriplets.cpp -o mrTriplets
// ./mrTriplets 14167504 < ~/Downloads/uk-2007-05-coalesced-part-00137

struct vertex_value {
  long id;
  double value;
  vertex_value(long id = -1.0, double value = -1.0): id(id), value(value) { }
};

int main(int argc, char* argv[]) {

  int num_edges = atoi(argv[1]);

  std::vector<long> srcIds(num_edges);
  std::vector<long> dstIds(num_edges);
  std::vector<double> attrs(num_edges, 1.0);
  std::unordered_map<long, long> global2local;
  std::vector<vertex_value> vertex_attr;

  printf("Loading...\n");


  long localId = 0;

  for (int i = 0; i < num_edges; i++) {
    long src, dst;
    scanf("%ld\t%ld", &src, &dst);

    long localSrc = -1;
    const std::unordered_map<long, long>::const_iterator srcIt = global2local.find(src);
    if (srcIt != global2local.end()) {
      localSrc = srcIt->second;
    } else {
      localSrc = localId++;
      global2local[src] = localSrc;
      vertex_attr.push_back(vertex_value(src, 1.0));
    }

    long localDst = -1;
    const std::unordered_map<long, long>::const_iterator dstIt = global2local.find(dst);
    if (dstIt != global2local.end()) {
      localDst = dstIt->second;
    } else {
      localDst = localId++;
      global2local[dst] = localDst;
      vertex_attr.push_back(vertex_value(dst, 1.0));
    }

    srcIds[i] = localSrc;
    dstIds[i] = localDst;
  }

  std::vector<double> msg_sum(vertex_attr.size());



  printf("Scanning...\n");

  clock_t start_time = clock();

  for (int i = 0; i < num_edges; i++) {
    double msg = vertex_attr[srcIds[i]].value * attrs[i];
    msg_sum[dstIds[i]] += msg;
  }

  clock_t end_time = clock();
  printf("Scanned %d edges in %f seconds\n",
         num_edges, (end_time - start_time) / static_cast<double>(CLOCKS_PER_SEC));

  return 0;
}
