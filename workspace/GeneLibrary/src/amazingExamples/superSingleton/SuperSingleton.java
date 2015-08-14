package amazingExamples.superSingleton;

import java.util.HashMap;

import com.gene.modules.check.Check;
import com.gene.modules.exceptions.InstanceAlreadyExistException;


public class SuperSingleton
{
	private String memberVariable1;
	private String memberVariable2;

	private String instanceKey;
	private static HashMap<String, SuperSingleton> instanceRegistry;
	private static HashMap<String, Integer> instanceBeingUsed;
	static
	{
		instanceRegistry = new HashMap<String, SuperSingleton>();
		instanceBeingUsed = new HashMap<String, Integer>();
	}

	public SuperSingleton(String memberVariable1, String memberVariable2) 
	{
		this.initialize(memberVariable1, memberVariable2);
	}

	protected void finalize() throws Throwable
	{
		try
		{
			this.close();
		}
		finally
		{
			super.finalize();
		}
	}

	private synchronized void initialize(String memberVariable1, String memberVariable2)  
	{
		Check.notBlank(memberVariable1, memberVariable2);

		String instanceKey = generateInstanceKey(memberVariable1, memberVariable2);
		boolean instanceNotExist = putInstanceToRegistryIfNotExist(instanceKey, this);
		Check.allTrue(InstanceAlreadyExistException.class, this.getClass().getName()+" instance already exists", instanceNotExist);

		this.instanceKey = instanceKey;
		this.memberVariable1 = memberVariable1;
		this.memberVariable2 = memberVariable2;
	}

	public synchronized void close()
	{
		if(this.instanceKey != null)
		{
			boolean removed = removeInstanceFromRegistry(this.instanceKey);
			if(removed)
			{
				this.memberVariable1 = null;
				this.memberVariable2 = null;
				this.instanceKey = null;
			}
		}
	}

	public synchronized String getInstanceKey()
	{
		return this.instanceKey;
	}
	
	public String getMemberVariable1()
	{
		return memberVariable1;
	}

	public String getMemberVariable2()
	{
		return memberVariable2;
	}





	private static synchronized boolean putInstanceToRegistryIfNotExist(String instanceKey, SuperSingleton superSingleton)
	{
		boolean instanceNotExist = instanceRegistry.get(instanceKey) == null;
		if(instanceNotExist)
		{
			instanceRegistry.put(instanceKey, superSingleton);
			instanceBeingUsed.put(instanceKey, 1);
		}

		return instanceNotExist;
	}



	private static synchronized boolean removeInstanceFromRegistry(String instanceKey)
	{
		Check.notBlank(instanceKey);
		boolean notBeingUsed = instanceBeingUsed.get(instanceKey) == 1;
		if(notBeingUsed)
		{
			instanceRegistry.remove(instanceKey);
			instanceBeingUsed.remove(instanceKey);
		}
		else
		{
			instanceBeingUsed.put(instanceKey, instanceBeingUsed.get(instanceKey)-1);
		}

		return notBeingUsed;
	}




	public static synchronized SuperSingleton getInstance(String memberVariable1, String memberVariable2)  
	{
		Check.notBlank(memberVariable1, memberVariable2);

		String instanceKey = generateInstanceKey(memberVariable1, memberVariable2);

		SuperSingleton instance = instanceRegistry.get(instanceKey);
		if(instance != null)
		{
			instanceBeingUsed.put(instanceKey, instanceBeingUsed.get(instanceKey)+1);
		}
		else
		{
			instance = new SuperSingleton(memberVariable1, memberVariable2);
		}

		return instance;
	}


	private static String generateInstanceKey(String memberVariable1, String memberVariable2)
	{
		Check.notBlank(memberVariable1, memberVariable2);
		return memberVariable1+"|"+memberVariable2;
	}







	public static void main(String[] args)
	{
	}
}