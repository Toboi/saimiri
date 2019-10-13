package de.toboidev.saimiri.es;

import com.jme3.app.state.AppState;
import com.simsilica.es.EntityData;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public interface EntityDataProvider extends AppState {

    EntityData getEntityData();
}
