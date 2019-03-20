void getCovexHullSolution(int, int*, int[][2], int*);
void getCovexHullSolutionWithSeparatePoints(int, int[][2], int[][2], int*);
float area(int x1, int y1, int x2, int y2, int x3, int y3); 
int isInside(int x1, int y1, int x2, int y2, int x3, int y3, int x, int y);

/**
 * Returns the points of the smallest Convel hull that contains all the points inside
 * @param noOfPoints   Number of input points.
 * @param inputPoints  intput points in order of [x1 ,y1, x2,y2, ..,xn,yn]
 * @param outputPoints Ouput Array e.g int ** outputPoints
 * @param noOfOutput   SizeOf Output Array.
 */
void getCovexHullSolution(int noOfPoints, int* inputPoints, int outputPoints[][2], int* noOfOutput) {
  /*
    Convert input points into two dimensional array and pass to main function.
   */
  int inpPoints[noOfPoints][2];
  for (int i = 0 , c = 0; i < noOfPoints ; i++, c += 2) {
    for (int j = 0 ; j < 2; j++) {
      inpPoints[i][j] = *(inputPoints + c + j);
    }
  }
  //Print The Array.
  for (int i = 0 ; i < noOfPoints; i ++) {
    printf("%d : (%d,%d)\n", i, inpPoints[i][0], inpPoints[i][1]);
  }
  getCovexHullSolutionWithSeparatePoints(noOfPoints, inpPoints, outputPoints, noOfOutput);
}

/**
 * Returns the points of the smallest Convel hull that contains all the points inside
 * @param noOfPoints   Number of input points.
 * @param inputPoints  intput points in order of [ [x1 ,y1], [x2,y2] , ..,[xn,yn]]
 * @param outputPoints Ouput Array e.g int ** outputPoints
 * @param noOfOutput   SizeOf Output Array.
 */
void getCovexHullSolutionWithSeparatePoints(int noOfPoints, int inputPoints[][2], int outputPoints[][2], int* noOfOutput) {
  /**
   * Separate inputPoints in X and Y arrays.
   */
  int X[noOfPoints], Y[noOfPoints];
  int minX[2], maxX[2], mx, m_x;
  int minY[2], maxY[2], my, m_y, n = 0;
  int pointsOutsideOfPolygon[noOfPoints];

  for (int i = 0 ; i < noOfPoints; i ++) {
    X[i] = inputPoints[i][0];
    Y[i] = inputPoints[i][1];
  }

  mx = X[0];
  minX[0] = inputPoints[0][0];
  minX[1] = inputPoints[0][1];

  m_x = X[0];
  maxX[0] = inputPoints[0][0];
  maxX[1] = inputPoints[0][1];


  my = Y[0];
  minY[0] = inputPoints[0][0];
  minY[1] = inputPoints[0][1];

  m_y = Y[0];
  maxY[0] = inputPoints[0][0];
  maxY[1] = inputPoints[0][1];

  for (int i = 1 ; i < noOfPoints; i ++) {
    if (mx > X[i]) {
      mx = X[i];
      minX[0] = inputPoints[i][0];
      minX[1] = inputPoints[i][1];
    } else if (m_x < X[i]) {
      m_x = X[i];
      maxX[0] = inputPoints[i][0];
      maxX[1] = inputPoints[i][1];
    }

    if (my > Y[i]) {
      my = Y[i];
      minY[0] = inputPoints[i][0];
      minY[1] = inputPoints[i][1];
    } else if (m_y < Y[i]) {
      m_y = Y[i];
      maxY[0] = inputPoints[i][0];
      maxY[1] = inputPoints[i][1];
    }
  }

  printf("Min X : (%d, %d)\n", minX[0], minX[1]);
  printf("Max X : (%d, %d)\n", maxX[0], maxX[1]);
  printf("Min Y : (%d, %d)\n", minY[0], minY[1]);
  printf("Max Y : (%d, %d)\n", maxY[0], maxY[1]);

  // Added Max and Min Points to output List
  outputPoints[*noOfOutput][0] = minX[0];
  outputPoints[(*noOfOutput)++][1] = minX[1];

  outputPoints[*noOfOutput][0] = maxX[0];
  outputPoints[(*noOfOutput)++][1] = maxX[1];

  outputPoints[*noOfOutput][0] = minY[0];
  outputPoints[(*noOfOutput)++][1] = minY[1];

  outputPoints[*noOfOutput][0] = maxY[0];
  outputPoints[(*noOfOutput)++][1] = maxY[1];

  /**
    Check if anr points is outside of polygon.
  */
  int f = 1, c = 0;
  for (int i = 0 ; i < noOfPoints; i ++) {
    if (isInside(minX[0],minX[1],minY[0],minY[1],maxX[0],maxX[1],inputPoints[i][0],inputPoints[i][1]) || isInside(minX[0],minX[1],minY[0],minY[1],maxY[0],maxY[1],inputPoints[i][0],inputPoints[i][1])) {
      // point is inside the polygon.
      printf("Inside Circle\n");
    } else {
      printf("Outside Circle\n");

      f = 0;
      // point is outside of polygon.
      pointsOutsideOfPolygon[c] = i;
      c++;
    }
  }

  if (f) {
    printf("====================================================\n");
    
    for (int i = 0 ; i < *noOfOutput ; i++) {
      printf("%d: (%d,%d)\n", i, outputPoints[i][0], outputPoints[i][1]);
    }
    
    printf("====================================================\n");
    return;
  } else {
    for (int i = 0 ; i < c ; i++) {
      inputPoints[i][0] = inputPoints[pointsOutsideOfPolygon[i]][0];
      inputPoints[i][1] = inputPoints[pointsOutsideOfPolygon[i]][1];
    }
    printf(">>>\n");
    getCovexHullSolutionWithSeparatePoints(c, inputPoints, outputPoints, noOfOutput);
  }


  return;
}

float area(int x1, int y1, int x2, int y2, int x3, int y3) 
{ 
   return abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0); 
} 

int isInside(int x1, int y1, int x2, int y2, int x3, int y3, int x, int y) 
{    
   /* Calculate area of triangle ABC */
   float A = area (x1, y1, x2, y2, x3, y3); 
  
   /* Calculate area of triangle PBC */   
   float A1 = area (x, y, x2, y2, x3, y3); 
  
   /* Calculate area of triangle PAC */   
   float A2 = area (x1, y1, x, y, x3, y3); 
  
   /* Calculate area of triangle PAB */    
   float A3 = area (x1, y1, x2, y2, x, y); 
    
   /* Check if sum of A1, A2 and A3 is same as A */ 
   return (A == A1 + A2 + A3); 
} 