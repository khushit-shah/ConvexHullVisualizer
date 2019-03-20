import numpy as np
import matplotlib.pyplot as plt
import sys

no_points = int(sys.argv[1])

xpoints = sys.argv[2::2]
print(xpoints)


ypoints = sys.argv[3::2]
print(ypoints)


order = np.argsort(xpoints)
xs = np.array(xpoints)[order]
ys = np.array(ypoints)[order]

plt.plot(xs, ys)
plt.show()