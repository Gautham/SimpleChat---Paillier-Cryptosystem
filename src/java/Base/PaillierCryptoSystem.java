package Base;

import java.util.Random;
import java.math.*;


public class PaillierCryptoSystem {
	
	int p, q;
	BigInteger n, lambda, g, psi, nu;
	
	static int gcd(int a, int b) {
		if (a < b) return gcd(b, a);
		if (a % b == 0) return b;
		else return gcd(b, a % b);
	}

	static boolean isPrime(int f) {
		if (f % 2 == 0) return false;
		else for (int i = 3; i*i <= f ; i += 2) if (f % i == 0) return false;
		return true;
	}
	
	static BigInteger lcm(int a, int b) {
		BigInteger out = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
		out = out.divide(BigInteger.valueOf(gcd(a, b)));
		return out;
	}
	
		
	public BigInteger Encrypto(int m) {
		int r = new Random().nextInt(10) + 3;
		BigInteger Ans = BigInteger.ZERO;
		BigInteger nSquare = n.multiply(n);
		Ans = g.modPow(BigInteger.valueOf(m), nSquare);
		Ans = Ans.multiply(BigInteger.valueOf(r).modPow(n, nSquare));
		Ans = Ans.remainder(nSquare);
		return Ans;
	}

	public BigInteger Encrypto(int m, BigInteger n, BigInteger g) {
		int r = new Random().nextInt(10) + 3;
		BigInteger Ans = BigInteger.ZERO;
		BigInteger nSquare = n.multiply(n);
		Ans = g.modPow(BigInteger.valueOf(m), nSquare);
		Ans = Ans.multiply(BigInteger.valueOf(r).modPow(n, nSquare));
		Ans = Ans.remainder(nSquare);
		return Ans;
	}
	
	public String Encrypt(String M) {
		int len = M.length();
		String C = "";
		java.text.NumberFormat nf = new java.text.DecimalFormat("0000000000000000000000");
		for (int i = 0; i < len; i++) {
			BigInteger c = Encrypto(M.charAt(i));
			C = C.concat(nf.format(c));
		}
		return C;
	}

	public String Encrypt(String M, BigInteger n, BigInteger g) {
		int len = M.length();
		String C = "";
		java.text.NumberFormat nf = new java.text.DecimalFormat("0000000000000000000000");
		for (int i = 0; i < len; i++) {
			BigInteger c = Encrypto(M.charAt(i), n, g);
			C = C.concat(nf.format(c));
		}
		return C;
	}

	public BigInteger GetPublicKey() {
            return n;
	}
	
	public int Decrypto(BigInteger c) {
		BigInteger nSquare = n.multiply(n);
		BigInteger Ans = c.modPow(lambda, nSquare);
		Ans = Ans.subtract(BigInteger.ONE).divide(n);
		Ans = Ans.remainder(n).multiply(nu).remainder(n);
		return Ans.intValue();
	}

	public String Decrypt(String C) {
		int len = C.length() / 22;
		String M = "";
		for (int i = 0; i < len; i++) {
			String Temp = C.substring(i * 22, i * 22 + 22);
			String m = Character.toString((char)Decrypto(new BigInteger(Temp)));
			M = M.concat(m);
		}
		return M;
	}
	
	
	public PaillierCryptoSystem() {
		p = 100000 + (new Random().nextInt(4) + 1) * 10000 + 1;
		while (!isPrime(p)) p += 2;
		q = p + 2;
		while (!isPrime(q)) q += 2;
		n = BigInteger.valueOf(p).multiply(BigInteger.valueOf(q));
		lambda = psi = lcm(p - 1, q - 1);
		g = n.add(BigInteger.valueOf(1));
		nu = psi.modInverse(n);
	}

	public PaillierCryptoSystem(int a, int b) {
		p = a;
		q = b;
		n = BigInteger.valueOf(p).multiply(BigInteger.valueOf(q));
		lambda = psi = lcm(p - 1, q - 1);
		g = n.add(BigInteger.valueOf(1));
		nu = psi.modInverse(n);
	}
}
