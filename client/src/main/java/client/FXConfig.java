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

import client.scenes.AddQuoteCtrl;
import client.scenes.BoardOverviewCtrl;
import client.scenes.CardPopupCtrl;
import client.scenes.MainCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class FXConfig implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddQuoteCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardPopupCtrl.class).in(Scopes.SINGLETON);
    }
}