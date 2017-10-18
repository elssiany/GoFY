/**
 * Copyright 2016 Google Inc. All Rights Reserved.
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
 */

package com.dkbrothers.app.gofy.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class JGFirebaInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Se llama si el token InstanceID se actualiza. Esto puede ocurrir si la
          ficha anterior había quedado comprometida.
     Tenga en cuenta que esto se llama cuando el token InstanceID
          se genera inicialmente por lo que es donde se recupera el token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //Si desea enviar mensajes a esta instancia de aplicación o
        //administrar estas suscripciones de aplicaciones en el lado del servidor,
        //Identificador de Instancia a su servidor de aplicaciones.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     *Seguir token a servidores de terceros.   
     *Modifique este método para asociar el token InstanceID del FCM del usuario con cualquier
     * cuenta del lado del servidor mantenido por su aplicación.
     *
     * @param token El nuevo token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implemente este método para enviar un token a su servidor de aplicaciones.


    }



}
