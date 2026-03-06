package com.gluonhq.jfxapps.core.api.driver;

import org.springframework.context.annotation.Fallback;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;

@ApplicationSingleton
@Fallback
public class GenericDriver extends AbstractDriver {

     public GenericDriver(DriverRegistry registry) {
         super(registry);
     }

}
