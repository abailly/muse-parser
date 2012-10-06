/*
 *  Copyright 2006 OQube contact at oqube dot com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package oqube.muse.doxia;

import org.apache.maven.doxia.module.site.AbstractSiteModule;

/**
 * Implementation of SiteModule for the muse format.
 * 
 * 
 * @author oqube
 * @plexus.component role="org.apache.maven.doxia.module.site.SiteModule" role-hint="muse"
 */
public final class MuseSiteModule
    extends AbstractSiteModule
{

    /**
     * @see SiteModule#getSourceDirectory()
     */
    public String getSourceDirectory()
    {
        return "muse";
    }

    /**
     * @see SiteModule#getExtension()
     */
    public String getExtension()
    {
        return "muse";
    }

    /**
     * @see SiteModule#getParserId()
     */
    public String getParserId()
    {
        return "muse";
    }

}
