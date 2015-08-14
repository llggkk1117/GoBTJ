package com.gene.modules.ACMPractice;

import java.util.Vector;

public class LargestPrimeFactorFinder
{
	public static long find(final long number)
	{
		long num = number;
		
		while((num % 2 == 0)&&(2 <= (num/2)))
		{
			num = num / 2;
		}
		
		long seed = 3;
		while(seed <= (num/seed))
		{
			if(num % seed == 0)
			{
				num = num / seed;
				seed = 3;
			}
			else
			{
				seed+=2;
			}
		}
		
		if(num == number)
		{
			num = 1;
		}
		
		return num;
	}
	
	public static long get(final long number)
	{
		Vector<Long> primeList = new Vector<Long>();
		primeList.add(Long.parseLong("2"));
		primeList.add(Long.parseLong("3"));
		primeList.add(Long.parseLong("5"));
		primeList.add(Long.parseLong("7"));
		long total = 17;
		
		long primeCandiate = 11L;
		int[] jump = new int[]{2,4,2,2};
		int jumpIndex = 0;
		
		boolean prime;
		while(primeCandiate <= number)
		{
			prime = true;
			for(int i=0; (i<primeList.size())&&(primeList.elementAt(i) <= primeCandiate/primeList.elementAt(i)); ++i)
			{
				if((primeCandiate % primeList.elementAt(i)) == 0)
				{
					prime = false;
					break;
				}
			}
			if(prime)
			{
				primeList.add(primeCandiate);
				total += primeCandiate;
				//System.out.println(primeCandiate);
			}

			primeCandiate += jump[jumpIndex]; 
			jumpIndex = (++jumpIndex)%4;
		}
		
		
		
		
		return total;
	}


	
	public static boolean isPrime(final long number)
	{
		int[] jump = new int[]{2,4,2,2};
		int jumpIndex = 0;
		boolean prime = true;
		boolean answerFound = false;
		
		if((number % 2 == 0)||(number % 3 == 0)||(number % 5 == 0)||(number % 7 == 0))
		{
			prime = false;
			answerFound = true;
		}
		
		long seed = 11;
		while((seed <= number/seed)&&(!answerFound))
		{
			if(number % seed == 0)
			{
				prime = false;
				answerFound = true;
			}
			else
			{
				seed += jump[jumpIndex];
				jumpIndex = (++jumpIndex)%4;
				System.out.println(seed+", "+(number/seed));
			}
		}
		
		return prime;
	}
	
	
	
	public static void main(String[] args)
	{
		LargestPrimeFactorFinder f = new LargestPrimeFactorFinder();
//		long start = System.nanoTime();
//		System.out.println(LargestPrimeFactorFinder.find2(Long.parseLong("6008514751435")));
//		System.out.println(LargestPrimeFactorFinder.find2(Long.parseLong("6008514751439")));
//		System.out.println(LargestPrimeFactorFinder.find2(Long.parseLong("324630")));
//		long end = System.nanoTime();
//		double diff = (double)(end - start) / (double)1000000;
//		System.out.println(diff+"ms");

		long start = System.nanoTime();
		//System.out.println(LargestPrimeFactorFinder.isPrime(Long.parseLong("32463773")));
		//System.out.println(LargestPrimeFactorFinder.find(Long.parseLong("600851475143")));
		System.out.println(LargestPrimeFactorFinder.get(Long.parseLong("2000000")));
		long end = System.nanoTime();
		double diff = (double)(end - start) / (double)1000000;
		System.out.println(diff+"ms");

	}
}
