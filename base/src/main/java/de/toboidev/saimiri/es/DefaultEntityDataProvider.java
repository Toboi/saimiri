package de.toboidev.saimiri.es;

import com.jme3.app.state.AbstractAppState;
import com.simsilica.es.EntityData;
import com.simsilica.es.base.DefaultEntityData;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class DefaultEntityDataProvider extends AbstractAppState implements EntityDataProvider {

    private final EntityData ed;

    public DefaultEntityDataProvider(EntityData ed) {
        this.ed = ed;
    }

    public DefaultEntityDataProvider() {
        ed = new DefaultEntityData();
    }

    @Override public EntityData getEntityData() {
        return ed;
    }
}
