package org.thepavel.icomponent.registrar;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionOverrideException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.thepavel.icomponent.InterfaceComponentConfiguration;

public class InterfaceComponentConfigurationRegistrar {
  public static void registerConfiguration(BeanDefinitionRegistry registry) {
    BeanDefinition beanDefinition = getConfigurationBeanDefinition();
    String beanName = getBeanName(beanDefinition, registry);

    try {
      registry.registerBeanDefinition(beanName, beanDefinition);
    } catch (BeanDefinitionOverrideException ignored) {
      // already registered
    }
  }

  private static BeanDefinition getConfigurationBeanDefinition() {
    return new AnnotatedGenericBeanDefinition(InterfaceComponentConfiguration.class);
  }

  private static String getBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
    return AnnotationBeanNameGenerator.INSTANCE.generateBeanName(beanDefinition, registry);
  }
}
