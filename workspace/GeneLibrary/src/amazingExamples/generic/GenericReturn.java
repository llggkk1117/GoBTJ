package amazingExamples.generic;

import java.util.HashMap;



class Animal{}


class Dog extends Animal
{
	public void bark() 
	{
		System.out.println("bark!");
	}
}

class Duck extends Animal
{
	public void quack() 
	{
		System.out.println("quack!");
	}
}

class AnimalContainer
{
	private HashMap<String, Animal> container;
	public AnimalContainer()
	{
		this.container = new HashMap<String, Animal>();
	}

	public void put(String name, Animal animal)
	{
		this.container.put(name, animal);
	}

	public <T extends Animal>T getAnimal(String name, Class<T> type)
	{
		return type.cast(this.container.get(name));
	}

	public <T extends Animal>T getAnimal2(String name, Class<T> type)
	{
		Animal ani = this.container.get(name);
		T result = null;
		if(type.isInstance(ani))
		{
			result = type.cast(ani);
		}
		return result;
	}
}


public class GenericReturn
{
	public static void main(String[] args)
	{
		AnimalContainer ac = new AnimalContainer();
		ac.put("happy", new Dog());
		ac.put("donald", new Duck());

		ac.getAnimal("happy", Dog.class).bark();
		ac.getAnimal("donald", Duck.class).quack();

		System.out.println(ac.getAnimal2("happy", Duck.class));
	}
}
