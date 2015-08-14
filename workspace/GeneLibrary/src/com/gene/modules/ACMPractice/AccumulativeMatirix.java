package com.gene.modules.ACMPractice;

public class AccumulativeMatirix
{

	public static void main(String[] args)
	{
		int[][] matrix_original = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		int[][] matrix_acc = new int[matrix_original.length][matrix_original[0].length];
		
		for(int i=0; i<matrix_acc.length; ++i)
		{
			for(int j=0; j<matrix_acc[i].length; ++j)
			{
				matrix_acc[i][j] = (j==0 ? 0 :matrix_acc[i][j-1])+matrix_original[i][j];
				for(int i2=0; i2<i; ++i2)
				{
					matrix_acc[i][j] += matrix_original[i2][j];
				}
			}
		}
		
		
		
		for(int i=0; i<matrix_original.length; ++i)
		{
			for(int j=0; j<matrix_original[i].length; ++j)
			{
				System.out.print(matrix_original[i][j]);
				if(j < matrix_original[i].length-1)
				{
					System.out.print("\t");
				}
				else
				{
					System.out.print("\n");
				}
			}
		}
		
		System.out.print("\n\n");
		
		for(int i=0; i<matrix_acc.length; ++i)
		{
			for(int j=0; j<matrix_acc[i].length; ++j)
			{
				System.out.print(matrix_acc[i][j]);
				if(j < matrix_acc[i].length-1)
				{
					System.out.print("\t");
				}
				else
				{
					System.out.print("\n");
				}
			}
		}
	}

}
