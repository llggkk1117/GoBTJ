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
			System.out.println(e.toString() + " ������ �߻��߽��ϴ�");
			System.out.println("����ó�� ��ƾ ����");

			// �̿� ���� ������ü�� ������ ������
			// finally ������ ����ȴ�
			throw e;
		}
		finally
		{ 
			//������ ���� ���� �ʵ� ������ ����Ǵ� ���  
			System.out.println("finally ���� ����"); 
		} 
		System.out.println("���α׷��� ����");
	} 
	
	public static void main(String[] args)
	{
		test1();
	}
}
