/**
 Information
 URL: http://www.mkyong.com/java/java-properties-file-examples/
 */

package amazingExamples.propertise;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadPropertiesFile
{
	public static void main( String[] args )
	{
		Properties prop = new Properties();
		try
		{
			//load a properties file
			prop.load(new FileInputStream("settings/amazingExamples/properties/config.properties"));

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
