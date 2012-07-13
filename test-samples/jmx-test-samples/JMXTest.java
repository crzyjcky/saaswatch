import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		JMXServiceURL u = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
		JMXConnector c = JMXConnectorFactory.connect(u);
		MBeanServerConnection mbsc = c.getMBeanServerConnection();

		MemoryMXBean mbean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
				ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);

		while (true) {
			System.out.println(mbean.getHeapMemoryUsage());
			System.out.println(mbean.getNonHeapMemoryUsage());
			Thread.sleep(100);
		}

	}
}