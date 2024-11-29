package sn.niit.infinitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnseignantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enseignant getEnseignantSample1() {
        return new Enseignant().id(1L).prenom("prenom1").nom("nom1").specialite("specialite1").email("email1").telephone("telephone1");
    }

    public static Enseignant getEnseignantSample2() {
        return new Enseignant().id(2L).prenom("prenom2").nom("nom2").specialite("specialite2").email("email2").telephone("telephone2");
    }

    public static Enseignant getEnseignantRandomSampleGenerator() {
        return new Enseignant()
            .id(longCount.incrementAndGet())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .specialite(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString());
    }
}
