#include <stdio.h>
#include <stdlib.h>
#include "convex_hull.h"


int main(int argc, char const *argv[]) {
  /*
  * Check if minimum arguments are provided!
  */
  if (argc < 8) {
    printf("Atleast Require seven arguments To run.\n");
    printf("\t1. No. Of Points (Minimum - 3)\n");
    printf("\t2. X Y Co-ordinates of each Point.\n");
    exit(0);
  }

  /*
  * Check if no. of point and point axis matches each other.
  */
  if ((argc - 2) / (float)2 != (float)atoi(argv[1])) {
    printf("Error: No. of Points and No. of axis input doen't matches\n");
    exit(0);
  }
  /*
  * Remove ./ConvexHull and no. of points from input.
  */
  int *inputPoints = (int *)malloc(sizeof(int) * (argc - 2));

  for (int i = 2; i < argc; i++) {
    *(inputPoints + i - 2) = atoi(argv[i]);
  }


  int points[100][2], n=0;
  getCovexHullSolution(atoi(argv[1]), inputPoints, points, &n);
  return 0;
}
