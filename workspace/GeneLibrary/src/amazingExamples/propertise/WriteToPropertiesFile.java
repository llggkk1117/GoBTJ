/**
 Information
 URL: http://www.mkyong.com/java/java-properties-file-examples/
 */

package amazingExamples.propertise;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriteToPropertiesFile
{
	public static void main( String[] args )
    {
    	Properties prop = new Properties();
    	try
    	{
    		//set the properties value
    		prop.setProperty("database", "localhost");
    		prop.setProperty("dbuser", "mkyong");
    		prop.setProperty("dbpassword", "password");
    		
    		//save properties to project root folder
    		prop.store(new FileOutputStream("settings/amazingExamples/properties/config.properties"), null);
    	}
    	catch (IOException ex)
    	{
    		ex.printStackTrace();
        }
    }
}
