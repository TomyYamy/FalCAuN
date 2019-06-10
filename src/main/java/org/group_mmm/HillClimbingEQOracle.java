package org.group_mmm;

import de.learnlib.api.oracle.PropertyOracle;
import net.automatalib.words.Word;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HillClimbingEQOracle extends AbstractSelectEQOracle {
    HillClimbingEQOracle(SimulinkMembershipOracleCost memOracle, int length, Random random, int maxTests, int generationSize, int childrenSize, boolean resetWord) {
        super(memOracle, length, random, maxTests, generationSize, childrenSize, resetWord);
    }

    HillClimbingEQOracle(SimulinkMembershipOracleCost memOracle, int length, Random random, int maxTests, int generationSize, int childrenSize, boolean resetWord, PropertyOracle.MealyPropertyOracle<String, String, String> ltlOracle) {
        super(memOracle, length, random, maxTests, generationSize, childrenSize, resetWord, ltlOracle);
    }

    @Override
    protected List<Word<String>> createNextGeneration(List<Word<String>> goodSamples) {
        return goodSamples.stream().flatMap(w -> neighborhoodStream(w).limit(this.childrenSize)).collect(Collectors.toList());

    }

    private Stream<Word<String>> neighborhoodStream(Word<String> input) {
        return IntStream.range(0, input.length()).boxed().flatMap(
                index -> this.symbolList.stream().filter(s -> s != input.getSymbol(index)).map(s ->
                {
                    List<String> tmp = input.stream().collect(Collectors.toList());
                    tmp.set(index, s);
                    return Word.fromList(tmp);
                }));
    }
}