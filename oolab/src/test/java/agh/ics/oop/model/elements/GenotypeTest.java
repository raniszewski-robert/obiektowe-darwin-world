package agh.ics.oop.model.elements;

import agh.ics.oop.model.enums.GenomeVariant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTest {

    @Test
    public void testInitialization() {
        int genomeSize = 10;
        Genotype genotype = new Genotype(genomeSize);

        // Sprawdź rozmiar genomu
        assertEquals(genomeSize, genotype.getGenome().size());

        // Sprawdź, czy wartości genomu są w zakresie 0-8
        for (int gene : genotype.getGenome()) {
            assertTrue(gene >= 0 && gene <= 8);
        }

        // Sprawdź, czy bieżący indeks jest poprawny
        assertTrue(genotype.getCurrentGenomeIndex() >= 0 && genotype.getCurrentGenomeIndex() < genomeSize);
    }

    @Test
    public void testIndexChangeNormalVariant() {
        int genomeSize = 5;
        Genotype genotype = new Genotype(genomeSize);

        int initialIndex = genotype.getCurrentGenomeIndex();
        genotype.indexChange(GenomeVariant.NORMAL);

        // Sprawdź, czy indeks zwiększył się o 1 w trybie NORMAL
        assertEquals((initialIndex + 1) % genomeSize, genotype.getCurrentGenomeIndex());
    }

    @Test
    public void testIndexChangeCrazyVariant() {
        int genomeSize = 5;
        Genotype genotype = new Genotype(genomeSize);

        int initialIndex = genotype.getCurrentGenomeIndex();
        boolean indexChangedRandomly = false;

        // Testowanie trybu CRAZY kilkukrotnie, aby uwzględnić losowość
        for (int i = 0; i < 100; i++) {
            genotype.setCurrentGenomeIndex(initialIndex);
            genotype.indexChange(GenomeVariant.CRAZY);

            int newIndex = genotype.getCurrentGenomeIndex();
            if (newIndex != (initialIndex + 1) % genomeSize) {
                indexChangedRandomly = true;
                break;
            }
        }

        assertTrue(indexChangedRandomly, "Index should change randomly in CRAZY mode.");
    }

    @Test
    public void testCreateChildGenotype() {
        int genomeSize = 10;
        Genotype parent1 = new Genotype(genomeSize);
        Genotype parent2 = new Genotype(genomeSize);

        // Stwórz genom dziecka z określoną proporcją energii
        float energyPercent = 0.4f; // 40% genomu od parent1
        Genotype child = parent1.createChildGenotype(parent2, energyPercent);

        // Sprawdź, czy rozmiar genomu dziecka jest poprawny
        assertEquals(genomeSize, child.getGenome().size());

        // Sprawdź, czy genom dziecka składa się z odpowiednich części
        int splitIndex = (int) (energyPercent * genomeSize);
        assertEquals(parent1.getGenome().subList(0, splitIndex), child.getGenome().subList(0, splitIndex));
        assertEquals(parent2.getGenome().subList(splitIndex, genomeSize), child.getGenome().subList(splitIndex, genomeSize));
    }

    @Test
    public void testToString() {
        Genotype genotype = new Genotype(5);
        assertEquals(genotype.getGenome().toString(), genotype.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        int genomeSize = 5;

        Genotype genotype1 = new Genotype(genomeSize);
        Genotype genotype2 = new Genotype(genomeSize);

        // Sprawdź, czy dwa różne genotypy są różne
        assertNotEquals(genotype1, genotype2);

        // Sprawdź, czy hashCode dwóch różnych genotypów różni się
        assertNotEquals(genotype1.hashCode(), genotype2.hashCode());

        // Skopiuj genotyp 1 i porównaj
        Genotype genotypeCopy = new Genotype(genomeSize);
        genotypeCopy.genome = genotype1.getGenome(); // Skopiuj ręcznie genom
        assertEquals(genotype1, genotypeCopy);
        assertEquals(genotype1.hashCode(), genotypeCopy.hashCode());
    }
}
