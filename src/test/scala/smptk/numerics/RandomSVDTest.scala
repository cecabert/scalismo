package smptk.numerics

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import smptk.registration.Kernel
import smptk.registration.GaussianKernel1D
import smptk.image.Geometry.CoordVector1D

class RandomSVDTest extends FunSpec with ShouldMatchers {

  describe("The random svd") { 
    it("accurately approximates the first 10 eigenvectors and eigenvalues of a gaussian kernel matrix") { 
      val k = GaussianKernel1D(10)
      val xs = (0 until 500).map(x => CoordVector1D(x.toDouble)) 
      val K = Kernel.computeKernelMatrix(xs, k)
      
      val (ur, lr, vrt) = RandomSVD.computeSVD(K, 10)
      val (ub, lb, vbt) = breeze.linalg.svd(K)

      for  (j <- 0 until 10) {
        (lr(j) should be (lb(j) plusOrMinus 0.001))
        
        for (i <- (0 until ur.rows)) {
        	(math.abs(ur(i,j)) should be (math.abs(ub(i,j)) plusOrMinus 0.001)) 
        }

        for (i <- (0 until vrt.rows)) {
        	(math.abs(vrt(i,j)) should be (math.abs(vbt(i,j)) plusOrMinus 0.001)) 
        }

      }
    }
  }
}