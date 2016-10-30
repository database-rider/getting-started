package com.github.database.rider.gettingstarted.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

import static com.github.database.rider.core.util.EntityManagerProvider.instance;

/**
 * Created by pestano on 09/10/15.
 */
@ApplicationScoped
public class EntityManagerProducer {

    private EntityManager em;


    @Produces
    public EntityManager produce() {
        return instance("riderDB").em();
    }

}
