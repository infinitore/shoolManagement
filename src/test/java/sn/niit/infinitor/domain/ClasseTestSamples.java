package sn.niit.infinitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClasseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Classe getClasseSample1() {
        return new Classe().id(1L).nom("nom1").niveau("niveau1").anneeScolaire("anneeScolaire1");
    }

    public static Classe getClasseSample2() {
        return new Classe().id(2L).nom("nom2").niveau("niveau2").anneeScolaire("anneeScolaire2");
    }

    public static Classe getClasseRandomSampleGenerator() {
        return new Classe()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .niveau(UUID.randomUUID().toString())
            .anneeScolaire(UUID.randomUUID().toString());
    }
}
