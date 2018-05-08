/*!
 * Copyright 2010 - 2018 Hitachi Vantara.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.pentaho.webpackage.extender.requirejs.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.pentaho.requirejs.IRequireJsPackage;
import org.pentaho.webpackage.core.IPentahoWebPackage;

/**
 * Tracks registered {@link IPentahoWebPackage} services and in turn registers {@link IRequireJsPackage} service.
 */
public class PentahoWebPackageServiceTracker implements ServiceTrackerCustomizer<IPentahoWebPackage, IRequireJsPackage> {
  private final BundleContext context;

  public PentahoWebPackageServiceTracker( BundleContext context ) {
    this.context = context;
  }

  @Override
  public IRequireJsPackage addingService( ServiceReference<IPentahoWebPackage> reference ) {
    Bundle bundle = reference.getBundle();
    // if null then the service is unregistered
    if ( bundle != null ) {
      RequireJsPackageImpl requireJsPackage = new RequireJsPackageImpl( bundle.getBundleContext(), this.context.getService( reference ) );
      requireJsPackage.register();

      return requireJsPackage;
    }

    return null;
  }

  @Override
  public void modifiedService( ServiceReference<IPentahoWebPackage> reference, IRequireJsPackage mapping ) {
  }

  @Override
  public void removedService( ServiceReference<IPentahoWebPackage> reference, IRequireJsPackage mapping ) {
    this.context.ungetService( reference );

    mapping.unregister();
  }
}
