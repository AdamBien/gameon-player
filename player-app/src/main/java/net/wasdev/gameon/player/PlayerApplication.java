/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.wasdev.gameon.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/players/*")
public class PlayerApplication extends Application {
    /*
     * The context root for this application is /play
     * @ApplicationPath will tuck the entirety of the REST endpoint under v1: /play/players/
     * Any {@link Path} annotations at the class level are appended onto that, etc.
     */

    public final static Set<Class<?>> playerJaxRSClasses = new HashSet<Class<?>>(
            Arrays.asList(new Class<?>[] { 
                AllPlayersResource.class, 
                Health.class,
                PlayerResource.class, 
                Player.class,
                PlayerNotFoundException.class, 
                RequestNotAllowedForThisIDException.class }));

    @Override
    public Set<Class<?>> getClasses() {
        return playerJaxRSClasses;
    }

}
