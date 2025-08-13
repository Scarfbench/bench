package jakartaee.tutorial.roster.entity;

import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import jakartaee.tutorial.roster.entity.League;
import jakartaee.tutorial.roster.entity.Player;
import javax.annotation.processing.Generated;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-08-12T14:19:52", comments="EclipseLink-3.0.0.v20201208-r3986bdbeae8e0e04e5be4a7076f2bda2ee1a09a5")
@StaticMetamodel(Team.class)
public class Team_ { 

    public static volatile SingularAttribute<Team, String> city;
    public static volatile CollectionAttribute<Team, Player> players;
    public static volatile SingularAttribute<Team, League> league;
    public static volatile SingularAttribute<Team, String> name;
    public static volatile SingularAttribute<Team, String> id;

}