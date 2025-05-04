package ru.lavrent.weblab3.beans;

import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class MBeanRegistrationListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(MBeanRegistrationListener.class.getName());

	private static final String HIT_ATTEMPT_COUNTER_OBJECT_NAME = "ru.lavrent.weblab3.beans:type=PointCounter";
	private static final String AREA_CALCULATOR_OBJECT_NAME = "ru.lavrent.weblab3.beans:type=AreaCalculator";

	private PointCounter hitAttemptCounter;
	private AreaCalculator areaCalculator;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

			hitAttemptCounter = new PointCounter();
			ObjectName hitCounterName = new ObjectName(HIT_ATTEMPT_COUNTER_OBJECT_NAME);
			mbeanServer.registerMBean(hitAttemptCounter, hitCounterName);
			sce.getServletContext().setAttribute("pointCounterMBean", hitAttemptCounter);
			logger.info("PointCounter MBean registered successfully.");

			areaCalculator = new AreaCalculator();
			ObjectName areaCalculatorName = new ObjectName(AREA_CALCULATOR_OBJECT_NAME);
			mbeanServer.registerMBean(areaCalculator, areaCalculatorName);
			sce.getServletContext().setAttribute("areaCalculatorMBean", areaCalculator);
			logger.info("AreaCalculator MBean registered successfully.");

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while registering MBeans: " + e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

			ObjectName hitCounterName = new ObjectName(HIT_ATTEMPT_COUNTER_OBJECT_NAME);
			if (mbeanServer.isRegistered(hitCounterName)) {
				mbeanServer.unregisterMBean(hitCounterName);
				logger.info("PointCounter MBean unregistered successfully.");
			}

			ObjectName areaCalculatorName = new ObjectName(AREA_CALCULATOR_OBJECT_NAME);
			if (mbeanServer.isRegistered(areaCalculatorName)) {
				mbeanServer.unregisterMBean(areaCalculatorName);
				logger.info("AreaCalculator MBean unregistered successfully.");
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while unregistering MBeans: " + e.getMessage(), e);
		}
	}
}
