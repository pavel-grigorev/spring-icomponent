package org.thepavel.icomponent.registrar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.thepavel.icomponent.InterfaceComponentConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InterfaceComponentConfigurationRegistrarTest {
  @Test
  public void addsBeanDefinitionToRegistry() {
    BeanDefinitionRegistry registry = new DummyBeanDefinitionRegistry();
    InterfaceComponentConfigurationRegistrar.registerConfiguration(registry);
    BeanDefinition beanDefinition = registry.getBeanDefinition("interfaceComponentConfiguration");

    assertNotNull(beanDefinition);
    assertEquals(InterfaceComponentConfiguration.class.getName(), beanDefinition.getBeanClassName());
  }
}
