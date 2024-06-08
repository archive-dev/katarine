package org.katarine.systems;

import org.katarine.logging.LogLevel;
import org.katarine.logging.Logger;
import org.katarine.utils.structs.concurrent.ConcurrentClassUniqueHashSet;

import java.util.Set;

public final class SystemManager extends System {
    private final ConcurrentClassUniqueHashSet<System> registeredSystems = new ConcurrentClassUniqueHashSet<>();

    private final ConcurrentClassUniqueHashSet<System> systemRegister = new ConcurrentClassUniqueHashSet<>();
    private final ConcurrentClassUniqueHashSet<System> systemUnregister = new ConcurrentClassUniqueHashSet<>();

    public SystemManager() {
        setSystemManager(this);
    }

    private void processChanges() {
        systemRegister.removeAll(systemUnregister);

        registeredSystems.removeAll(systemUnregister);
        registeredSystems.addAll(systemRegister);
        systemRegister.clear();
        systemUnregister.clear();
    }

    private final Logger logger = new Logger(LogLevel.DEBUG);

    @Override
    public void awake() {
        processChanges();
        //            logger.debug(system + " is awaken");
        registeredSystems.forEachSynchronized(8L, System::init);
    }

    @Override
    public void start() {
        processChanges();
        //            logger.debug(system + " is started");
        registeredSystems.forEachSynchronized(8L, System::create);
    }

    @Override
    public void preUpdate() {
        processChanges();
        registeredSystems.forEachSynchronized(8L, System::preRender);
    }

    @Override
    public void update() {
        registeredSystems.forEachSynchronized(8L, System::render);
    }

    @Override
    public void postUpdate() {
        registeredSystems.forEachSynchronized(8L, System::postRender);
//        registeredSystems.removeAll(systemUnregister);
    }

    @Override
    public void die() {
        registeredSystems.forEachSynchronized(8L, System::dispose);
    }

    public <T extends System> T registerSystem(T system) {
        systemRegister.add(system);
        system.setSystemManager(this);
        system.init();
        return system;
    }
    
    public void unregisterSystem(System system) {
        systemUnregister.add(system);
        system.dispose();
    }

    @SuppressWarnings("unchecked")
    public <T extends System> T getSystem(Class<T> systemClass) {
        var ret = this.registeredSystems.getByClass(systemClass);
        if (ret!=null)
            return (T) ret;
        return (T) this.systemRegister.getByClass(systemClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends System> T getExactSystem(Class<T> systemClass) {
        var ret = this.registeredSystems.getByExactClass(systemClass);
        if (ret!=null)
            return (T) ret;
        return (T) this.systemRegister.getByExactClass(systemClass);
    }

    @SuppressWarnings("unchecked")
    public Set<System> getSystems() {
        return (Set<System>) registeredSystems.clone();
    }
}
