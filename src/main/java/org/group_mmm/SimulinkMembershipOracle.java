package org.group_mmm;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import net.automatalib.incremental.mealy.tree.IncrementalMealyTreeBuilder;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The membership oracle for a Simulink model
 *
 * @author Masaki Waga {@literal <masakiwaga@gmail.com>}
 */
public class SimulinkMembershipOracle implements MembershipOracle.MealyMembershipOracle<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulinkMembershipOracle.class);

    protected final SimulinkSUL simulink;
    protected final SimulinkSULMapper mapper;
    IncrementalMealyTreeBuilder<String, String> cache;

    IncrementalMealyTreeBuilder<String, String> getCache() {
        return cache;
    }

    void setCache(IncrementalMealyTreeBuilder<String, String> cache) {
        this.cache = cache;
    }

    SimulinkMembershipOracle(SimulinkSUL simulink, SimulinkSULMapper mapper) {
        this.simulink = simulink;
        this.mapper = mapper;
        this.cache = new IncrementalMealyTreeBuilder<>(mapper.constructAbstractAlphabet());
    }

    /** {@inheritDoc} */
    @Override
    public void processQueries(Collection<? extends Query<String, Word<String>>> queries) {
        for (Query<String, Word<String>> q : queries) {
            final Word<String> abstractInput = q.getInput();
            WordBuilder<String> abstractOutputBuilder = new WordBuilder<>(abstractInput.size());

            if (!cache.lookup(abstractInput, abstractOutputBuilder)) {
                abstractOutputBuilder.clear();
                final Word<List<Double>> concreteInput = Word.fromList(
                        abstractInput.stream().map(mapper::mapInput).collect(Collectors.toList()));
                assert concreteInput.size() == q.getInput().size();

                final Word<List<Double>> concreteOutput;
                try {
                    concreteOutput = simulink.execute(concreteInput);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return;
                }
                assert concreteOutput.size() == concreteInput.size();
                abstractOutputBuilder.append(
                        new IOSignal(concreteInput, concreteOutput).stream().map(mapper::mapOutput).collect(Collectors.toList()));

                assert concreteOutput.size() == abstractOutputBuilder.toWord().size();
                cache.insert(abstractInput, abstractOutputBuilder.toWord());
            }

            final Word<String> output = abstractOutputBuilder.toWord().suffix(q.getSuffix().length());
            q.answer(output);
        }
    }

    void cacheInsert(Word<String> abstractInput, Word<String> abstractOutput) {
        cache.insert(abstractInput, abstractOutput);
    }
}

