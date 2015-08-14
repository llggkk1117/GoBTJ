package amazingExamples.casting;

public class ArrayCasting
{
	public static void main(String[] args)
	{
		String[] str = new String[]{"aa", "bb"};
		Object[] obj = (Object[]) str;
		for(int i=0; i<obj.length; ++i)
		{
			System.out.println(obj[i]);
		}
		
		System.out.println(obj instanceof String[]);
		
		String[] str2 = (String[]) obj;
		for(int i=0; i<str2.length; ++i)
		{
			System.out.println(str2[i]);
		}
		
		System.out.println(str instanceof Object[]);
		
		Integer[] intArray1 = new Integer[]{1,2,3};
		System.out.println(intArray1 instanceof Object[]);
	}
}
