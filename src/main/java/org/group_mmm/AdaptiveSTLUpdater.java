package org.group_mmm;

import de.learnlib.api.oracle.BlackBoxOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.oracle.PropertyOracle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public interface AdaptiveSTLUpdater extends BlackBoxOracle.MealyBlackBoxOracle<String, String> {
    List<STLCost> getSTLProperties();

    /**
     * Returns the current list of LTL formulas in the string representation
     */
    List<String> getLTLProperties();

    /**
     * Returns the current list of LTL formulas in MealyPropertyOracle
     */
    List<PropertyOracle.MealyPropertyOracle<String, String, String>> list();

    /**
     * Returns the current list of LTL formulas in MealyPropertyOracle in stream
     */
    Stream<PropertyOracle.MealyPropertyOracle<String, String, String>> stream();

    /**
     * Returns the number of the current list of STL formulas
     */
    int size();

    /**
     * Set new membership oracle. This is necessary to call list() and stream()
     */
    void setMemOracle(@NotNull MembershipOracle.MealyMembershipOracle<String, String> memOracle);
}
