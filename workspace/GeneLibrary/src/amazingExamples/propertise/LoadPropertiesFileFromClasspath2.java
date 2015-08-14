/**
 Information
 URL: http://www.mkyong.com/java/java-properties-file-examples/
 */

package amazingExamples.propertise;

import java.io.IOException;
import java.util.Properties;

public class LoadPropertiesFileFromClasspath2
{
	public void function()
	{
		Properties prop = new Properties();
		try
		{
			//load a properties file from class path for non-static method
			prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

			//get the property value and print it out
			System.out.println(prop.getProperty("database"));
			System.out.println(prop.getProperty("dbuser"));
			System.out.println(prop.getProperty("dbpassword"));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public static void main( String[] args )
	{
		LoadPropertiesFileFromClasspath2 sample = new LoadPropertiesFileFromClasspath2();
		sample.function();
	}
}
