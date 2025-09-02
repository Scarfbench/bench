package jakarta.tutorial.addressbook.repository;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.tutorial.addressbook.entity.Contact;

@ApplicationScoped
public class ContactRepository extends AbstractRepository<Contact> {

    @Inject
    EntityManager em;

    public ContactRepository() {
        super(Contact.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
