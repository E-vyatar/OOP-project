/*
 * Copyright 2021 Delft University of Technology
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
 */
package client;

import client.scenes.*;
import client.scenes.service.AddCardService;
import client.scenes.service.DeleteListService;
import client.scenes.service.RenameListService;
import client.utils.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class FXConfig implements Module {

    /**
     * Configure the bindings for the {@link com.google.inject.Injector}.
     *
     * @param binder the Binder for which to configure.
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(PollingUtils.class).in(Scopes.SINGLETON);
        binder.bind(SocketsUtils.class).in(Scopes.SINGLETON);
        binder.bind(ClientConfig.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardPopupCtrl.class).in(Scopes.SINGLETON);
        binder.bind(RenameListPopupCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ConnectServerCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(DeleteCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(PollingUtils.class).in(Scopes.SINGLETON);
        binder.bind(DeleteListPopupCtrl.class).in(Scopes.SINGLETON);
        binder.bind(DeleteListService.class).in(Scopes.SINGLETON);
        binder.bind(RenameListService.class).in(Scopes.SINGLETON);
        binder.bind(AddCardService.class).in(Scopes.SINGLETON);
    }
}