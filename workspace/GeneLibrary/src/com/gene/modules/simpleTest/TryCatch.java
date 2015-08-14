package com.gene.modules.simpleTest;

public class TryCatch
{

	public static void test1()
	{
		try
		{
			String str = null;
//			String str = "aaa";
			System.out.println(str.length());
		}
		catch(NullPointerException e)
		{ 
			System.out.println(e.toString() + " 에러가 발생했습니다");
			System.out.println("에러처리 루틴 실행");

			// 이와 같이 에러객체를 밖으로 던져도
			// finally 구문은 실행된다
			throw e;
		}
		finally
		{ 
			//에러가 나든 나지 않든 무조건 실행되는 블록  
			System.out.println("finally 구문 실행"); 
		} 
		System.out.println("프로그램의 종료");
	} 
	
	public static void main(String[] args)
	{
		test1();
	}
}
