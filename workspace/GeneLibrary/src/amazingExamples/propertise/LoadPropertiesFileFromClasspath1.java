/**
 Information
 URL: http://www.mkyong.com/java/java-properties-file-examples/
 */

package amazingExamples.propertise;

import java.io.IOException;
import java.util.Properties;

// Load a properties file named “config.properties” from project classpath, and retrieved the property value.
public class LoadPropertiesFileFromClasspath1
{
	public static void main( String[] args )
	{
		Properties prop = new Properties();
		try
		{
			//load a properties file from class path, inside static method
			prop.load(LoadPropertiesFileFromClasspath1.class.getClassLoader().getResourceAsStream("config.properties"));
			
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
}
