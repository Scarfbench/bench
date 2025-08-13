package jakartaee.tutorial.roster.entity;

import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import jakartaee.tutorial.roster.entity.Team;
import javax.annotation.processing.Generated;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-08-12T14:19:52", comments="EclipseLink-3.0.0.v20201208-r3986bdbeae8e0e04e5be4a7076f2bda2ee1a09a5")
@StaticMetamodel(Player.class)
public class Player_ { 

    public static volatile CollectionAttribute<Player, Team> teams;
    public static volatile SingularAttribute<Player, String> name;
    public static volatile SingularAttribute<Player, String> id;
    public static volatile SingularAttribute<Player, String> position;
    public static volatile SingularAttribute<Player, Double> salary;

}